package com.backend.tests.controllers;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backend.ApplicationProperties;
import com.backend.email.EmailConfirmations;
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

  @Autowired
  private ApplicationProperties properties;

  @Autowired
  private ObjectMapper objectMapper;

  /**
   * Validate that we can register, log in and delete a user.
   */
  @Test
  public void registerLoginDeleteShouldSucceed() throws Exception {

    // Create user.
    //
    MvcResult createResult = mockMvc.perform(post("/api/users/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ResourceLoader.loadJson("register-user-mock.json")
            .toPrettyString()))
        .andExpect(request().asyncStarted())
        .andReturn();

    // Manual confirmation override
    //
    if (properties.getEmailProperties().isVerified()) {
        StringBuilder confirmation = new StringBuilder("/api/email/confirm/");
        confirmation
            .append(new String(Base64.getUrlEncoder().encode("mock.user99@localhost".getBytes())))
            .append("/")
            .append(EmailConfirmations.getInstance().getCodeOfPending("mock.user99@localhost"));

        mockMvc.perform(post(confirmation.toString()))
            .andExpect(status().isOk());
    }

    {
        Object result = createResult.getAsyncResult(5000);
        System.err.println("Result " + result.toString());
    }

    // Log in with new user (and keep auth info).
    //
    MvcResult result = mockMvc.perform(get("/api/users/login")
        .param("query", ResourceLoader.loadBase64urlJson("login-user-mock.json")))
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
    @Test
    public void testLoginWithValidUsernameAndPassword() throws Exception {
        // Create login JSON
        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", "testuser");
        loginData.put("password", "testpassword");

        // Encode to Base64
        String jsonString = objectMapper.writeValueAsString(loginData);
        String base64Query = Base64.getUrlEncoder().encodeToString(jsonString.getBytes());

        // Perform GET request to /login?query=<base64>
        mockMvc.perform(get("/login")
                        .param("query", base64Query)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
