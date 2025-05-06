package com.jwt.api.security.services;

import com.jwt.api.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {
    @Test
    void build_returnsUserDetailsImpl() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setEmail("test@test.com");
        user.setPassword("pass");
        assertNotNull(UserDetailsImpl.build(user));
    }

    @Test
    void getUsername_returnsUsername() {
        UserDetailsImpl details = new UserDetailsImpl(1L, "test", "test@test.com", "pass", null);
        assertEquals("test", details.getUsername());
    }
}
