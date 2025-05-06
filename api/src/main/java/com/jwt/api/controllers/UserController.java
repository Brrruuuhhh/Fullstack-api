package com.jwt.api.controllers;

import com.jwt.api.models.Order;
import com.jwt.api.models.User;
import com.jwt.api.repository.OrderRepository;
import com.jwt.api.repository.RoleRepository;
import com.jwt.api.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users-with-orders")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"}, maxAge = 3600, allowCredentials="true")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Получить всех пользователей с заказами", description = "Только для модераторов и администраторов. Возвращает список пользователей с их заказами и ролями.")
    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Map<String, Object>> getUsersWithOrders() {
        List<User> users = userRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("email", user.getEmail());
            // Добавляем роли пользователя
            List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
            userMap.put("roles", roles);
            List<Order> orders = orderRepository.findByUser(user);
            userMap.put("orders", orders);
            result.add(userMap);
        }
        return result;
    }

    @Operation(summary = "Создать пользователя", description = "Только для администраторов. Создаёт нового пользователя.")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User createUser(@RequestBody Map<String, Object> userMap) {
        User user = new User();
        user.setUsername((String) userMap.get("username"));
        user.setEmail((String) userMap.get("email"));
        String rawPassword = (String) userMap.get("password");
        user.setPassword(passwordEncoder.encode(rawPassword));
        List<String> rolesStr = (List<String>) userMap.get("roles");
        Set<com.jwt.api.models.Role> roles = new HashSet<>();
        if (rolesStr != null) {
            for (String r : rolesStr) {
                com.jwt.api.models.Role role = roleRepository.findByName(com.jwt.api.models.ERole.valueOf(r)).orElse(null);
                if (role != null) roles.add(role);
            }
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Operation(summary = "Обновить пользователя", description = "Только для администраторов. Обновляет данные пользователя по id.")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User updateUser(@PathVariable Long id, @RequestBody Map<String, Object> userMap) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername((String) userMap.get("username"));
        user.setEmail((String) userMap.get("email"));
        if (userMap.get("password") != null && !((String)userMap.get("password")).isEmpty()) {
            String rawPassword = (String) userMap.get("password");
            user.setPassword(passwordEncoder.encode(rawPassword));
        }
        List<String> rolesStr = (List<String>) userMap.get("roles");
        Set<com.jwt.api.models.Role> roles = new HashSet<>();
        if (rolesStr != null) {
            for (String r : rolesStr) {
                com.jwt.api.models.Role role = roleRepository.findByName(com.jwt.api.models.ERole.valueOf(r)).orElse(null);
                if (role != null) roles.add(role);
            }
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Operation(summary = "Удалить пользователя", description = "Только для администраторов. Удаляет пользователя и все его заказы по id.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            orderRepository.deleteAll(orderRepository.findByUser(user));
            userRepository.deleteById(id);
        }
    }
}
