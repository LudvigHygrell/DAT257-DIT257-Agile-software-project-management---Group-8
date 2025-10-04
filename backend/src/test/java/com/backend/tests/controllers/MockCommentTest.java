package com.backend.tests.controllers;

import com.backend.tests.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc

public class MockCommentTest {
    @Autowired
    private MockMvc mockMvc;
    private String createdCommentId;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a mock comment before each test
        MvcResult result = mockMvc.perform(post("/api/comments/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ResourceLoader.loadJson("add-comment-mock.json").toPrettyString()))
                .andExpect(status().isOk())
                .andReturn();
    }
    @Test
    public void addCommentTest() throws Exception {
        mockMvc.perform(post("/api/comments/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ResourceLoader.loadJson("add-comment-mock.json")
                                .toPrettyString()))
                .andExpect(status().isOk());

    }
    @Test
    public void removeCommentTest() throws Exception {
        mockMvc.perform(delete("/api/comments/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ResourceLoader.loadJson("remove-comment-mock.json")
                                .toPrettyString()))
                .andExpect(status().isOk());
    }

    @Test
    public void blameCommentTest() throws Exception {
        mockMvc.perform(post("/api/comments/blame")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ResourceLoader.loadJson("blame-comment-mock.json")
                                .toPrettyString()))
                .andExpect(status().isOk());
    }

}
