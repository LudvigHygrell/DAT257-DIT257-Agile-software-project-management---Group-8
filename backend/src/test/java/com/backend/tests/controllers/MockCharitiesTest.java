package com.backend.tests.controllers;

import com.backend.tests.ResourceLoader;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.backend.tests.BenesphereTestUtilities.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class MockCharitiesTest {

    @Autowired
    private MockMvc mockMvc;
    
    String jwtToken;

    String charity;

    @BeforeEach
    public void setUp() throws Exception {
        jwtToken = logInAsMock(mockMvc);
        charity = getRandomCharity(mockMvc);
    }

    @Test
    public void testVoteSuccess() throws Exception {
        mockMvc.perform(post("/api/charities/vote")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{ \"charity\": \"%s\", \"up\": true }", charity)))
                .andExpect(status().isOk());
    }

    @Test
    public void testVoteMissingCharityField() throws Exception {
        mockMvc.perform(post("/api/charities/vote")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ResourceLoader.loadJson(("vote-missing-charity-mock.json")).toPrettyString()))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testVoteMissingUpField() throws Exception {
        mockMvc.perform(post("/api/charities/vote")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ResourceLoader.loadJson(("vote-missing-up-mock.json")).toPrettyString()))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test to see if we get an error when trying to vote on a non-existent charity
     */
    @Test
    public void testVoteInvalidCharity() throws Exception {

    }

}
