package com.haianh.authenticationserver.security.permission;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApplicationUserAuthority {
    STUDENT_READ_ALL("student:readAll"),
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    LECTURER_READ_ALL("lecturer:readAll"),
    LECTURER_READ("lecturer:read"),
    LECTURER_WRITE("lecturer:write");

    private final String authority;
}
