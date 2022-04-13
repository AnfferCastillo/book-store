package com.anffercastillo.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails, Principal {

  private String username;
  private String password;
  private boolean isBanned;
  private Long id;

  public static CustomUserDetails buildUserDetails(User user) {
    var userDetails = new CustomUserDetails();
    userDetails.setUsername(user.getUsername());
    userDetails.setPassword(user.getPassword());
    userDetails.setBanned(user.isBanned());
    userDetails.setId(user.getId());
    return userDetails;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setBanned(boolean banned) {
    isBanned = banned;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !isBanned;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return !isBanned;
  }

  @Override
  public String getName() {
    return getUsername();
  }
}
