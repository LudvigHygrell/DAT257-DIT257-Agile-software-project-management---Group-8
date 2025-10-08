package com.backend.controllers;

import com.backend.database.adapters.CharitiesAdapter;
import com.backend.database.entities.Charity;
import com.backend.database.entities.CharityData;
import com.backend.database.filtering.FilteredQuery;
import com.backend.database.filtering.JsonToFilterConverter;
import com.backend.jwt.user.UserUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/charities")
public class CharitiesController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CharitiesAdapter charitiesAdapter;

    private JsonNodeFactory jb = JsonNodeFactory.instance;

    @SuppressWarnings("deprecation")
    @GetMapping("/list")
    public ResponseEntity<JsonNode> list(@RequestBody JsonNode json) {
        if (!json.isObject())
            return ResponseEntity.badRequest().body(
                jb.objectNode().put("message", jb.textNode("Expected a Json object.")));
        
        List<CharityData> results;
        try {
            FilteredQuery<CharityData> query = new FilteredQuery<>(entityManager, CharityData.class);
            results = JsonToFilterConverter.runQueryFromJson(query, json);

            if (UserUtil.isAuthenticated()) {
                charitiesAdapter.addSkimSearchEntries(results.stream().map(c -> new Charity(c.getCharity())).toList());
            }
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                .body(jb.objectNode()
                .put("message", "Error fetching results."));
        }

        JsonNode values = jb.arrayNode()
            .addAll(results.stream()
                .map(c -> c.toJson()).toList());

        JsonNode result = jb.objectNode()
            .put("message", "success")
            .set("value", values);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/get")
    public ResponseEntity<JsonNode> get(@RequestBody JsonNode json) {
        if (!json.has("identity")) {
            return ResponseEntity.badRequest().body(
                jb.objectNode().put("message", "Missing Org ID"));
        }
        try {
            Charity charity = charitiesAdapter.get(json.get("identity").asText());
            charitiesAdapter.addSearchEntry(charity);
            return ResponseEntity.ok()
                .body(jb.objectNode()
                    .put("message", "success")
                    .set("value", charity.toJson()));
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                .body(jb.objectNode().put("message", "Error fetching charity."));
        }
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
