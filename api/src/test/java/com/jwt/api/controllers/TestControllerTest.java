package com.jwt.api.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestControllerTest {
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TestController()).build();

    @Test
    void allAccess_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/test/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void userAccess_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"MODERATOR"})
    void moderatorAccess_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/test/mod"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void adminAccess_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isOk());
    }
}
