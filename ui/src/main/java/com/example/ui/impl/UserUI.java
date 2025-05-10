package com.example.ui.impl;

import com.example.entities.User;
import com.example.exceptions.InvalidUserException;
import com.example.exceptions.UserNotFoundException;
import com.example.exceptions.enums.ValidationErrorCodes;
import com.example.services.UserService;
import com.example.ui.Menu;
import com.example.utils.ConstantMessages;
import com.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class UserUI implements Menu {

    private static final Logger USER_LOGGER = LoggerFactory.getLogger("USER_LOGGER");
    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");

    private final UserService userService;

    public UserUI(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            USER_LOGGER.info(ConstantMessages.USER_MENU);
            try {
                USER_LOGGER.info("Enter your option: ");
                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> createUser();
                    case 2 -> viewAllUsers();
                    case 3 -> removeUser();
                    case 4 -> checkPassword();
                    case 5 -> {
                        USER_LOGGER.info("Exiting user panel...");
                        return;
                    }
                    default -> USER_LOGGER.error(ConstantMessages.getValidationUserMessage(ValidationErrorCodes.INVALID_CHOICE));
                }
            } catch (Exception e) {
                USER_LOGGER.error(ConstantMessages.getValidationUserMessage(ValidationErrorCodes.INVALID_INPUT));
                INTERNAL_LOGGER.error("[{}] - {}", ValidationErrorCodes.INVALID_INPUT.getCode(), e.getMessage());
                sc.nextLine(); // Clear invalid input
            }
        }
    }

    private void createUser() {
        String firstName = InputHelper.getString.supplier(ConstantMessages.ENTER_USER_FIRST_NAME).get();
        String lastName = InputHelper.getString.supplier(ConstantMessages.ENTER_USER_LAST_NAME).get();
        String email = InputHelper.getString.supplier(ConstantMessages.ENTER_USER_EMAIL).get();
        String password = InputHelper.getString.supplier(ConstantMessages.ENTER_USER_PASSWORD).get();

        try {
            User user = new User(firstName, lastName, email, password);
            userService.create(user);
            USER_LOGGER.info("User created successfully!");
        } catch (InvalidUserException e) {
            USER_LOGGER.error(ConstantMessages.getValidationUserMessage(e.getErrorCode()));
            INTERNAL_LOGGER.error("[{}] - {}", e.getErrorCode().getCode(), e.getMessage());
        }
    }

    private void viewAllUsers() {
        try {
            USER_LOGGER.info("Displaying all users:");
            List<User> users = userService.getAll();
            USER_LOGGER.info("User: \n{}", users);
        } catch (UserNotFoundException e) {
            USER_LOGGER.error(ConstantMessages.getNotFoundUserMessage(e.getErrorCode()));
            INTERNAL_LOGGER.error("[{}] - {}", e.getErrorCode().getCode(), e.getMessage());
        }
    }

    private void removeUser() {
        Long userId = InputHelper.getLong.supplier(ConstantMessages.ENTER_USER_ID).get();
        try {
            userService.remove(userId);
            USER_LOGGER.info("User with ID {} removed successfully!", userId);
        } catch (UserNotFoundException e) {
            USER_LOGGER.error(ConstantMessages.getNotFoundUserMessage(e.getErrorCode()));
            INTERNAL_LOGGER.error("[{}] - {}", e.getErrorCode().getCode(), e.getMessage());
        }
    }

    private void checkPassword() {
        Long userId = InputHelper.getLong.supplier(ConstantMessages.ENTER_USER_ID).get();
        String plainPassword = InputHelper.getString.supplier(ConstantMessages.ENTER_USER_PASSWORD).get();

        try {
            boolean isPasswordValid = userService.checkPassword(userId, plainPassword);

            if (isPasswordValid) {
                USER_LOGGER.info("Password is correct for user with ID {}.", userId);
            } else {
                USER_LOGGER.warn("Invalid password entered for user with ID {}.", userId);
            }
        } catch (UserNotFoundException e) {
            USER_LOGGER.error(ConstantMessages.getNotFoundUserMessage(e.getErrorCode()));
            INTERNAL_LOGGER.error("[{}] - {}", e.getErrorCode().getCode(), e.getMessage());
        } catch (InvalidUserException e) {
            USER_LOGGER.error(ConstantMessages.getValidationUserMessage(e.getErrorCode()));
            INTERNAL_LOGGER.error("[{}] - {}", ValidationErrorCodes.INVALID_INPUT.getCode(), e.getMessage());
        }
    }
}
