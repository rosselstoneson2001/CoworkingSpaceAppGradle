package com.example.ui.controllers;

import com.example.entities.User;
import com.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


/**
 * REST controller for managing users.
 * Provides endpoints for retrieving, creating, and deleting users,
 * as well as checking user passwords.
 *
 * <p>Endpoints:</p>
 * <ul>
 *     <li>GET /users - Get all users</li>
 *     <li>GET /users/{id} - Get a user by ID</li>
 *     <li>POST /users - Create a new user</li>
 *     <li>DELETE /users/{id} - Delete a user</li>
 *     <li>POST /users/{id}/check-password - Validate user password</li>
 * </ul>
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Fetch all users.
     * @return List of users.
     */
    @GetMapping("/get-all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Fetch a user by ID.
     * @param id User ID.
     * @return User if found, 404 otherwise.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        Optional<User> user = userService.getById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Create a new user.
     * @param user User details.
     * @return Created user with 201 status.
     */
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Delete a user by ID.
     * @param id User ID.
     * @return 204 No Content if successful, 404 if not found, 500 for other errors.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.remove(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Check user password.
     * @param userId User ID.
     * @param password Password.
     * @return JSON response indicating whether the password is valid.
     */
    @PostMapping("/check-password")
    public ResponseEntity<Boolean> checkPassword(@RequestParam Long userId, @RequestParam String password) {
        boolean isPasswordValid = userService.checkPassword(userId, password);
        if (isPasswordValid) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
        }
    }
}