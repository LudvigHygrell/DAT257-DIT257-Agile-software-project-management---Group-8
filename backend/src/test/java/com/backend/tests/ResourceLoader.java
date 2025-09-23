package com.backend.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class for loading files from the resources directory.
 * @author JaarmaCo
 * @since 2025-09-23
 * @version 1.0
 */
public abstract class ResourceLoader {

    private ResourceLoader() {}

    /**
     * Load a Json object from resources.
     * @param path Path to the json file relative to resources/.
     * @return The loaded json element.
     * @throws IOException I/O error occurred.
     */
    public static JsonNode loadJson(String path) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(path)) {
            if (null == is)
                throw new RuntimeException("Resource not found.");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(is);
        }
    }
}
