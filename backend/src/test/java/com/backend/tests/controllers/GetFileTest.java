package com.backend.tests.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class GetFileTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCanGetHelloTextFile() throws Exception {

        String contents = mockMvc.perform(get("/api/files/public/hello.txt"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse().getContentAsString();

        assertEquals(contents, "Hello, world!");
    }
}
