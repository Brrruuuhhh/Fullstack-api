package com.jwt.api.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void injectedRepositoryIsNotNull() {
        assertNotNull(orderRepository);
    }
}
