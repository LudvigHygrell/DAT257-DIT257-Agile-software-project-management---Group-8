package com.backend.controllers;

import com.backend.database.adapters.CommentsAdapter;
import com.backend.jwt.user.UserUtil;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;

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

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody JsonNode json){
        if(!json.has("comment")){
            return ResponseEntity.badRequest().body("Missing comment text");
        }
        if(!json.has("charity")){
            return ResponseEntity.badRequest().body("Missing charity");
        }
        JsonNode comment = json.get("comment");
        String charity = json.get("charity").asText();

        if (commentsAdapter.add(comment, UserUtil.getUsername(), charity)){
            return ResponseEntity.ok("Comment successfully added");
        }
        return ResponseEntity.status(500).body("Error posting comment");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> remove(@RequestBody JsonNode json){
        if (!json.has("comment_id")){
            return ResponseEntity.badRequest().body("Missing comment id");
        }
        if (!json.has("charity")){
            return ResponseEntity.badRequest().body("Missing charity");
        }
        int comment_id = json.get("comment_id").asInt();
        String charity = json.get("charity").asText();

        if (commentsAdapter.remove(comment_id, charity)){
            return ResponseEntity.ok("Comment successfully removed");
        }
        return ResponseEntity.status(404).body("Comment could not be found");
    }

    @PostMapping("/blame")
    public  ResponseEntity<String> blame(@RequestBody JsonNode json){
        if (!json.has("comment_id")){
            return ResponseEntity.badRequest().body("Missing comment id");
        }
        if (!json.has("charity")) {
            return ResponseEntity.badRequest().body("Missing charity");
        }
        if (!json.has("reason")){
            return ResponseEntity.badRequest().body("Missing reason");
        }
        int comment_id = json.get("comment_id").asInt();
        String charity = json.get("charity").asText();
        String reason = json.get("reason").asText();
        /*String comment = "";
        if (json.has("comment")){  //Check if the blame contains a "comment" type
            comment = json.get("comment").asText();
        }*/
        if(commentsAdapter.blame(comment_id, charity, UserUtil.getUsername(), reason)){
            return ResponseEntity.ok("Comment successfully blamed");
        }

        return ResponseEntity.status(500).body("Error producing blame");
    }
}
