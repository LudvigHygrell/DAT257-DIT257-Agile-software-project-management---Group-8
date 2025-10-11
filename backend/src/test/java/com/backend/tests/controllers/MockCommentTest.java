package com.backend.tests.controllers;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.backend.tests.BenesphereTestUtilities.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
public class MockCommentTest {
    
    @Autowired
    private MockMvc mockMvc;

    private String jwtToken;

    private String charity;

    private int comment = -1;

    @BeforeAll
    public void setUp() throws Exception {
        jwtToken = logInAsMock(mockMvc);
        charity = getRandomCharity(mockMvc);
    }

    @Test
    @Order(1)
    public void addCommentTest() throws Exception {
        mockMvc.perform(post("/api/comments/add")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{ \"charity\": \"%s\"," 
                        + " \"comment\": { \"contents\": \"This is a comment.\" } }", charity)))
                .andExpect(status().isOk());
    }
    
    @Test
    @Order(2)
    public void blameCommentTest() throws Exception {
        comment = getLatestComment(mockMvc, charity);
        mockMvc.perform(post("/api/comments/blame")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{ \"charity\": \"%s\", \"comment_id\": %s, \"reason\": \"none\" }", 
                                charity, comment)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void removeCommentTest() throws Exception {
        mockMvc.perform(delete("/api/comments/remove")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{ \"comment_id\": %s, \"charity\": \"%s\" }", 
                                comment, charity)))
                .andExpect(status().isOk());
                
        JsonNode listResponse = new ObjectMapper().readTree(
                mockMvc.perform(post("/api/comments/list")
                                .header("Authorization", "Bearer " + jwtToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format(
                                        "{ \"filters\": [ { \"filter\":" 
                                        + " \"equals\", \"field\": \"commentId\"," 
                                        + " \"value\": %s } ] }", comment)))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString());

        // Assert that the comment did not exist after delete.
        assertEquals(listResponse.get("value").size(), 0);
    }
}
