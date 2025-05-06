package com.jwt.api.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void injectedRepositoryIsNotNull() {
        assertNotNull(roleRepository);
    }
}
