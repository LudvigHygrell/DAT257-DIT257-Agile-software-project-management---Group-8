package com.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.database.adapters.CommentsAdapter;
import com.backend.database.entities.Comment;
import com.backend.database.filtering.FilteredQuery;
import com.backend.database.filtering.JsonToFilterConverter;
import com.backend.jwt.user.UserUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * The CommentsController class handles comment-related API endpoints such as commenting, blaming comments, and removing comments.
 * @author Ludvighygrell, JaarmaCo
 * @version 1.1
 * @since 2025-09-18
 */
@RestController
@RequestMapping("/api/comments")
public class CommentsController {

    @Autowired
    private CommentsAdapter commentsAdapter;

    @PersistenceContext
    private EntityManager entityManager;

    private final JsonNodeFactory jb = JsonNodeFactory.instance;

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody JsonNode json) {
        if (!json.has("comment")) {
            return ResponseEntity.badRequest().body("Missing comment text");
        }
        if (!json.has("charity")) {
            return ResponseEntity.badRequest().body("Missing charity");
        }
        JsonNode comment = json.get("comment");
        String charity = json.get("charity").asText();

        if (commentsAdapter.add(comment, UserUtil.getUsername(), charity)) {
            return ResponseEntity.ok("Comment successfully added");
        }
        return ResponseEntity.status(500).body("Error posting comment");
    }

    @PostMapping("/list")
    public ResponseEntity<JsonNode> list(@RequestBody JsonNode json) {

        if (!json.isObject())
            return ResponseEntity.badRequest().body(
                jb.objectNode().set("message", jb.textNode("Expected a Json object.")));
        
        List<Comment> results;
        try {
            FilteredQuery<Comment> query = new FilteredQuery<>(entityManager, Comment.class);
            results = JsonToFilterConverter.runQueryFromJson(query, json);
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                .body(jb.objectNode()
                .put("message", "Error fetching results."));
        }
        return ResponseEntity.ok().body(jb.objectNode()
            .put("message", "success")
            .set("value", jb.arrayNode()
                .addAll(results.stream()
                    .map(c -> c.toJson()).toList())));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> remove(@RequestBody JsonNode json) {
        if (!json.has("comment_id")) {
            return ResponseEntity.badRequest().body("Missing comment id");
        }
        if (!json.has("charity")) {
            return ResponseEntity.badRequest().body("Missing charity");
        }
        int comment_id = json.get("comment_id").asInt();
        String charity = json.get("charity").asText();

        if (commentsAdapter.remove(comment_id, charity)) {
            return ResponseEntity.ok("Comment successfully removed");
        }
        return ResponseEntity.status(404).body("Comment could not be found");
    }

    @PostMapping("/blame")
    public ResponseEntity<String> blame(@RequestBody JsonNode json) {
        if (!json.has("comment_id")) {
            return ResponseEntity.badRequest().body("Missing comment id");
        }
        if (!json.has("charity")) {
            return ResponseEntity.badRequest().body("Missing charity");
        }
        if (!json.has("reason")) {
            return ResponseEntity.badRequest().body("Missing reason");
        }
        int comment_id = json.get("comment_id").asInt();
        String charity = json.get("charity").asText();
        String reason = json.get("reason").asText();
 
        if (commentsAdapter.blame(comment_id, charity, UserUtil.getUsername(), reason)) {
            return ResponseEntity.ok("Comment successfully blamed");
        }
        return ResponseEntity.status(500).body("Error producing blame");
    }
}
