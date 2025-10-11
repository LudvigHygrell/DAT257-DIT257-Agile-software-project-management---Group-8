package com.backend.tests.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backend.tests.ResourceLoader;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests basic user login functionality.
 * <p>
 * Prerequisites:
 * 1. There exists a table named Users(username, email, userpassword) in the
 * database.
 * 2. The user MockUser99 is not registered.
 * 3. Security permissions are set up to allow /api/users/create without
 * authentication.
 */
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
//@Import(MockUserLoginTestConfig.class)
public class MockUserLoginTest {

  @Autowired
  private MockMvc mockMvc;

  /**
   * Validate that we can register, log in and delete a user.
   */
  @Test
  public void registerLoginDeleteShouldSucceed() throws Exception {

    // Create user.
    //
    mockMvc.perform(post("/api/users/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ResourceLoader.loadJson("register-user-mock.json")
            .toPrettyString()))
        .andExpect(status().isOk())
        .andReturn();

    // Log in with new user (and keep auth info).
    //
    MvcResult result = mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ResourceLoader.loadJson("login-user-mock.json")
                .toPrettyString()))
        .andExpect(status().isOk())
        .andReturn();

    String jwtToken = new ObjectMapper()
        .readTree(result.getResponse()
            .getContentAsString())
        .get("token").asText();

    // Delete the new user.
    //
    mockMvc.perform(delete("/api/users/remove")
        .header("Authorization", "Bearer " + jwtToken)
        .contentType(MediaType.APPLICATION_JSON)
        .content(ResourceLoader.loadJson("delete-user-mock.json")
            .toPrettyString()))
        .andExpect(status().isOk());
    }
}
