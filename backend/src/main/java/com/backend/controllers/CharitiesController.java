package com.backend.controllers;

import com.backend.database.CharitiesAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/charities")
public class CharitiesController {

    private CharitiesAdapter charitiesAdapter;

    /**
     *
     * @param json
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<String> list(@RequestBody JsonNode json) throws JsonProcessingException {
        if(!json.has("filters")) {
            return ResponseEntity.badRequest().body("Missing filters");
        }
        if(!json.has("order_by")) {
            return ResponseEntity.badRequest().body("Missing sorting order");
        }
        //TODO tags not implemented in database yet
        String order_by = json.get("order_by").asText();
        return ResponseEntity.badRequest().body("Not implemented");
    }
    /**
     *
     * @param json
     * @return ResponseEntity
     */
    @GetMapping
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

    /**
     *
     * @param json
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<String> vote(@RequestBody JsonNode json) {
        if(!json.has("charity")) {
            return ResponseEntity.badRequest().body("Missing charity");
        }
        if (!json.has("up")) {
            return ResponseEntity.badRequest().body("Missing vote");
        }
        String charity = json.get("charity").asText();
        Boolean up = json.get("up").asBoolean();
        if(charitiesAdapter.vote(charity, up)) {
            return ResponseEntity.ok().body("Vote posted successfully");
        }
        return ResponseEntity.status(500).body("Error posting vote"); //TODO Implement CharitiesAdapter
    }

    /**
     *
     * @param json
     * @return ResponseEntity
     */

    @PutMapping
    public ResponseEntity<String> edit_vote(@RequestBody JsonNode json) {
        if (!json.has("charity")) {
            return ResponseEntity.badRequest().body("Missing charity");
        }
        if (!json.has("up")) {
            return ResponseEntity.badRequest().body("Missing vote");
        }
        String charity = json.get("charity").asText();
        Boolean up = json.get("up").asBoolean();
        if (charitiesAdapter.edit_vote(charity, up)) {
            return ResponseEntity.ok().body("Vote edited successfully");
        }
        return ResponseEntity.ok().body(""); //TODO
    }
    /**
     *
     * @param json
     * @return ResponseEntity
     */

    @DeleteMapping
    public ResponseEntity<String> delete_vote(@RequestBody JsonNode json) {
        if (!json.has("charity")) {
            return ResponseEntity.badRequest().body("Missing charity");
        }
        if (!json.has("user")) {
            return ResponseEntity.badRequest().body("Missing user");
        }
        String charity = json.get("charity").asText();
        String user = json.get("user").asText();
        if (charitiesAdapter.delete_vote(charity, user)) {
            return ResponseEntity.ok().body("Vote removed successfully");
        }
        return ResponseEntity.ok().body(""); //TODO
    }
}
