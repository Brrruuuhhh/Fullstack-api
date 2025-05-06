package com.jwt.api.controllers;

import com.jwt.api.models.Book;
import com.jwt.api.repository.BookRepository;
import com.jwt.api.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_returnsList() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());
        assertNotNull(bookController.getAll());
    }

    @Test
    void create_savesBook() {
        Book book = new Book();
        when(bookRepository.save(book)).thenReturn(book);
        assertEquals(book, bookController.create(book));
    }

    @Test
    void update_existingBook_returnsOk() {
        Book book = new Book();
        book.setTitle("t");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);
        ResponseEntity<?> response = bookController.update(1L, book);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void update_notFound_returnsNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<?> response = bookController.update(1L, new Book());
        assertEquals(404, response.getStatusCodeValue());
    }
}
