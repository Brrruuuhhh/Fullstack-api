package com.jwt.api.controllers;

import com.jwt.api.models.Book;
import com.jwt.api.models.Order;
import com.jwt.api.models.User;
import com.jwt.api.repository.BookRepository;
import com.jwt.api.repository.OrderRepository;
import com.jwt.api.repository.UserRepository;
import com.jwt.api.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"}, maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Получить все заказы", description = "Только для модераторов и администраторов. Возвращает список всех заказов.")
    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Operation(summary = "Получить свои заказы", description = "Доступно всем авторизованным пользователям. Возвращает список заказов текущего пользователя.")
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Order> getUserOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow();
        return orderRepository.findByUser(user);
    }

    @Operation(summary = "Создать заказ для себя", description = "Доступно всем авторизованным пользователям. Создаёт заказ на книгу для текущего пользователя.")
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam Long bookId) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow();
        Book book = bookRepository.findById(bookId).orElseThrow();
        Order order = new Order(user, book);
        return ResponseEntity.ok(orderRepository.save(order));
    }

    @Operation(summary = "Обновить заказ", description = "Только для модераторов и администраторов. Обновляет книгу в заказе по id заказа.")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody java.util.Map<String, Object> body) {
        Long bookId = Long.valueOf(body.get("bookId").toString());
        Order order = orderRepository.findById(id).orElseThrow();
        Book book = bookRepository.findById(bookId).orElseThrow();
        order.setBook(book);
        orderRepository.save(order);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Удалить заказ", description = "Только для модераторов и администраторов. Удаляет заказ по id.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Создать заказ для пользователя", description = "Только для модераторов и администраторов. Создаёт заказ на книгу для выбранного пользователя.")
    @PostMapping("/for-user")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createOrderForUser(@RequestParam Long userId, @RequestParam Long bookId) {
        User user = userRepository.findById(userId).orElseThrow();
        Book book = bookRepository.findById(bookId).orElseThrow();
        Order order = new Order(user, book);
        return ResponseEntity.ok(orderRepository.save(order));
    }
}
