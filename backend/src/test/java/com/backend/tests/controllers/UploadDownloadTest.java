package com.backend.tests.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

import static com.backend.tests.BenesphereTestUtilities.logInAsMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UploadDownloadTest {

    @Autowired
    private MockMvc mockMvc;

    private String jwt;

    private static final String TEST_FILE_NAME = "uploaded.txt";

    private static final String TEST_FILE_CONTENTS = "Testing that a file can be uploaded...";

    private static final String TEST_FILE_MAPPING = "/api/files/private/uploaded.txt";

    @BeforeAll
    public void initialize() {
        jwt = logInAsMock(mockMvc);
    }

    @Test
    @Order(1)
    public void testCanUpload() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", 
            TEST_FILE_NAME, "text/plain", TEST_FILE_CONTENTS.getBytes());
        mockMvc.perform(multipart("/api/files/private")
            .file(file)
            .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isCreated())
        .andReturn()
        .getResponse();
    }

    @Test
    @Order(2)
    public void testCanDownload() throws Exception {
        String contents = mockMvc.perform(get(TEST_FILE_MAPPING)
                .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        assertEquals(contents, TEST_FILE_CONTENTS);
    }

    @Test
    @Order(3)
    public void testCanDelete() throws Exception {
        mockMvc.perform(delete(TEST_FILE_MAPPING)
                .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isOk())
            .andReturn();
        mockMvc.perform(get(TEST_FILE_MAPPING)
            .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isNotFound());
    }
}
