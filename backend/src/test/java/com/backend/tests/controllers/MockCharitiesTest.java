package com.backend.tests.controllers;

import com.backend.tests.ResourceLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.glassfish.jaxb.core.v2.TODO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class MockCharitiesTest {
    @Autowired
    private MockMvc mockMvc;
    String jwtToken;

    @BeforeEach
    public void setUp() throws Exception {

        //TODO We need to be able to create a charity for the database to run tests on

        //Register a user
        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ResourceLoader.loadJson("register-user-mock.json")
                                .toPrettyString()))
                .andExpect(status().isOk());

        //Log in as a user
        MvcResult logInResult = mockMvc.perform(get("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ResourceLoader.loadJson("login-user-mock.json")
                                .toPrettyString()))
                .andExpect(status().isOk())
                .andReturn();

        //Store the authorization token
        jwtToken = new ObjectMapper()
                .readTree(logInResult.getResponse()
                        .getContentAsString())
                .get("token").asText();

    }
    @Test
    public void testVoteSuccess() throws Exception {
        mockMvc.perform(post("/api/charities/vote")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ResourceLoader.loadJson(("vote-success-mock.json")).toPrettyString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Vote posted successfully"));
    }
    @Test
    public void testVoteMissingCharityField() throws Exception {
        mockMvc.perform(post("/api/charities/vote")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ResourceLoader.loadJson(("vote-missing-charity-mock.json")).toPrettyString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing charity"));
    }
    @Test
    public void testVoteMissingUpField() throws Exception {
        mockMvc.perform(post("/api/charities/vote")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ResourceLoader.loadJson(("vote-missing-up-mock.json")).toPrettyString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing up field"));
    }

    /**
     * Test to see if we get an error when trying to vote on a non-existent charity
     */
    @Test
    public void testVoteInvalidCharity() throws Exception {

    }

}
