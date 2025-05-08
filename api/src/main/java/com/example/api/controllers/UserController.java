package com.example.api.controllers;

import com.example.api.converters.UserConverter;
import com.example.api.dto.requests.UserRequestDTO;
import com.example.api.dto.responses.UserResponseDTO;
import com.example.domain.entities.User;
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
     * Fetch all users in the system.
     *
     * @return a list of {@link UserResponseDTO} representing all users.
     */
    @GetMapping("/get-all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserResponseDTO> userResponseDTOS = UserConverter.toDTO(users);
        return ResponseEntity.ok(userResponseDTOS);
    }

    /**
     * Fetch a user by their ID.
     *
     * @param id the ID of the user to fetch
     * @return the {@link UserResponseDTO} corresponding to the user, or 404 if not found.
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
     *
     * @param request the details of the user to be created
     * @return the created user with HTTP 201 status.
     */
    @PostMapping("/create")
    public ResponseEntity<UserRequestDTO> createUser(@RequestBody UserRequestDTO request) {
        User user = UserConverter.toEntity(request);
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     /**
     * Delete a user by their ID.
     *
     * @param id the ID of the user to delete
     * @return HTTP 204 No Content if the user is deleted successfully,
     *         404 if the user is not found, or 500 for other errors.
     */
    @DeleteMapping("remove/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
    }

    /**
     * Check whether a given password is valid for a user.
     *
     * @param userId the ID of the user whose password is being checked
     * @param password the password to validate
     * @return HTTP 200 OK with a boolean indicating whether the password is valid,
     *         or HTTP 403 Forbidden if the password is incorrect.
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