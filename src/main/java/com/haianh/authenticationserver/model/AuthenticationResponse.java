package com.haianh.authenticationserver.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthenticationResponse {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private String accessToken;
    private String refreshToken;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Builder
    public AuthenticationResponse(int status, String error, String message, String path,String accessToken, String refreshToken) {
        this.timestamp = LocalDateTime.now().format(formatter);
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
