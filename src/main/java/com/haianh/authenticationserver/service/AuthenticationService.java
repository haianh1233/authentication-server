package com.haianh.authenticationserver.service;


import com.haianh.authenticationserver.model.AuthenticationRequest;
import com.haianh.authenticationserver.model.GoogleAuthenticationRequest;
import com.haianh.authenticationserver.model.Token;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public interface AuthenticationService {

    Token authenticateUsernameAndPassword(AuthenticationRequest authenticationRequest) throws BadCredentialsException;

    Token authenticateGoogleAccount(GoogleAuthenticationRequest googleAuthenticationRequest, String role)
            throws GeneralSecurityException, IOException, AuthenticationServiceImplV1.EmailDomainException;
}
