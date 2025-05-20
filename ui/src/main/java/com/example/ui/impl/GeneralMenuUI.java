package com.example.ui.impl;


import com.example.exceptions.enums.ValidationErrorCodes;
import com.example.utils.ConstantMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class GeneralMenuUI {

    private static final Logger USER_LOGGER = LoggerFactory.getLogger("USER_LOGGER");
    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");

    private final AdminUI adminUI;
    private final CustomerUI customerUI;
    private final UserUI userUI;

    public GeneralMenuUI(AdminUI adminUI,
                         CustomerUI customerUI,
                         UserUI userUI) {
        this.adminUI = adminUI;
        this.customerUI = customerUI;
        this.userUI = userUI;
    }

    public void generalMenu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            USER_LOGGER.info(ConstantMessages.MAIN_MENU);
            try {
                USER_LOGGER.info("Enter your option: ");
                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> adminUI.showMenu();
                    case 2 -> customerUI.showMenu();
                    case 3 -> userUI.showMenu();
                    case 4 -> {
                        USER_LOGGER.info("Exiting application.");
                        return;
                    }
                    default ->
                            USER_LOGGER.error(ConstantMessages.getValidationUserMessage(ValidationErrorCodes.INVALID_CHOICE));
                }
            } catch (InputMismatchException e) {
                USER_LOGGER.error(ConstantMessages.getValidationUserMessage(ValidationErrorCodes.INVALID_INPUT));
                INTERNAL_LOGGER.error("[{}] - {}", ValidationErrorCodes.INVALID_INPUT.getCode(), "Input was not a valid number.");
                sc.nextLine(); // Clear invalid input
            }
        }
    }
}
