package com.jwt.api.repository;

import com.jwt.api.models.Order;
import com.jwt.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
