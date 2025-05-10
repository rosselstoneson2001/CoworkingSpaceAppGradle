package com.example.ui.impl;


import com.example.exceptions.enums.ErrorCodes;
import com.example.repositories.ReservationRepository;
import com.example.repositories.WorkspaceRepository;
import com.example.repositories.impl.ReservationRepositoryImpl;
import com.example.repositories.impl.WorkspaceRepositoryImpl;
import com.example.services.ReservationService;
import com.example.services.WorkspaceService;
import com.example.services.impl.ReservationServiceImpl;
import com.example.services.impl.WorkspaceServiceImpl;
import com.example.utils.ConstantMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.InputMismatchException;
import java.util.Scanner;

public class GeneralMenuUI {

    private static final Logger USER_LOGGER = LoggerFactory.getLogger("USER_LOGGER");
    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");

    public void generalMenu() {

        // Create repository instances
        ReservationRepository reservationRepository = new ReservationRepositoryImpl();
        WorkspaceRepository workspaceRepository = new WorkspaceRepositoryImpl();

        // Create service instances
        WorkspaceService workspaceService = new WorkspaceServiceImpl(workspaceRepository);
        ReservationService reservationService = new ReservationServiceImpl(reservationRepository, workspaceService);

        // Create Customer UI and Admin UI
        CustomerUI customerUI = new CustomerUI(workspaceService, reservationService);
        AdminUI adminUI = new AdminUI(workspaceService, reservationService);

        // Initialize jsonDataStorage
        workspaceRepository.load();
        reservationRepository.load();

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
                    case 3 -> {
                        USER_LOGGER.info("Exiting application.");
                        reservationService.save();
                        workspaceService.save();
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
}
