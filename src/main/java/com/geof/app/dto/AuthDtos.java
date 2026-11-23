package com.geof.app.dto;
import jakarta.validation.constraints.*;
public class AuthDtos {
  public record LoginRequest(@NotBlank String username, @NotBlank String password){}
  public record RegisterRequest(@NotBlank String username, @Email @NotBlank String email, @Size(min=8) String password){}
  public record AuthResponse(String token, String tokenType, String username, String role, long expiresInMs){}
  public record MeResponse(Long id, String username, String email, String role){}
  public record MessageResponse(String message){}
}
