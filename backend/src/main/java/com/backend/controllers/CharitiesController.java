package com.backend.controllers;

import com.backend.database.adapters.CharitiesAdapter;
import com.backend.database.entities.Charity;
import com.backend.database.filtering.FilteredQuery;
import com.backend.database.filtering.JsonToFilterConverter;
import com.backend.database.filtering.Limits;
import com.backend.database.filtering.Ordering;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        
        Ordering order;
        Limits limits;
        try {
            order = json.has("sorting") ? Ordering.fromJson(json.get("sorting")) : Ordering.NONE;
            
            int start = 0;
            int maxResults = Integer.MAX_VALUE;

            if (json.has("first"))
                start = json.get("first").asInt();
            if (json.has("max_count"))
                maxResults = json.get("max_count").asInt();

            limits = new Limits(start, maxResults);

        } catch (Exception ex) {
            return ResponseEntity.status(500)
                .body(jb.objectNode()
                .put("message", "Error in Json formatting."));
        }

        List<Charity> results;
        try {
            FilteredQuery<Charity> query = new FilteredQuery<>(entityManager, Charity.class);
            if (json.has("filters")) {
                results = query.runQuery(
                    JsonToFilterConverter.filterFromJson(
                        query.getFilterBuilder(), json.get("filters")));
            } else {
                results = query.runQuery(order, limits);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                .body(jb.objectNode()
                .put("message", "Error fetching results."));
        }
        return ResponseEntity.ok().body(jb.arrayNode()
            .addAll(results.stream()
                .map(c -> c.toJson()).toList()));
    }

    @GetMapping("/get")
    public ResponseEntity<JsonNode> get(@RequestBody JsonNode json) {
        if (!json.has("identity")) {
            return ResponseEntity.badRequest().body(
                jb.objectNode().put("message", "Missing Org ID"));
        }
        try {
            return ResponseEntity.ok()
                .body(charitiesAdapter.get(json.get("identity").asText()).toJson());
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
