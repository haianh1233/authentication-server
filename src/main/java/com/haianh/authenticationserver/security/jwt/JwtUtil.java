package com.haianh.authenticationserver.security.jwt;

import com.haianh.authenticationserver.security.permission.ApplicationUserRole;
import com.haianh.authenticationserver.security.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.haianh.authenticationserver.security.permission.ApplicationUserRole.LECTURER;
import static com.haianh.authenticationserver.security.permission.ConfigVariable.*;

@Service
public class JwtUtil {
    private final JwtConfig jwtConfig;

    @Autowired
    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public Boolean isEnabled(String token) {
        return (Boolean) extractAllClaims(token).get(IS_USER_ENABLED.getValue());
    }

    public Boolean isAccountNonLocked(String token) {
        return (Boolean) extractAllClaims(token).get(IS_USER_NON_LOCKED.getValue());
    }

    public List<GrantedAuthority> authorities(String token) {
        return (List<GrantedAuthority>) extractAllClaims(token).get(USER_AUTHORITIES.getValue());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateAccessToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID.getValue(), userDetails.getId());
        claims.put(IS_USER_NON_LOCKED.getValue(), userDetails.isAccountNonLocked());
        claims.put(IS_USER_ENABLED.getValue(), userDetails.isEnabled());
        claims.put(USER_ROLE.getValue(), "ROLE_" + userDetails.getRole());
        return jwtConfig.getTokenPrefix() + " " + createToken(claims, userDetails.getUsername(), jwtConfig.getAccessTokenExpirationAfter());
    }

    public String generateRefreshToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ROLE.getValue(), "ROLE_" + userDetails.getRole()); //add user role into refresh
        claims.put(USER_ID.getValue(), userDetails.getId());
        return jwtConfig.getTokenPrefix() + " " + createToken(claims, userDetails.getUsername(), jwtConfig.getRefreshTokenExpirationAfter());
    }

    private String createToken(Map<String, Object> claims, String subject,Long extra) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ extra))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return  (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

}
