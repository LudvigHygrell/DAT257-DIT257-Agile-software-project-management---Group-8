package com.backend.tests;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assumptions.abort;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Helper methods for running tests against the benesphere server.
 */
public class BenesphereTestUtilities {

    /**
     * Logs in as the mock user (a.k.a The Mockingjay).
     * <p>
     * Will abort the test on failure.
     * @param mockMvc MVC for the test.
     * @return A JWT for the "mockingjay" user
     */
    public static String logInAsMock(MockMvc mockMvc) {
        try {
            MvcResult result = mockMvc.perform(
                get("/api/debug/mock/login"))
                .andExpect(status().isOk())
                .andReturn();
            return new ObjectMapper().readTree(result.getResponse().getContentAsString())
                .get("token").asText();
        } catch (Exception ex) {
            abort("Failed to log in as the mock user. Reason: " + ex.getMessage());
            throw new AssertionError("Unreachable, abort() should not return.");
        }
    }

    /**
     * Gets the organization id of a random charity in the database.
     * @param mockMvc MVC for the test.
     * @return The organization id of a random charity.
     */
    public static String getRandomCharity(MockMvc mockMvc) {
        try {
            return mockMvc.perform(get("/api/debug/charities/get_random"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        } catch (Exception ex) {
            abort("Failed to get a charity. Reason: " + ex.getMessage());
            throw new AssertionError("Unreachable, abort() should not return.");
        }
    }

    /**
     * Gets the last comment made in a given charity.
     * @param mockMvc MVC for the test.
     * @param charity Charity that was commented on.
     * @return The id of the latest comment.
     */
    public static int getLatestComment(MockMvc mockMvc, String charity) {
        try {
            return Integer.parseInt(mockMvc.perform(get("/api/debug/comments/{}/latest", charity))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
        } catch (Exception ex) {
            abort("Failed to get the latest comment. Reason: " + ex.getMessage());
            throw new AssertionError("Unreachable, abort() should not return.");
        }
    }
}
