package com.example.services.impl;

import com.example.entities.User;
import com.example.exceptions.InvalidUserException;
import com.example.exceptions.UserNotFoundException;
import com.example.exceptions.enums.ErrorCodes;
import com.example.repositories.UserRepository;
import com.example.services.UserService;
import com.example.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(User user) {
        if (user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null || user.getPassword() == null) {
            throw new InvalidUserException(ErrorCodes.INVALID_USER, "All user fields are required.");
        }

        userRepository.add(user);
        INTERNAL_LOGGER.info("User with email {} created successfully.", user.getEmail());
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.getAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException(ErrorCodes.USER_NOT_FOUND, "No users found.");
        } else {
            return users;
        }
    }

    @Override
    public Optional<User> getById(Long userId) {
        Optional<User> user = userRepository.getById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(ErrorCodes.USER_NOT_FOUND, "No user found with ID: " + userId);
        }
        return user;
    }

    @Override
    public void remove(Long userId) {
        if (getById(userId).isPresent()) {
            userRepository.remove(userId);
            INTERNAL_LOGGER.info("User with ID {} removed successfully.", userId);
        } else {
            throw new UserNotFoundException(ErrorCodes.USER_NOT_FOUND, "No user found with ID: " + userId);
        }
    }

    @Override
    public boolean checkPassword(Long userId, String plainPassword) {
        Optional<User> user = userRepository.getById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(ErrorCodes.USER_NOT_FOUND, "User not found with ID: " + userId);
        }

        String hashedPassword = user.get().getPassword();
        return PasswordUtils.checkPassword(plainPassword, hashedPassword);
    }
}
