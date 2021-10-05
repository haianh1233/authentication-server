package com.haianh.authenticationserver.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.haianh.authenticationserver.entity.Admin;
import com.haianh.authenticationserver.entity.Lecturer;
import com.haianh.authenticationserver.entity.Student;
import com.haianh.authenticationserver.model.AuthenticationRequest;
import com.haianh.authenticationserver.model.GoogleAuthenticationRequest;
import com.haianh.authenticationserver.model.Token;
import com.haianh.authenticationserver.repository.AdminRepository;
import com.haianh.authenticationserver.repository.LecturerRepository;
import com.haianh.authenticationserver.repository.StudentRepository;
import com.haianh.authenticationserver.security.jwt.JwtUtil;
import com.haianh.authenticationserver.security.permission.ApplicationUserRole;
import com.haianh.authenticationserver.security.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;

import static com.haianh.authenticationserver.security.permission.ApplicationUserRole.LECTURER;
import static com.haianh.authenticationserver.security.permission.ApplicationUserRole.STUDENT;

@Component
public class AuthenticationServiceImplV1 implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;
    private final String CLIENT_ID1 = "69016056321-5j2fr23vo8oggc3jsqksgu2a4g1s1mhn.apps.googleusercontent.com";
    private final String CLIENT_ID2 = "431681257434-1makcgok3vs3tnivthtvcbnt2fcc1aul.apps.googleusercontent.com";

    @Autowired
    public AuthenticationServiceImplV1(AuthenticationManager authenticationManager,
                                       UserDetailsService userDetailsService,
                                       JwtUtil jwtUtil,
                                       StudentRepository studentRepository,
                                       LecturerRepository lecturerRepository,
                                       AdminRepository adminRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.studentRepository = studentRepository;
        this.lecturerRepository = lecturerRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public Token authenticateUsernameAndPassword(AuthenticationRequest authenticationRequest) throws BadCredentialsException {
        Optional<Admin> admin = adminRepository
                .findAdminByUsernameAndPassword(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        if(admin.isPresent()) {
            User userDetails = new User(admin.get());
            return generateToken(jwtUtil, userDetails);
        }else {
            throw  new BadCredentialsException("Incorrect username or password");
        }
    }

    @Override
    public Token authenticateGoogleAccount(GoogleAuthenticationRequest googleAuthenticationRequest,String role)
            throws GeneralSecurityException, IOException, EmailDomainException {
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                //.setAudience(Collections.singletonList("431681257434-1makcgok3vs3tnivthtvcbnt2fcc1aul.apps.googleusercontent.com"))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .setAudience(Arrays.asList(CLIENT_ID1, CLIENT_ID2))
                .build();
        GoogleIdToken idToken = verifier.verify(googleAuthenticationRequest.getToken());

        if(idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();

            if (!email.endsWith("@fpt.edu.vn") && !email.endsWith("fe.edu.vn"))
                throw new EmailDomainException("Invalid email domain");

            try{
                Optional<Student> student = studentRepository.findStudentByEmail(email);
                Optional<Lecturer> lecturer = lecturerRepository.findLecturerByEmail(email);
                if(student.isPresent()) {
                    User userDetails = student.map(User::new).get();
                    String accessToken =  jwtUtil.generateAccessToken(userDetails);
                    String refreshToken = jwtUtil.generateRefreshToken(userDetails);
                    return Token.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                }else if(lecturer.isPresent()) {
                    User userDetails = lecturer.map(User::new).get();
                    return generateToken(jwtUtil, userDetails);
                }else {
                    throw new UsernameNotFoundException(String.format("%s not found", email));
                }
            }catch (UsernameNotFoundException ex) {
                //if role !=null create new user by role student or lecturer
                if(role != null) {
                    if(role.equalsIgnoreCase(STUDENT.name())) {
                        String name = (String) payload.get("name");
                        String pictureUrl = (String) payload.get("picture");
                        Student student = Student.builder()
                                .email(email)
                                .name(name)
                                .status(1) //set student to active
                                .avatarUrl(pictureUrl)
                                .build();
                        //save student to database
                        studentRepository.saveAndFlush(student);
                        //load user from database again to make sure user already in db and for generate user detail
                        User userDetails = new User(student);
                        return generateToken(jwtUtil, userDetails);
                    }
                    if(role.equalsIgnoreCase(LECTURER.name())) {
                        String name = (String) payload.get("name");
                        String pictureUrl = (String) payload.get("picture");
                        Lecturer lecturer = Lecturer.builder()
                                .email(email)
                                .name(name)
                                .status(0) //set lecturer status to inactive
                                .avatarUrl(pictureUrl)
                                .build();
                        lecturerRepository.saveAndFlush(lecturer);
                        //load user from database again to make sure user already in db and for generate user detail
                        User userDetails = new User(lecturer);
                        return generateToken(jwtUtil, userDetails);
                    }
                }
            }
        }

        return null;
    }

    private Token generateToken(JwtUtil jwtUtil, User userDetails) {
        String accessToken =  jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public class EmailDomainException extends Exception{
        public EmailDomainException(String message) {
            super(message);
        }
    }
}
