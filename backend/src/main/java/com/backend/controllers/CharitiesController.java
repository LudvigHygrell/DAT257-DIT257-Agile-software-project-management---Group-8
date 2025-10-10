package com.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.database.adapters.CharitiesAdapter;
import com.backend.database.entities.Charity;
import com.backend.database.entities.CharityData;
import com.backend.database.filtering.FilteredQuery;
import com.backend.database.filtering.JsonToFilterConverter;
import com.backend.database.repositories.CharityDataRepository;
import com.backend.jwt.user.UserUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RestController
@RequestMapping("/api/charities")
public class CharitiesController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CharitiesAdapter charitiesAdapter;

    @Autowired
    private CharityDataRepository charityData;

    private final JsonNodeFactory jb = JsonNodeFactory.instance;

    @PostMapping("/list")
    public ResponseEntity<JsonNode> list(@RequestBody JsonNode json) {

        if (!json.isObject())
            return ResponseEntity.badRequest().body(
                jb.objectNode().set("message", jb.textNode("Expected a Json object.")));
        
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
    public ResponseEntity<JsonNode> get(@RequestParam(defaultValue = "", name = "charity") String orgId) {

        try {
            // Try to get charity data directly from charityData repository
            CharityData charityDataEntity = charityData.findById(orgId)
                .orElseThrow(() -> new Exception("Charity not found"));

            // If user is authenticated, add search entry
            if (UserUtil.isAuthenticated()) {
                try {
                    Charity charity = charitiesAdapter.get(orgId);
                    charitiesAdapter.addSearchEntry(charity);
                } catch (Exception e) {
                    // Charity doesn't exist in charities table yet, create it
                    charitiesAdapter.addSearchEntry(new Charity(orgId));
                }
            }

            return ResponseEntity.ok()
                .body(jb.objectNode()
                    .put("message", "success")
                    .set("value", charityDataEntity.toJson()));
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                .body(jb.objectNode().put("message", "Error fetching charity: " + ex.getMessage()));
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
