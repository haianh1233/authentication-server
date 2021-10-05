package com.haianh.authenticationserver.controller;

import com.haianh.authenticationserver.model.AuthenticationRequest;
import com.haianh.authenticationserver.model.AuthenticationResponse;
import com.haianh.authenticationserver.model.GoogleAuthenticationRequest;
import com.haianh.authenticationserver.model.Token;
import com.haianh.authenticationserver.service.AuthenticationService;
import com.haianh.authenticationserver.service.AuthenticationServiceImplV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@RequestMapping("/api/v1/authentication")
@CrossOrigin(
        allowCredentials = "true",
        origins = {"http://localhost:3000", "http://localhost:5500", "https://lasa-fpt.web.app"},
        allowedHeaders = {
                CONTENT_TYPE,
                CONTENT_LENGTH,
                HOST,
                USER_AGENT,
                ACCEPT,
                ACCEPT_ENCODING,
                CONNECTION,
                AUTHORIZATION
        },
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS}
)
@RestController
public class AuthenticationControllerImplV1 implements AuthenticationController{

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationControllerImplV1(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public ResponseEntity<?> UsernamePasswordAuthentication(AuthenticationRequest authenticationRequest,
                                                 HttpServletResponse response,
                                                 HttpServletRequest request) {
        try {
            Token token = authenticationService.authenticateUsernameAndPassword(authenticationRequest);

            return ResponseEntity.status(OK)
                    .body(AuthenticationResponse.builder()
                            .status(OK.value())
                            .accessToken(token.getAccessToken())
                            .refreshToken(token.getRefreshToken())
                            .build()
                    );
        }catch (BadCredentialsException ex) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(AuthenticationResponse.builder()
                            .status(UNAUTHORIZED.value())
                            .error(ex.getClass().getSimpleName())
                            .message(ex.getMessage())
                            .path(request.getRequestURI())
                            .build()
                    );
        }
    }

    @Override
    public ResponseEntity<?> googleAuthentication(GoogleAuthenticationRequest authenticationRequest, String role, HttpServletResponse response, HttpServletRequest request) {
        try {
            Token token = authenticationService.authenticateGoogleAccount(authenticationRequest, role);
            if(token == null) {
                return ResponseEntity.status(UNAUTHORIZED)
                        .body("Account not found");
            }else {
                return ResponseEntity.status(OK)
                        .body(AuthenticationResponse.builder()
                                .status(OK.value())
                                .accessToken(token.getAccessToken())
                                .refreshToken(token.getRefreshToken())
                                .build()
                        );
            }
        } catch (GeneralSecurityException | IOException ex) {

            return ResponseEntity.status(NOT_ACCEPTABLE)
                    .body(AuthenticationResponse.builder()
                            .status(NOT_ACCEPTABLE.value())
                            .error(ex.getClass().getSimpleName())
                            .message(ex.getMessage())
                            .path(request.getRequestURI())
                            .build()
                    );
        } catch (AuthenticationServiceImplV1.EmailDomainException ex) {

            return ResponseEntity.status(UNAUTHORIZED)
                    .body(AuthenticationResponse.builder()
                            .status(UNAUTHORIZED.value())
                            .error(ex.getClass().getSimpleName())
                            .message(ex.getMessage())
                            .path(request.getRequestURI())
                            .build()
                    );
        }
    }
}
