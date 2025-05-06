package com.jwt.api.security.services;

import com.jwt.api.models.User;
import com.jwt.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_returnsUserDetails() {
        User user = new User();
        user.setUsername("test");
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        assertNotNull(userDetailsService.loadUserByUsername("test"));
    }

    @Test
    void loadUserByUsername_notFound_throwsException() {
        when(userRepository.findByUsername("notfound")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userDetailsService.loadUserByUsername("notfound"));
    }
}
