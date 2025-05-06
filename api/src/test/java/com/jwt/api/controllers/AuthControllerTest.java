package com.jwt.api.controllers;

import com.jwt.api.models.ERole;
import com.jwt.api.models.Role;
import com.jwt.api.models.User;
import com.jwt.api.payload.request.LoginRequest;
import com.jwt.api.payload.request.SignupRequest;
import com.jwt.api.payload.response.JwtResponse;
import com.jwt.api.payload.response.MessageResponse;
import com.jwt.api.repository.RoleRepository;
import com.jwt.api.repository.UserRepository;
import com.jwt.api.security.jwt.JwtUtils;
import com.jwt.api.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder encoder;
    @Mock
    JwtUtils jwtUtils;
    @InjectMocks
    AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticateUser_success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("pass");
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("token");
        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getUsername()).thenReturn("user");
        when(userDetails.getEmail()).thenReturn("user@test.com");
        when(userDetails.getAuthorities()).thenReturn((Collection) Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof JwtResponse);
    }

    @Test
    void registerUser_usernameTaken() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("user");
        signupRequest.setEmail("user@test.com");
        signupRequest.setPassword("pass");
        when(userRepository.existsByUsername("user")).thenReturn(true);
        ResponseEntity<?> response = authController.registerUser(signupRequest);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof MessageResponse);
    }

    @Test
    void registerUser_emailTaken() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("user");
        signupRequest.setEmail("user@test.com");
        signupRequest.setPassword("pass");
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("user@test.com")).thenReturn(true);
        ResponseEntity<?> response = authController.registerUser(signupRequest);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof MessageResponse);
    }
}
