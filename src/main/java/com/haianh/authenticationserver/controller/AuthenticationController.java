package com.haianh.authenticationserver.controller;

import com.haianh.authenticationserver.model.AuthenticationRequest;
import com.haianh.authenticationserver.model.GoogleAuthenticationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/default")
public interface AuthenticationController {

    @PostMapping
    ResponseEntity<?> UsernamePasswordAuthentication(@RequestBody AuthenticationRequest authenticationRequest,
                                                     HttpServletResponse response,
                                                     HttpServletRequest request);

    @PostMapping(value = {"/google", "/google/{role}"})
    public ResponseEntity<?> googleAuthentication(@RequestBody GoogleAuthenticationRequest authenticationRequest,
                                                  @PathVariable(value = "role", required = false) String role,
                                                  HttpServletResponse response,
                                                  HttpServletRequest request);

}
