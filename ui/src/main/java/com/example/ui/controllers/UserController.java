package com.example.ui.controllers;

import com.example.converters.UserConverter;
import com.example.dto.requests.UserRequestDTO;
import com.example.dto.responses.UserResponseDTO;
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
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserResponseDTO> userResponseDTOS = UserConverter.toDTO(users);
        return ResponseEntity.ok(userResponseDTOS);
    }

    /**
     * Fetch a user by ID.
     * @param id User ID.
     * @return User if found, 404 otherwise.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") Long id) {
        Optional<User> user = userService.findById(id);
        return user
                .map(u -> ResponseEntity.ok(UserConverter.toDTO(u)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Create a new user.
     * @param request User details.
     * @return Created user with 201 status.
     */
    @PostMapping("/create")
    public ResponseEntity<UserRequestDTO> createUser(@RequestBody UserRequestDTO request) {
        User user = UserConverter.toEntity(request);
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    /**
     * Delete a user by ID.
     * @param id User ID.
     * @return 204 No Content if successful, 404 if not found, 500 for other errors.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteById(id);
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