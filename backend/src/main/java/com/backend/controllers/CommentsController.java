package com.backend.controllers;

import com.backend.database.CommentsAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/add")
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

    /**
     *
     * @param json
     * @return
     */
    @DeleteMapping("/remove")
    public ResponseEntity<String> remove (@RequestBody JsonNode json){
        if (!json.has("comment_id")){
            return ResponseEntity.badRequest().body("Missing comment id");
        }
        if (!json.has("charity")){
            return ResponseEntity.badRequest().body("Missing charity");
        }
        String comment_id = json.get("comment_id").asText();
        String charity = json.get("charity").asText();

        if (CommentsAdapter.remove(comment_id, charity)){
            return ResponseEntity.ok("Comment successfully removed");
        }
        return ResponseEntity.status(404).body("Comment could not be found");
    }

    /**
     *
     * @param json
     * @return
     */
    @PostMapping("/blame")
    public  ResponseEntity<String> blame (@RequestBody JsonNode json){
        if (!json.has("comment_id")){
            return ResponseEntity.badRequest().body("Missing comment id");
        }
        if (!json.has("reason")){
            return ResponseEntity.badRequest().body("Missing reason");
        }
        if ((!json.has("blame_victim"))){
            return ResponseEntity.badRequest().body("Missing blame victim");
        }

        String comment_id = json.get("comment_id").asText();
        String reason = json.get("reason").asText();
        String blame_victim = json.get("blame_victim").asText();
        String comment = "";
        if (json.has("comment")){  //Check if the blame contains a "comment" type
            comment = json.get("comment").asText();
        }
        if(CommentsAdapter.blame(comment_id,reason, comment, blame_victim)){
            return ResponseEntity.ok("Comment successfully blamed");
        }

        return ResponseEntity.status(500).body("Error producing blame");
    }
}
