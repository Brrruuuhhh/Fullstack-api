package com.jwt.api.controllers;

import com.jwt.api.models.Book;
import com.jwt.api.repository.BookRepository;
import com.jwt.api.repository.OrderRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"}, maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Operation(summary = "Получить список всех книг", description = "Доступно всем. Возвращает список всех книг.")
    @GetMapping
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Operation(summary = "Создать книгу", description = "Только для модераторов и администраторов. Создаёт новую книгу.")
    @PostMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @Operation(summary = "Обновить книгу", description = "Только для модераторов и администраторов. Обновляет данные книги по id.")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Book bookDetails) {
        return bookRepository.findById(id).map(book -> {
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setPrice(bookDetails.getPrice());
            return ResponseEntity.ok(bookRepository.save(book));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Удалить книгу", description = "Только для модераторов и администраторов. Удаляет книгу и связанные с ней заказы по id.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return bookRepository.findById(id).map(book -> {
            List<com.jwt.api.models.Order> orders = orderRepository.findAll();
            orders.stream().filter(o -> o.getBook() != null && o.getBook().getId().equals(id))
                .forEach(orderRepository::delete);
            bookRepository.delete(book);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
