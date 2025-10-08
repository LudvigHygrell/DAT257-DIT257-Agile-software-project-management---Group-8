package com.backend.controllers;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.ApplicationProperties;
import com.backend.database.PasswordHashUtility;
import com.backend.database.adapters.UserAdapter;
import com.backend.email.EmailConfirmations;
import com.backend.email.EmailService;
import com.backend.jwt.JwtUtil;
import com.backend.jwt.user.UserDetail;
import com.backend.jwt.user.UserDetailService;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * The UserController class handles user-related API endpoints such as login and
 * registration. This class contains methods to process incoming requests,
 * validate input data, and interact with the UserAdapter for database
 * operations.
 *
 * @author LVFK04
 * @version 1.0
 * @since 2025-09-16
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserAdapter userAdapter;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordHashUtility encoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ApplicationProperties props;

    /**
     * A basic method for logging into a user. Varifies that the username and
     * password checks against the database.
     *
     * @param json The input object for this REST controller
     * @return One of the following HTTP-packets:
     * <p>
     * 400 If there was some parameter missing (specified in the body) </p>
     * <p>
     * 401 If the username and password did not match for any user</p>
     * <p>
     * 200 If all went well, along with JWT data</p>
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JsonNode json) {

        if (!json.has("username") && !json.has("email")) {
            return ResponseEntity.status(400).body("Missing username or email");
        }
        if (!json.has("password")) {
            return ResponseEntity.status(400).body("Missing password");
        }

        if (json.has("username") && json.has("email")) {
            return ResponseEntity.status(400).body("Cannot specify both email and username at once.");
        }

        String username;
        if (json.has("username")) {
            username = json.get("username").asText();
        } else {
            try {
                username = userAdapter.getUsernameFromEmail(json.get("email").asText())
                        .orElseThrow();
            } catch (Exception ex) {
                return ResponseEntity.status(401).body("Invalid email");
            }
        }
        String password = json.get("password").asText();

        if (userAdapter.login(username, password)) {
            final UserDetail user = (UserDetail) this.userDetailService.loadUserByUsername(username);
            final String jwt = jwtUtil.generateToken((UserDetails) user);
            return ResponseEntity.ok().body("{\"token\": \"" + jwt + "\"}"); // Returns the JWT token
        }

        return ResponseEntity.status(401).body("Invalid username or password");
    }

    /**
     * Registers a new user with the provided username, email, and password.
     *
     * @param json The input object for this REST controller. Must contain
     * "username", "email", and "password".
     * @return One of the following HTTP-packets:
     * <p>
     * 400 If there was some parameter missing (specified in the body) </p>
     * <p>
     * 409 If the username or email already exists</p>
     * <p>
     * 200 If all went well, and the user was registered</p>
     * <p>
     * 500 If there was an error registering the user</p>
     */
    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<String>> register(@RequestBody JsonNode json) {

        if (!json.has("username")) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.badRequest().body("Missing username"));
        }
        if (!json.has("email")) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.badRequest().body("Missing email"));
        }
        if (!json.has("password")) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.badRequest().body("Missing password"));
        }

        String username = json.get("username").asText();
        String email = json.get("email").asText();
        String password = json.get("password").asText();

        if (username.contains("@")) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(400).body("Usernames cannot contain the '@' character."));
        }

        if (userAdapter.isUsername(username)) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(409).body("Username already exists"));
        }

        if (userAdapter.isEmail(email)) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(409).body("Email already exists"));
        }

        if (props.getEmailProperties().isVerified()) {
            CompletableFuture<String> fut;
            try {
                fut = emailService.sendEmailConfirmation(email);
            } catch (Exception ex) {
                return CompletableFuture.completedFuture(
                        ResponseEntity.status(500).body("Failed to send email."));
            }
            return fut.thenApply(confirmedEmail -> {
                return ControllerHelper.orElseResponse(() -> {
                    userAdapter.register(username, confirmedEmail, password);
                    return "User registered successfully.";
                }, "Error registering user.");
            }).exceptionally(ex -> {
                EmailConfirmations.getInstance().abort(email);
                return ResponseEntity.status(500).body("Failed to confirm email.");
            });
        }

        return ControllerHelper.orElseFutureResponse(() -> {
            userAdapter.register(username, email, password);
            return "User registered successfully";
        }, "Error registering user.");
    }

    /**
     * Changes the password for a user.
     *
     * @param json The input object for this REST controller. Must contain
     * "username", "old" (password), and "new" (password).
     * @return One of the following HTTP-packets:
     * <p>
     * 400 If there was some parameter missing (specified in the body) </p>
     * <p>
     * 401 If the username and old password did not match for any user</p>
     * <p>
     * 200 If all went well, and the password was changed</p>
     * <p>
     * 500 If there was an error changing the password</p>
     */
    @PutMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestBody JsonNode json) {
        if (!json.has("username")) {
            return ResponseEntity.badRequest().body("Missing username");
        }
        if (!json.has("old")) {
            return ResponseEntity.badRequest().body("Missing old password");
        }
        if (!json.has("new")) {
            return ResponseEntity.badRequest().body("Missing new password");
        }

        String username = json.get("username").asText();
        String old_password = json.get("old").asText();
        String new_password = json.get("new").asText();

        if (old_password.equals(new_password)) {
            return ResponseEntity.badRequest().body("New password must be different from old password");
        }

        if (!userAdapter.getPassword(username)
                .map((pw) -> encoder.passwordMatches(pw, old_password))
                .orElse(false)) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        return ControllerHelper.orElseResponse(() -> {
            userAdapter.changePassword(username, new_password);
            return "Password changed successfully";
        }, "Error changing password.");
    }

    /**
     * Resets the password for a user using a verification code.
     *
     * @param json The input object for this REST controller. Must contain
     * "email", "new_password", and "verification_code".
     * @return One of the following HTTP-packets:
     * <p>
     * 400 If there was some parameter missing (specified in the body) </p>
     * <p>
     * 200 If all went well, and the password was reset</p>
     * <p>
     * 500 If there was an error resetting the password</p>
     */
    @PutMapping("/reset_password")
    public ResponseEntity<String> resetPassword(@RequestBody JsonNode json) {
        if (!json.has("email")) {
            return ResponseEntity.status(400).body("The email parameter is not set.");
        }
        if (!json.has("new_password")) {
            return ResponseEntity.status(400).body("The new_password parameter is not set.");
        }
        if (!json.has("verification_code")) {
            return ResponseEntity.status(400).body("The verification_code parameter was not set.");
        }

        // TODO: implement the check for verification code
        String email = json.get("email").asText();
        Optional<String> username = userAdapter.getUsernameFromEmail(email);
        String new_password = json.get("new_password").asText();

        if (username.isEmpty()) {
            return ResponseEntity.status(500).body("Error in getting username for provided email.");
        }

        return ControllerHelper.orElseResponse(() -> {
            userAdapter.changePassword(username.get(), new_password);
            return "All went fine. You password is now changed!";
        }, "There was something wrong at the server->database communication.");
    }

    /**
     * Changes the email for a user.
     *
     * @param json The input object for this REST controller. Must contain
     * "username", "email", and "password".
     * @return One of the following HTTP-packets:
     * <p>
     * 400 If there was some parameter missing (specified in the body) </p>
     * <p>
     * 401 If the email already exists</p>
     * <p>
     * 402 If the user-credentials provided did not match any account</p>
     * <p>
     * 200 If all went well, and the email was changed</p>
     * <p>
     * 500 If there was an error changing the email</p>
     */
    @PutMapping("/change_email")
    public ResponseEntity<String> changeEmail(@RequestBody JsonNode json) {
        
        if (!json.has("username")) {
            return ResponseEntity.status(400).body("The username parameter was not set");
        }
        if (!json.has("email")) {
            return ResponseEntity.status(400).body("The email parameter was not set.");
        }
        if (!json.has("password")) {
            return ResponseEntity.status(400).body("The password parameter was not set.");
        }

        String username = json.get("username").asText();
        String password = json.get("password").asText();
        String email = json.get("email").asText();

        if (userAdapter.isEmail(email)) {
            return ResponseEntity.status(401).body("The email you set is already a registered email at our site");
        }
        if (!userAdapter.login(username, password)) {
            return ResponseEntity.status(402).body("The user-credentials provided did not match any account");
        }
        return ControllerHelper.orElseResponse(() -> {
            userAdapter.changeEmail(username, email);
            return "Your email-address was successfully changed";
        }, "There was an error at the server - database communication."
                            + " This is nothing you can do about, but will be fixed in the soon future.");
    }

    /**
     * Deletes a user.
     *
     * @param json The input object for this REST controller. Must contain
     * "username".
     * @return One of the following HTTP-packets:
     * <p>
     * 400 If there was some parameter missing (specified in the body) </p>
     * <p>
     * 200 If all went well, and the user was deleted</p>
     * <p>
     * 500 If there was an internal server error</p>
     */
    @DeleteMapping("/remove")
    public ResponseEntity<String> delete_user(@RequestBody JsonNode json) {
        if (!json.has("username")) {
            return ResponseEntity.status(400).body("The username parameter was not set");
        }
        return ControllerHelper.orElseResponse(() -> {
            String username = json.get("username").asText();
            userAdapter.deleteUser(username);
            return "The user was successfully deleted";
        }, "There was an internal server error with the database communication");
    }
}
