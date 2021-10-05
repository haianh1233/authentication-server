package com.haianh.authenticationserver.security.permission;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConfigVariable {
    IS_USER_ENABLED("isEnabled"),
    IS_USER_NON_LOCKED("isAccountNonLocked"),
    USER_AUTHORITIES("authorities"),
    USER_ROLE("role"),
    USER_ID("id");

    private final String value;
}
