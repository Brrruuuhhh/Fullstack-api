package com.jwt.api.controllers;

import com.jwt.api.models.User;
import com.jwt.api.models.Order;
import com.jwt.api.models.Role;
import com.jwt.api.repository.UserRepository;
import com.jwt.api.repository.OrderRepository;
import com.jwt.api.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUsersWithOrders_returnsList() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setEmail("test@test.com");
        user.setRoles(new HashSet<>());
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(orderRepository.findByUser(user)).thenReturn(Collections.emptyList());
        List<Map<String, Object>> result = userController.getUsersWithOrders();
        assertFalse(result.isEmpty());
    }

    @Test
    void createUser_savesUser() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", "test");
        userMap.put("email", "test@test.com");
        userMap.put("password", "pass");
        userMap.put("roles", List.of("ROLE_USER"));
        Role role = new Role();
        role.setName(com.jwt.api.models.ERole.ROLE_USER);
        when(roleRepository.findByName(com.jwt.api.models.ERole.ROLE_USER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        User user = userController.createUser(userMap);
        assertEquals("test", user.getUsername());
    }
}
