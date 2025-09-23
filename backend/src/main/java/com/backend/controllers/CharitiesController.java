package com.backend.controllers;

import com.backend.database.adapters.CharitiesAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/charities")
public class CharitiesController {

    private CharitiesAdapter charitiesAdapter;

    @GetMapping("/list")
    public ResponseEntity<String> list(@RequestBody JsonNode json) throws JsonProcessingException {
        if(!json.has("filters")) {
            return ResponseEntity.badRequest().body("Missing filters");
        }
        if(!json.has("order_by")) {
            return ResponseEntity.badRequest().body("Missing sorting order");
        }
        //TODO tags not implemented in database yet
        // String order_by = json.get("order_by").asText();
        return ResponseEntity.badRequest().body("Not implemented");
    }

    @GetMapping("/get")
    public ResponseEntity<String> get(@RequestBody JsonNode json) {
        if(!json.has("identity")) {
            return ResponseEntity.badRequest().body("Missing Org ID");
        }
        String identity = json.get("identity").asText();
        if(charitiesAdapter.get(identity)){
            return ResponseEntity.ok("Charity retrieved successfully");
        }
        return ResponseEntity.status(404).body("Charity not found");
    }

    @PostMapping("/vote")
    public ResponseEntity<String> vote(@RequestBody JsonNode json) {
        if (!json.has("charity")) {
            return ResponseEntity.badRequest().body("Missing charity");
        }
        if (!json.has("up")) {
            return ResponseEntity.badRequest().body("Missing up field.");
        }
        String charity = json.get("charity").asText();
        boolean up = json.get("up").asBoolean();
        if (charitiesAdapter.vote(charity, up)) {
            return ResponseEntity.ok().body("Vote posted successfully");
        }
        return ResponseEntity.status(500).body("Error posting vote");
    }

    @PutMapping("/edit_vote")
    public ResponseEntity<String> editVote(@RequestBody JsonNode json) {
        if (!json.has("charity")) {
            return ResponseEntity.badRequest().body("Missing charity field.");
        }
        if (!json.has("up")) {
            return ResponseEntity.badRequest().body("Missing vote value.");
        }
        String charity = json.get("charity").asText();
        boolean up = json.get("up").asBoolean();
        if (!charitiesAdapter.editVote(charity, up))  {
            return ResponseEntity.status(500).body("Failed to edit vote.");
        }
        return ResponseEntity.ok("Vote registered successfully.");
    }

    @DeleteMapping("/remove_vote")
    public ResponseEntity<String> removeVote(@RequestBody JsonNode json) {
        if (!json.has("charity")) {
            return ResponseEntity.badRequest().body("Missing charity");
        }
        String charity = json.get("charity").asText();
        if (charitiesAdapter.deleteVote(charity)) {
            return ResponseEntity.ok().body("Vote edited successfully");
        }
        return ResponseEntity.status(500).body("Error removing like.");
    }
}
