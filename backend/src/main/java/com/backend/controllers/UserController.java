package com.backend.controllers;

import com.backend.database.UserRepository;
import com.backend.database.CommentsAdapter;
import com.backend.database.UserAdapter;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.config.ResourceReaderRepositoryPopulatorBeanDefinitionParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * The UserController class handles user-related API endpoints such as login and registration.
 * This class contains methods to process incoming requests, validate input data, and interact
 * with the UserAdapter for database operations.
 * @author LVFK04
 * @version 1.0
 * @since 2025-09-16
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserAdapter userAdapter;
    private CommentsAdapter commentAdapter;

    /**
     * 
     * @param json
     * @return
     */
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody JsonNode json) {
        if (!json.has("username"))
            return ResponseEntity.badRequest().body("Missing username");
        if (!json.has("password"))
            return ResponseEntity.badRequest().body("Missing password");

        String username = json.get("username").asText();
        String password = json.get("password").asText();

        if (userAdapter.login(username, password))
            return ResponseEntity.ok("Login successful");
        
        return ResponseEntity.status(401).body("Invalid username or password");
    }

    /**
     * 
     * @param json
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<String> register(@RequestBody JsonNode json) {

        if (!json.has("username"))
            return ResponseEntity.badRequest().body("Missing username");
        if (!json.has("email"))
            return ResponseEntity.badRequest().body("Missing email");
        if (!json.has("password"))
            return ResponseEntity.badRequest().body("Missing password");

        String username = json.get("username").asText();
        String email = json.get("email").asText();
        String password = json.get("password").asText();

        if (userAdapter.isUsername(username))
            return ResponseEntity.status(409).body("Username already exists");
        
        if (userAdapter.isEmail(email))
            return ResponseEntity.status(409).body("Email already exists");

        try {
            userAdapter.register(username, email, password);
            return ResponseEntity.ok("User registered successfully");
        } catch(Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(500).body("Error registering user");
    }

    @PutMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestBody JsonNode json) {
        if (!json.has("username"))
            return ResponseEntity.badRequest().body("Missing username");
        if (!json.has("old"))
            return ResponseEntity.badRequest().body("Missing old password");
        if (!json.has("new"))
            return ResponseEntity.badRequest().body("Missing new password");

        String username = json.get("username").asText();
        String old_password = json.get("old").asText();
        String new_password = json.get("new").asText();
        if (old_password.equals(new_password))
            return ResponseEntity.badRequest().body("New password must be different from old password");

        if (!userAdapter.login(username, old_password))
            return ResponseEntity.status(401).body("Invalid username or password");
        try {
            userAdapter.changePassword(username, new_password);
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(500).body("Error changing password");
    }


	@PutMapping("/reset_password")
    public ResponseEntity<String> reset_password(@RequestBody JsonNode json ) {
        if(!json.has("email"))
            return ResponseEntity.status(400).body("The email parameter is not set.");
        if(!json.has("new_password"))
            return ResponseEntity.status(400).body("The new_password parameter is not set.");
        if(!json.has("verification_code"))
            return ResponseEntity.status(400).body("The verification_code parameter was not set.");

        // TODO: implement the check for verification code

        String email = json.get("email").asText();
        String username = userAdapter.getUsernameFromEmail(email);
        String new_password = json.get("new_password").asText();
        try {
            userAdapter.changePassword(username, new_password);
            return ResponseEntity.status(200).body("All went fine. You password is now changed!");
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("There was something wrong at the server->database communication.");
        }
    }

    /**
     * 
     * @param json
     * @return
     */
    @PutMapping("/change_email")
    public ResponseEntity<String> change_email(@RequestBody JsonNode json) {
        if(!json.has("username"))
            return ResponseEntity.status(400).body("The username parameter was not set");
        if(!json.has("email"))
            return ResponseEntity.status(400).body("The email parameter was not set.");
        if(!json.has("password"))
            return ResponseEntity.status(400).body("The password parameter was not set.");

        String username = json.get("username").asText();
        String password = json.get("password").asText();
        String email = json.get("email").asText();

        if(userAdapter.isEmail(email))
            return ResponseEntity.status(401).body("The email you set is already a registered email at our site");
        if(!userAdapter.login(username, password))
            return ResponseEntity.status(402).body("The user-credentials provided did not match any account");

        try {
            userAdapter.changeEmail(username, email);
            return ResponseEntity.status(200).body("Your email-address was successfully changed");
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("There was an error at the server - database communication. This is nothing you can do about, but will be fixed in the soon future.");
        }

    }
    /**
     * 
     *      "username" : string,
     *      "type" : ["comments" AND/OR "likes"],
     *      "filters" : string (TODO)
     * @param json
     * @return
     */
    @PostMapping("/get_activity")
    public ResponseEntity<JsonNode> get_activity(@RequestBody JsonNode json) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode ret_node = factory.objectNode();

        if(!json.has("username")) {
            ret_node.put("message", "The username-parameter was not set");
            return ResponseEntity.status(400).body(ret_node);
        }
        if(!json.has("type")){
            ret_node.put("message", "The type-parameter was not set");
            return ResponseEntity.status(400).body(ret_node);
        }

        String username = json.get("username").asText();

        try {
            ObjectNode comments_node = commentAdapter.comments(username);
            ObjectNode likes_node = commentAdapter.likes(username);

            // TODO: implement the filters-functionallity

            ret_node.set("likes", likes_node);
            ret_node.set("comments", comments_node);
            return ResponseEntity.status(200).body(ret_node);
        } catch(Exception e) {
            e.printStackTrace();
            ret_node.put("message", "There was some internal server error in the database-communication");
            return ResponseEntity.status(500).body(ret_node);
        }
    }

    /**
     * 
     * @param json
     * @return
     */
    @DeleteMapping("/remove")
    public ResponseEntity<String> delete_user(@RequestBody JsonNode json) {
        if(!json.has("username"))
            return ResponseEntity.status(400).body("The username-parameter was not set");

        try {
            String username = json.get("username").asText();
            userAdapter.deleteUser(username);
            return ResponseEntity.status(200).body("The user was successfully deleted");
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("There was an internal server error with the database-communication");
        }

    }

}
