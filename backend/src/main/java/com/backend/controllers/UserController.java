package com.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import com.backend.database.UserAdapter;

/**
 * The UserController class handles user-related API endpoints such as login and registration. This class contains methods to process incoming requests, validate input data, and interact with the UserAdapter for database operations.
 * @author LVFK04
 * @version 1.0
 * @since 2025-09-16
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * 
     * @param json
     * @return
     */
    @GetMapping("/login")
    public ResponseEntity<String> login (@RequestBody JsonNode json) {
        if(!json.has("username")) 
            return ResponseEntity.badRequest().body("Missing username");
        if(!json.has("password"))
            return ResponseEntity.badRequest().body("Missing password");

        String username = json.get("username").asText();
        String password = json.get("password").asText();

        if(UserAdapter.login(username, password)) 
            return ResponseEntity.ok("Login successful");
        
        return ResponseEntity.status(401).body("Invalid username or password");
    }

    /**
     * This method returns a ResponseEntity with the HTTP code 200 OK if everything went alright. In the response body you'll find all necessary 
     * such as JWT information. If the response HTTP code is 409 it means that the Username or Email set already was in use. You can also get a 
     * code, meaning that you 
     * @param json
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<String> register (@RequestBody JsonNode json) {
        if(!json.has("username")) 
            return ResponseEntity.badRequest().body("Missing username");
        if(!json.has("email"))
            return ResponseEntity.badRequest().body("Missing email");
        if(!json.has("password"))
            return ResponseEntity.badRequest().body("Missing password");

        String username = json.get("username").asText();
        String email = json.get("email").asText();
        String password = json.get("password").asText();

        if(UserAdapter.is_username()) 
            return ResponseEntity.status(409).body("Username already exists");
        
        if(UserAdapter.is_email())
            return ResponseEntity.status(409).body("Email already exists");

        try {
            UserAdapter.register(username, email, password);
            return ResponseEntity.ok("User registered successfully");
        } catch(Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(500).body("Error registering user");
    }

    @PutMapping("/change_password")
    public ResponseEntity<String> change_password (@RequestBody JsonNode json) {
        if(!json.has("username"))
            return ResponseEntity.badRequest().body("Missing username");
        if(!json.has("old"))
            return ResponseEntity.badRequest().body("Missing old password");
        if(!json.has("new"))
            return ResponseEntity.badRequest().body("Missing new password");

        String username = json.get("username").asText();
        String old_password = json.get("old").asText();
        String new_password = json.get("new").asText();
        if(old_password.equals(new_password))
            return ResponseEntity.badRequest().body("New password must be different from old password");

        if(!UserAdapter.login(username, old_password))
            return ResponseEntity.status(401).body("Invalid username or password");
        UserAdapter.change_password(username, new_password);
        return ResponseEntity.ok("Password changed successfully");
        
    }
   @DeleteMapping("/delete_user")
   public ResponseEntity<String> delete_password (@RequestBody JsonNode json) {
	return null;
   }

   @PutMapping("/change_email")
   public ResponseEntity<String> change_email(@RequestBody JsonNode json) {
	return null;
   }
   
}