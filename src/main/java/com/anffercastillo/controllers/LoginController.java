package com.anffercastillo.controllers;

import com.anffercastillo.dto.LoginRequest;
import com.anffercastillo.dto.LoginResponse;
import com.anffercastillo.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  private AuthenticationManager authenticationManager;
  private UserDetailsService userDetailsService;
  private JwtUtils jwtUtils;

  public LoginController(
      AuthenticationManager authenticationManager,
      UserDetailsService userDetailsService,
      JwtUtils jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/login")
  public LoginResponse login(@RequestBody LoginRequest loginRequest) {
    var authentication =
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(), loginRequest.getPassword());

    authenticationManager.authenticate(authentication);

    var user = userDetailsService.loadUserByUsername(loginRequest.getUsername());
    var response = new LoginResponse();
    response.setToken(jwtUtils.generateToken(user));

    return response;
  }
}
