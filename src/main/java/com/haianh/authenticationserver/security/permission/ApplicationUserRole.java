package com.haianh.authenticationserver.security.permission;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.haianh.authenticationserver.security.permission.ApplicationUserAuthority.*;


@AllArgsConstructor
@Getter
public enum ApplicationUserRole {
    STUDENT(com.google.common.collect.Sets.newHashSet(
            STUDENT_READ,
            STUDENT_WRITE,
            LECTURER_READ
    )),
    LECTURER(com.google.common.collect.Sets.newHashSet(
            LECTURER_READ,
            LECTURER_WRITE,
            STUDENT_READ
    )),
    ADMIN(Sets.newHashSet(
            STUDENT_READ,
            STUDENT_WRITE,
            LECTURER_READ,
            LECTURER_WRITE,
            LECTURER_READ_ALL,
            STUDENT_READ_ALL
    ));

    private final Set<ApplicationUserAuthority> authorities;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> authorities = getAuthorities().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getAuthority()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
