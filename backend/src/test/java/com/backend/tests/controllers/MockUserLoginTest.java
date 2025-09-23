package com.backend.tests.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.backend.tests.ResourceLoader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests basic user login functionality.
 * <p>
 * Prerequisites:
 *  1. There exists a table named Users(username, email, userpassword) in the database.
 *  2. The user MockUser99 is not registered.
 *  3. Security permissions are set up to allow /api/users/create without authentication.
 */
@SpringBootTest(webEnvironment=WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class MockUserLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void registerLoginDeleteShouldSucceed() throws Exception {

        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ResourceLoader.loadJson("register-user-mock.json")
                            .toPrettyString()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ResourceLoader.loadJson("login-user-mock.json")
                            .toPrettyString()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/users/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ResourceLoader.loadJson("delete-user-mock.json")
                            .toPrettyString()))
                .andExpect(status().isOk());
    }
}
