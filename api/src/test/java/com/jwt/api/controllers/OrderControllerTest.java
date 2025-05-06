package com.jwt.api.controllers;

import com.jwt.api.models.Book;
import com.jwt.api.models.Order;
import com.jwt.api.models.User;
import com.jwt.api.repository.BookRepository;
import com.jwt.api.repository.OrderRepository;
import com.jwt.api.repository.UserRepository;
import com.jwt.api.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllOrders_returnsList() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());
        assertNotNull(orderController.getAllOrders());
    }

    @Test
    void getUserOrders_returnsList() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(1L);
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user)).thenReturn(Collections.emptyList());
        assertNotNull(orderController.getUserOrders(userDetails));
    }

    @Test
    void createOrder_createsOrder() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(1L);
        User user = new User();
        Book book = new Book();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(orderRepository.save(any())).thenReturn(new Order(user, book));
        ResponseEntity<?> response = orderController.createOrder(userDetails, 2L);
        assertEquals(200, response.getStatusCodeValue());
    }
}
