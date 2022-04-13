package com.anffercastillo.filters;

import com.anffercastillo.utils.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

  public static final String BEARER = "Bearer ";

  private JwtUtils jwtUtils;
  private UserDetailsService userDetailsService;

  public AuthorizationFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
    this.jwtUtils = jwtUtils;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (request.getServletPath().equals("/login")) {
      filterChain.doFilter(request, response);
    } else {
      if (request.getHeader(HttpHeaders.AUTHORIZATION) != null
          && request.getHeader(HttpHeaders.AUTHORIZATION).startsWith(BEARER)) {
        var token = request.getHeader(HttpHeaders.AUTHORIZATION).split(BEARER)[1];

        var decodedToken =
            jwtUtils
                .getDecodedToken(token)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.UNAUTHORIZED));

        var username = decodedToken.getSubject();
        var userDetails = userDetailsService.loadUserByUsername(username);
        var authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
      } else {
        filterChain.doFilter(request, response);
      }
    }
  }
}
