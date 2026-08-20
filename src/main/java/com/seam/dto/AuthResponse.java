package com.seam.dto;

public class AuthResponse {
  private final String userId;
  private final String name;
  private final String email;
  private final String role;
  private final String accessToken;
  private final String refreshToken;

  public AuthResponse(String userId, String name, String email, String role,
      String accessToken, String refreshToken) {
    this.userId = userId;
    this.name = name;
    this.email = email;
    this.role = role;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public String getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getRole() {
    return role;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }
}