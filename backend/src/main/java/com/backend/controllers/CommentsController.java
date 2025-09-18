package com.backend.controllers;

import com.backend.database.CommentsAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The CommentsController class handles comment-related API endpoints such as commenting, blaming comments, and removing comments.
 * @author Ludvighygrell
 * @version 1.0
 * @since 2025-09-18
 */
@RestController
@RequestMapping("/api/comments")
public class CommentsController {
    /**
     *
     * @param json
     * @return
     */
    @GetMapping("/add")
    public ResponseEntity<String> add (@RequestBody JsonNode json){
        if(!json.has("comment")){
            return ResponseEntity.badRequest().body("Missing comment text");
        }
        if(!json.has("user")){
            return ResponseEntity.badRequest().body("Missing user");
        }
        if(!json.has("charity")){
            return ResponseEntity.badRequest().body("Missing charity");
        }
        String comment = json.get("comment").asText();
        String user = json.get("user").asText();
        String charity = json.get("charity").asText();

        if (CommentsAdapter.add(comment,user,charity)){
            return ResponseEntity.ok("Comment successfully added");
        }
        return ResponseEntity.status(500).body("Error posting comment");

    }
}
