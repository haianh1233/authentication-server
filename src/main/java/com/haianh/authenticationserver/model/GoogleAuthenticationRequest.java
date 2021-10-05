package com.haianh.authenticationserver.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleAuthenticationRequest {
    private String token;
}
