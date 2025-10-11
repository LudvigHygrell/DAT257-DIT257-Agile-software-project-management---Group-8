package com.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.ApplicationProperties;
import com.backend.database.debug.MockUserUtils;
import com.backend.database.entities.Charity;
import com.backend.database.entities.Comment;
import com.backend.database.filtering.FilteredQuery;
import com.backend.database.filtering.Limits;
import com.backend.database.filtering.Ordering;
import com.backend.jwt.JwtUtil;
import com.backend.jwt.user.UserDetail;
import com.backend.jwt.user.UserDetailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Controller for endpoints that are only available during the debug build. 
 */
@RestController
@RequestMapping("/api/debug")
public class DebugController {
    
    @Autowired
    private MockUserUtils mockUserUtils;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ApplicationProperties properties;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Logs in (and registeres if not present) a mock user that can be used to debug endpoint functions.
     * @return Same as /api/users/login
     */
    @GetMapping("/mock/login")
    public ResponseEntity<JsonNode> loginMockUser() {

        if (properties.inRelease()) {
            return ResponseEntity.status(403).body(JsonNodeFactory.instance.objectNode()
                .put("message", "Debug build is disabled"));
        }

        String jwt;
        try {
            mockUserUtils.ensureMockUserRegistered();
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(JsonNodeFactory.instance.objectNode()
                .put("message", "Failed to register mock user."));
        }
        try {
            UserDetail details = (UserDetail)userDetailService.loadUserByUsername(MockUserUtils.MOCK_USER_USERNAME);
            jwt = jwtUtil.generateToken(details);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(500).body(JsonNodeFactory.instance.objectNode()
                .put("message", "Failed to authenticate mock user."));
        }
        return ResponseEntity.ok().body(JsonNodeFactory.instance.objectNode()
            .put("token", jwt));
    }

    /**
     * Gets a random charity from the database.
     * @return The charity org id.
     */
    @GetMapping("/charities/get_random")
    public ResponseEntity<String> getRandomCharity() {
        try {
            FilteredQuery<Charity> query = new FilteredQuery<>(entityManager, Charity.class);
            return ResponseEntity.ok().body(query
                .runQuery(Ordering.NONE, 
                    new Limits(1)).get(0).getOrgID());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("<!INVALID!>");
        }
    }

    @GetMapping("/comments/{charity}/latest")
    public ResponseEntity<Integer> getLatestComment(@PathVariable(name="charity") String charity) {
        try {
            FilteredQuery<Comment> query = new FilteredQuery<>(entityManager, Comment.class);
            return ResponseEntity.ok().body(query
                .runQuery(Ordering.descending("insertTime"), new Limits(1))
                .get(0).getCommentId());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(-1);
        }
    }
}
