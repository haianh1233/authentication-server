package com.haianh.authenticationserver.security.user;

import com.haianh.authenticationserver.entity.Admin;
import com.haianh.authenticationserver.entity.Lecturer;
import com.haianh.authenticationserver.entity.Student;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

import static com.haianh.authenticationserver.security.permission.ApplicationUserRole.*;

@Data
@NoArgsConstructor
public class User implements UserDetails {
    private int id;
    private String userName;
    private String name;
    private String password;
    private int status;
    private String role;
    private Set<? extends GrantedAuthority> grantedAuthorities;

    public User(Student student) {
        this.id = student.getId();
        this.userName = student.getEmail();
        this.name = student.getName();
        this.status = student.getStatus();
        this.role = STUDENT.name();
        this.grantedAuthorities =  STUDENT.getGrantedAuthorities();
    }

    public User(Lecturer lecturer) {
        this.id = lecturer.getId();
        this.userName = lecturer.getEmail();
        this.name = lecturer.getName();
        this.role = LECTURER.name();
        this.status = lecturer.getStatus();
        this.grantedAuthorities = LECTURER.getGrantedAuthorities();
    }

    public User(Admin admin) {
        this.id = admin.getId();
        this.userName = admin.getUsername();
        this.password = admin.getPassword();
        this.name = admin.getName();
        this.role = ADMIN.name();
        this.status = 1;
        this.grantedAuthorities = ADMIN.getGrantedAuthorities();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != 2;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status != 0;
    }
}
