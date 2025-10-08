package com.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.database.entities.Comment;
import com.backend.database.entities.CommentBlame;
import com.backend.database.entities.GetMappedEntity;
import com.backend.database.entities.SearchedCharity;
import com.backend.database.filtering.FilteredQuery;
import com.backend.database.filtering.JsonToFilterConverter;
import com.backend.jwt.user.UserUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Controller for managing the activities of a user. Activities are records of actions that the user
 * has made on the site (I.e: charity searches, comments, blames, etc.)
 */
@RestController
@RequestMapping("/api/users/get_activity")
public class UserActivityController {

    @PersistenceContext
    private EntityManager entityManager;

    private final JsonNodeFactory factory = JsonNodeFactory.instance;

    private <T extends GetMappedEntity> ResponseEntity<JsonNode> applyQuery(String userQuery, Class<T> clazz) {

        String userField = ControllerHelper.getUserColumnName(clazz).orElseThrow(); // Should not throw, as long as you use it carefully

        JsonNode json;
        try {
            json = ControllerHelper.getJsonArguments(userQuery).orElseThrow();
        } catch (Exception ex) {
            return ControllerHelper.messageJsonResponse(422, "Expected query as base64url encoded JSON");
        }

        FilteredQuery<T> query = new FilteredQuery<>(entityManager, clazz);
        List<T> results = JsonToFilterConverter.runQueryFromJson(query, json.get("query"), 
            b -> b.equalTo(userField, UserUtil.getUsername()));

        List<JsonNode> jsonValues = results.stream().map(T::toJson).toList();
        return ControllerHelper.valueJsonResponse("success", factory.arrayNode().addAll(jsonValues));
    }

    /**
     * Lists comments made by this user.
     */
    @GetMapping("comments")
    public ResponseEntity<JsonNode> comments(@RequestParam(defaultValue = "", name = "query") String userQuery) {
        return applyQuery(userQuery, Comment.class);
    }

    /**
     * Lists comment blamed by user.
     */
    @GetMapping("comment_blame")
    public ResponseEntity<JsonNode> commentBlame(@RequestParam(defaultValue = "", name = "query") String userQuery) {
        return applyQuery(userQuery, CommentBlame.class);
    }

    /**
     * Lists charities searched by user. 
     */
    @GetMapping("searched_charities")
    public ResponseEntity<JsonNode> searchedCharities(@RequestParam(defaultValue = "", name = "query") String userQuery) {
        return applyQuery(userQuery, SearchedCharity.class);
    }
}
