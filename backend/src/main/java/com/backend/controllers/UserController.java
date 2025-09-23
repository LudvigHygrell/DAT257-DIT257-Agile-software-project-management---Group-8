package com.backend.controllers;

import com.backend.database.adapters.CommentsAdapter;
import com.backend.database.adapters.UserAdapter;

import org.springframework.beans.factory.annotation.Autowired;
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
     * A basic method for logging into a user. Varifies that the username and password checks against the database. 
     * @param json The input object for this REST controller
     * @return One of the following HTTP-packets:
     * <p>400 If there was some parameter missing (specified in the body) </p>
     * <p>401 If the username and password did not match for any user</p>
     * <p>200 If all went well, along with JWT data</p>
     */
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody JsonNode json) {
        if (!json.has("username"))
            return ResponseEntity.status(400).body("Missing username");
        if (!json.has("password"))
            return ResponseEntity.status(400).body("Missing password");

        String username = json.get("username").asText();
        String password = json.get("password").asText();

        if (userAdapter.login(username, password))
            return ResponseEntity.ok("Login successful"); // TODO: Add JWT data
        
        return ResponseEntity.status(401).body("Invalid username or password");
    }

    /**
     * Registers a new user with the provided username, email, and password.
     * @param json The input object for this REST controller. Must contain "username", "email", and "password".
     * @return One of the following HTTP-packets:
     * <p>400 If there was some parameter missing (specified in the body) </p>
     * <p>409 If the username or email already exists</p>
     * <p>200 If all went well, and the user was registered</p>
     * <p>500 If there was an error registering the user</p>
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

    /**
     * Changes the password for a user.
     * @param json The input object for this REST controller. Must contain "username", "old" (password), and "new" (password).
     * @return One of the following HTTP-packets:
     * <p>400 If there was some parameter missing (specified in the body) </p>
     * <p>401 If the username and old password did not match for any user</p>
     * <p>200 If all went well, and the password was changed</p>
     * <p>500 If there was an error changing the password</p>
     */
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


	/**
     * Resets the password for a user using a verification code.
     * @param json The input object for this REST controller. Must contain "email", "new_password", and "verification_code".
     * @return One of the following HTTP-packets:
     * <p>400 If there was some parameter missing (specified in the body) </p>
     * <p>200 If all went well, and the password was reset</p>
     * <p>500 If there was an error resetting the password</p>
     */
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
     * Changes the email for a user.
     * @param json The input object for this REST controller. Must contain "username", "email", and "password".
     * @return One of the following HTTP-packets:
     * <p>400 If there was some parameter missing (specified in the body) </p>
     * <p>401 If the email already exists</p>
     * <p>402 If the user-credentials provided did not match any account</p>
     * <p>200 If all went well, and the email was changed</p>
     * <p>500 If there was an error changing the email</p>
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
     * Retrieves the activity for a user, including comments and likes.
     * @param json The input object for this REST controller. Must contain "username" and "type" (e.g., ["comments", "likes"]).
     * @return One of the following HTTP-packets:
     * <p>400 If there was some parameter missing (specified in the body) </p>
     * <p>200 If all went well, along with the user's activity data</p>
     * <p>500 If there was an internal server error</p>
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
     * Deletes a user.
     * @param json The input object for this REST controller. Must contain "username".
     * @return One of the following HTTP-packets:
     * <p>400 If there was some parameter missing (specified in the body) </p>
     * <p>200 If all went well, and the user was deleted</p>
     * <p>500 If there was an internal server error</p>
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
