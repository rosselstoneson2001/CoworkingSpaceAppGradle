package com.example.ui.impl;


import com.example.entities.Reservation;
import com.example.exceptions.InvalidReservationException;
import com.example.exceptions.ReservationNotFoundException;
import com.example.exceptions.WorkspaceNotFoundException;
import com.example.exceptions.enums.ErrorCodes;
import com.example.services.ReservationService;
import com.example.services.WorkspaceService;
import com.example.ui.Menu;
import com.example.utils.ConstantMessages;
import com.example.utils.InputHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CustomerUI implements Menu {

    private static final Logger USER_LOGGER = LoggerFactory.getLogger("USER_LOGGER");
    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");

    private final WorkspaceService workspaceService;
    private final ReservationService reservationService;

    public CustomerUI(WorkspaceService workspaceService, ReservationService reservationService) {
        this.workspaceService = workspaceService;
        this.reservationService = reservationService;
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {

            USER_LOGGER.info(ConstantMessages.CUSTOMER_MENU);

            try {
                USER_LOGGER.info("Enter your option: ");
                int choice = sc.nextInt();
                sc.nextLine();  // Consume newline to avoid input skipping issues

                switch (choice) {
                    case 1 -> displayWorkspaces();
                    case 2 -> makeReservation();
                    case 3 -> viewReservation();
                    case 4 -> cancelReservation();
                    case 5 -> {
                        USER_LOGGER.info("Exiting customer menu...");
                        return;
                    }
                    default -> USER_LOGGER.error(ConstantMessages.getUserMessage(ErrorCodes.INVALID_CHOICE));
                }
            } catch (InputMismatchException e) {
                USER_LOGGER.error(ConstantMessages.getUserMessage(ErrorCodes.INVALID_INPUT));
                INTERNAL_LOGGER.error("[{}] - {}", ErrorCodes.INVALID_INPUT.getCode(), "Input was not a valid number.");
                sc.nextLine(); // Clear invalid input
            }
        }
    }


    private void displayWorkspaces() {
        try {
            USER_LOGGER.info("Displaying all available workspaces:");
            USER_LOGGER.info(workspaceService.getAll().toString());
        } catch (WorkspaceNotFoundException e) {
            USER_LOGGER.error(ConstantMessages.getUserMessage(e.getErrorCode()));
            INTERNAL_LOGGER.error("[{}] - {}", e.getErrorCode().getCode(), e.getMessage());
        }
    }

    private void makeReservation() {

        Long workspaceId = InputHelper.getLong.supplier(ConstantMessages.ENTER_SPACE_ID).get();
        String customerName = InputHelper.getString.supplier(ConstantMessages.ENTER_CUSTOMER_NAME).get();
        LocalDateTime startDateTime = InputHelper.getDateTime.supplier(ConstantMessages.ENTER_START_DATE).get();
        LocalDateTime endDateTime = InputHelper.getDateTime.supplier(ConstantMessages.ENTER_END_DATE).get();

        try {
            reservationService.create(new Reservation(workspaceId, customerName, startDateTime, endDateTime, LocalDateTime.now()));
            USER_LOGGER.info("Workspace booked successfully!");
        } catch (WorkspaceNotFoundException e) {
            USER_LOGGER.error(ConstantMessages.getUserMessage(e.getErrorCode()));
            INTERNAL_LOGGER.error("[{}] - {}", e.getErrorCode().getCode(), e.getMessage());
        } catch (InvalidReservationException e) {
            USER_LOGGER.error(ConstantMessages.getUserMessage(e.getErrorCode()));
            INTERNAL_LOGGER.error("[{}] - {}", e.getErrorCode().getCode(), e.getMessage());
        } catch (ReservationNotFoundException e) {
            USER_LOGGER.error(ConstantMessages.getUserMessage(e.getErrorCode()));
            INTERNAL_LOGGER.error("[{}] - {}", e.getErrorCode().getCode(), e.getMessage());
        }
    }

    private void viewReservation() {
        String customerName = InputHelper.getString.supplier(ConstantMessages.ENTER_CUSTOMER_NAME).get();
        try {
            USER_LOGGER.info("Reservations: \n{}", reservationService.findReservationsByCustomer(customerName));
        } catch (ReservationNotFoundException e) {
            USER_LOGGER.error(ConstantMessages.getUserMessage(e.getErrorCode()));
            INTERNAL_LOGGER.error("[{}] - {}", e.getErrorCode().getCode(), e.getMessage());
        }

    }

    private void cancelReservation() {
        Long reservationId = InputHelper.getLong.supplier(ConstantMessages.ENTER_RESERVATION_ID).get();
        try {
            reservationService.remove(reservationId);
            USER_LOGGER.info("Reservation ID {} cancelled successfully.", reservationId);
        } catch (ReservationNotFoundException e) {
            USER_LOGGER.error(ConstantMessages.getUserMessage(e.getErrorCode()));
            INTERNAL_LOGGER.error("[{}] - {}", e.getErrorCode().getCode(), e.getMessage());
        }
    }

}