package com.example.ui.impl;

import com.example.entities.Workspace;
import com.example.exceptions.InvalidWorkspaceException;
import com.example.exceptions.ReservationNotFoundException;
import com.example.exceptions.WorkspaceNotFoundException;
import com.example.exceptions.enums.ValidationErrorCodes;
import com.example.services.ReservationService;
import com.example.services.WorkspaceService;
import com.example.ui.Menu;
import com.example.utils.ConstantMessages;
import com.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminUI implements Menu {

    private static final Logger USER_LOGGER = LoggerFactory.getLogger("USER_LOGGER");
    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");

    private final WorkspaceService workspaceService;
    private final ReservationService reservationService;

    public AdminUI(WorkspaceService workspaceService, ReservationService reservationService) {
        this.workspaceService = workspaceService;
        this.reservationService = reservationService;
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            USER_LOGGER.info(ConstantMessages.ADMIN_MENU);
            try {
                USER_LOGGER.info("Enter your option: ");
                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> addWorkspace();
                    case 2 -> removeWorkspace();
                    case 3 -> viewAllReservations();
                    case 4 -> {
                        USER_LOGGER.info("Exiting admin panel...");
                        return;
                    }
                    default -> USER_LOGGER.error(ConstantMessages.getValidationUserMessage(ValidationErrorCodes.INVALID_CHOICE));
                }
            } catch (InputMismatchException e) {
                USER_LOGGER.error(ConstantMessages.getValidationUserMessage(ValidationErrorCodes.INVALID_INPUT));
                INTERNAL_LOGGER.error("[{}] - {}", ValidationErrorCodes.INVALID_INPUT.getCode(), "Input was not a valid number.");

                sc.nextLine(); // Clear invalid input
            }
        }
    }


    private void addWorkspace() {
        String type = InputHelper.getString.supplier(ConstantMessages.ENTER_WORKSPACE_TYPE).get();
        BigDecimal price = InputHelper.getBigDecimal.supplier(ConstantMessages.ENTER_WORKSPACE_PRICE).get();

        try {
            workspaceService.create(new Workspace(price, type));
            USER_LOGGER.info("Workspace added successfully!");
        } catch (InvalidWorkspaceException e) {
            USER_LOGGER.error(ConstantMessages.getValidationUserMessage(e.getErrorCode()));
            INTERNAL_LOGGER.error("[{}] - {}", e.getErrorCode().getCode(), e.getMessage());
        }
    }

    private void removeWorkspace() {
        Long workspaceId = InputHelper.getLong.supplier(ConstantMessages.ENTER_WORKSPACE_ID).get();
        try {
            workspaceService.remove(workspaceId);
            USER_LOGGER.info("Workspace with ID {} removed successfully!", workspaceId);
        } catch (WorkspaceNotFoundException e) {
            USER_LOGGER.error(ConstantMessages.getNotFoundUserMessage(e.getErrorCode()));
            INTERNAL_LOGGER.error("[{}] - {}", e.getErrorCode().getCode(), e.getMessage());
        }
    }

    private void viewAllReservations() {
        try {
            USER_LOGGER.info("Displaying all reservations:");
            USER_LOGGER.info("Reservations: \n{}", reservationService.getAll());
        } catch (ReservationNotFoundException e) {
            USER_LOGGER.error(ConstantMessages.getNotFoundUserMessage(e.getErrorCode()));
            INTERNAL_LOGGER.error("[{}] - {}", e.getErrorCode().getCode(), e.getMessage());
        }
    }


}