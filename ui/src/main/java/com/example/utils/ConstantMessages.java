package com.example.utils;

import com.example.exceptions.enums.ErrorCodes;

import java.util.Map;

public class ConstantMessages {

    public static final String MAIN_MENU =
            """
                    Welcome to the Coworking Space Reservation Application
                    1. Admin Menu
                    2. Customer Menu
                    3. User Menu
                    4. Exit""";

    public static final String ADMIN_MENU =
            """
                    Admin Menu:
                    1. Add Workspace
                    2. Remove Workspace
                    3. View Reservations
                    4. Exit""";

    public static final String CUSTOMER_MENU =
            """
                    Customer Menu:
                    1. Browse Spaces
                    2. Make a Reservation
                    3. View My Reservations
                    4. Cancel Reservation
                    5. Exit""";

    public static final String USER_MENU =
            """
                    User Menu:
                    1. Create User
                    2. View All Users
                    3. Remove User
                    4. Check Password
                    5. Exit""";


    public static final String ENTER_WORKSPACE_ID = "Enter Workspace ID";
    public static final String ENTER_WORKSPACE_TYPE = "Enter Workspace Type: ";
    public static final String ENTER_WORKSPACE_PRICE = "Enter Workspace Price: ";
    public static final String ENTER_SPACE_ID = "Enter Space ID: ";
    public static final String ENTER_CUSTOMER_NAME = "Enter Customer Name: ";
    public static final String ENTER_START_DATE = "Enter Start Date (yyyy-MM-dd): ";
    public static final String ENTER_END_DATE = "Enter End Date (yyyy-MM-dd): ";
    public static final String ENTER_RESERVATION_ID = "Enter Reservation ID: ";
    public static final String ENTER_USER_FIRST_NAME = "Enter the user's first name: ";
    public static final String ENTER_USER_LAST_NAME = "Enter the user's last name: ";
    public static final String ENTER_USER_EMAIL = "Enter the user's email: ";
    public static final String ENTER_USER_PASSWORD = "Enter the user's password: ";
    public static final String ENTER_USER_ID = "Enter the user ID: ";



    public static final Map<ErrorCodes, String> userMessages = Map.of(
            ErrorCodes.RESERVATION_NOT_FOUND, "Reservation not found.",
            ErrorCodes.INVALID_RESERVATION, "Invalid reservation. Please check the provided details.",
            ErrorCodes.USER_NOT_FOUND, "User not found.",
            ErrorCodes.INVALID_USER, "Invalid user. Please check the provided details.",
            ErrorCodes.WORKSPACE_NOT_FOUND, "Workspace not found.",
            ErrorCodes.INVALID_WORKSPACE, "Invalid workspace. Please check the provided details.",
            ErrorCodes.INVALID_INPUT, "Invalid input! Please try again.",
            ErrorCodes.INVALID_CHOICE, "Invalid choice! Please try again."

    );

    public static String getUserMessage(ErrorCodes errorCode) {
        return userMessages.getOrDefault(errorCode, "An unknown error occurred.");
    }

}
