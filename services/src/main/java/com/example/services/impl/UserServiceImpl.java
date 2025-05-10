package com.example.services.impl;

import com.example.entities.User;
import com.example.exceptions.InvalidUserException;
import com.example.exceptions.UserNotFoundException;
import com.example.exceptions.enums.NotFoundErrorCodes;
import com.example.exceptions.enums.ValidationErrorCodes;
import com.example.repositories.UserRepository;
import com.example.services.UserService;
import com.example.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Provides operations related to user management.
 * Implementation of the {@link UserService} interface.
 */
public class UserServiceImpl implements UserService {

    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user after validating required fields.
     *
     * @param user the user to create.
     * @throws InvalidUserException if any required field is missing.
     */
    @Override
    public void create(User user) {
        if (user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null || user.getPassword() == null) {
            throw new InvalidUserException(ValidationErrorCodes.MISSING_FIELD, "All user fields are required.");
        }

        userRepository.add(user);
        INTERNAL_LOGGER.info("User with email {} created successfully.", user.getEmail());
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return a list of all users.
     * @throws UserNotFoundException if no users are found.
     */
    @Override
    public List<User> getAll() {
        List<User> users = userRepository.getAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException(NotFoundErrorCodes.USER_NOT_FOUND, "Failed to retrieve all users. No users found in the system.");
        } else {
            return users;
        }
    }

    /**
     * Retrieves a user by ID.
     *
     * @param userId the ID of the user.
     * @return an {@link Optional} containing the user if found.
     * @throws UserNotFoundException if no user is found with the given ID.
     */
    @Override
    public Optional<User> getById(Long userId) {
        Optional<User> user = userRepository.getById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(NotFoundErrorCodes.USER_NOT_FOUND, "No user found with ID: " + userId);
        }
        return user;
    }

    /**
     * Removes a user by ID.
     *
     * @param userId the ID of the user to remove.
     * @throws UserNotFoundException if the user does not exist.
     */
    @Override
    public void remove(Long userId) {
        if (getById(userId).isPresent()) {
            userRepository.remove(userId);
            INTERNAL_LOGGER.info("User with ID {} removed successfully.", userId);
        } else {
            throw new UserNotFoundException(NotFoundErrorCodes.USER_NOT_FOUND, "Failed to delete user. No user found with ID: " + userId);
        }
    }

    /**
     * Checks if the provided password matches the stored hashed password for a user.
     *
     * @param userId        the ID of the user.
     * @param plainPassword the raw password input by the user.
     * @return {@code true} if the password matches; otherwise, {@code false}.
     * @throws UserNotFoundException if no user is found with the given ID.
     */
    @Override
    public boolean checkPassword(Long userId, String plainPassword) {
        Optional<User> user = userRepository.getById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(NotFoundErrorCodes.USER_NOT_FOUND, "Failed to check password. User not found with ID: " + userId);
        }

        String hashedPassword = user.get().getPassword();
        return PasswordUtils.checkPassword(plainPassword, hashedPassword);
    }
}
