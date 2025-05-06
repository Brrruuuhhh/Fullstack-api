package com.jwt.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"}, maxAge = 3600, allowCredentials="true")

@RestController
@RequestMapping("/api/test")
public class TestController {
  @Operation(summary = "Публичный доступ", description = "Доступно всем пользователям без авторизации.")
  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @Operation(summary = "Доступ для пользователей", description = "Доступно пользователям с ролями USER, MODERATOR или ADMIN.")
  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public String userAccess() {
    return "User Content.";
  }

  @Operation(summary = "Доступ для модераторов", description = "Доступно только пользователям с ролью MODERATOR.")
  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @Operation(summary = "Доступ для администраторов", description = "Доступно только пользователям с ролью ADMIN.")
  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }
}
