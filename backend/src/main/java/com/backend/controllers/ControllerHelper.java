package com.backend.controllers;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerMapping;

import com.backend.interfaces.ThrowingSupplier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpServletRequest;

public abstract class ControllerHelper {

    public static String getRemainingPath(HttpServletRequest request) {
                
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        AntPathMatcher apm = new AntPathMatcher();
        String remainingPath = apm.extractPathWithinPattern(bestMatchPattern, path);

        return remainingPath;
    }

    public static String sanitizePathSuffix(String path) {
        try {
            path = URLDecoder.decode(path, StandardCharsets.UTF_8.name())
                .replace("..", "/")
                .replace("\\", "/")
                .replace("~", "/")
                .replace("$", "/");
            
            while (path.contains("//"))
                path = path.replace("//", "/");

            return path;
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> ResponseEntity<T> orElseResponse(ThrowingSupplier<T> attempt, T valueElse) {
        try {
            return ResponseEntity.ok().body(attempt.get());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(valueElse);
        }
    }
    
    public static <T> ResponseEntity<T> orElseResponse(ThrowingSupplier<T> attempt, int code, T valueElse) {
        try {
            return ResponseEntity.ok().body(attempt.get());
        } catch (Exception e) {
            return ResponseEntity.status(code).body(valueElse);
        }
    }

    public static <T> CompletableFuture<ResponseEntity<T>> orElseFutureResponse(ThrowingSupplier<T> attempt, T valueElse) {
        try {
            ResponseEntity<T> response = ResponseEntity.ok().body(attempt.get());
            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                ResponseEntity.status(500).body(valueElse)
            );
        }
    }

    public static <T> CompletableFuture<ResponseEntity<T>> orElseFutureResponse(ThrowingSupplier<T> attempt, int code, T valueElse) {
        try {
            ResponseEntity<T> response = ResponseEntity.ok().body(attempt.get());
            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                ResponseEntity.status(code).body(valueElse)
            );
        }
    }

    /**
     * Extracts the paramaterized Json from an HTTP get base64url string.
     * 
     * @param query Query string to extract json from.
     * @return An optional containing the json if decoding was successful, Optional.empty if not successful.
     */
    public static Optional<JsonNode> getJsonArguments(String query) {
        try {
            return Optional.of(new ObjectMapper().readTree(
                new String(Base64.getUrlDecoder().decode(query.getBytes()))));
        } catch (JsonProcessingException ex) {
            return Optional.empty();
        } catch (Throwable ex) {
            return Optional.empty();
        }
    }

    /**
     * Convert an object that has a defined toJson() method to json.
     * @param value Value to convert to json.
     * @return Optional containing the json object, or Optional.empty if the conversion failed. 
     */
    public static Optional<JsonNode> valueToJson(Object value) {
        try {
            Method method = value.getClass().getMethod("toJson");
            return Optional.of((JsonNode)method.invoke(value));
        } catch (IllegalAccessException | NoSuchMethodException 
            | SecurityException | InvocationTargetException ex) {
            return Optional.empty();
        }
    }

    /**
     * Gets the name of the SQL column that contains the name of the associated user.
     * @param clazz Class to extract the column from.
     * @return Optional containing the column name, or Optional.empty if the class was invalid for that operation.
     */
    public static Optional<String> getUserColumnName(Class<?> clazz) {
        try {
            return Optional.of((String)clazz.getField("USER_COLUMN_NAME").get(null));
        } catch (IllegalAccessException | IllegalArgumentException 
            | NoSuchFieldException | SecurityException ex) {
            return Optional.empty();
        }
    }

    /**
     * Gets a json object that displays a message.
     * @param message Message to display.
     * @return { "message": "<the-message>" }
     */
    public static ObjectNode messageJson(String message) {
        return JsonNodeFactory.instance.objectNode()
            .put("message", message);
    }

    /**
     * Gets a json object that displays a message and wraps it in a response entity.
     * @param code Status code of the response.
     * @param message Message to display.
     * @return ResponseEntity.status(<the-code>).body({ "message": "<the-message>" })
     */
    public static ResponseEntity<JsonNode> messageJsonResponse(int code, String message) {
        return ResponseEntity.status(code).body(messageJson(message));
    }

    /**
     * Gets a json object, wrapped in a response entity, that contains a message and a value result.
     * @param message Message to set.
     * @param value Value to set.
     * @return ResponseEntity.ok().body({ "message": "<the-message>", "value": <the-value> })
     */
    public static ResponseEntity<JsonNode> valueJsonResponse(String message, JsonNode value) {
        return ResponseEntity.ok().body(
            messageJson(message).set("value", value));
    }}