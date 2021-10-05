package com.haianh.authenticationserver.security.jwt;


import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class Key {
    private final JwtConfig jwtConfig;

    @Autowired
    public Key(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public SecretKey getSecretKey(JwtConfig jwtConfig) {
        return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
    }
}
