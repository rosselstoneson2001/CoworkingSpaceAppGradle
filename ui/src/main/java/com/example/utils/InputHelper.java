package com.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * A utility class to handle various types of user input.
 * It provides methods for obtaining input in different formats, including Integer, Long, String, BigDecimal, and LocalDateTime.
 * The input is validated, and appropriate error messages are logged in case of invalid input.
 */
public class InputHelper {

    private static final Logger USER_LOGGER = LoggerFactory.getLogger("USER_LOGGER");
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";


    public static InputSupplierCreator<Integer, Integer> getInt = new InputSupplierCreator<>(USER_LOGGER::info, () -> {
                while (!SCANNER.hasNextInt()) {
                    USER_LOGGER.error("Invalid input. Please enter a number.");
                    SCANNER.next();
                }
                Integer input = SCANNER.nextInt();
                SCANNER.nextLine();
                return input;
            });

    public static InputSupplierCreator<Long, Long> getLong = new InputSupplierCreator<>(USER_LOGGER::info, () -> {
        while (!SCANNER.hasNextLong()) {
            USER_LOGGER.error("Invalid input. Please enter a valid ID.");
            SCANNER.next();
        }
        Long input = SCANNER.nextLong();
        SCANNER.nextLine();
        return input;
    });

    public static InputSupplierCreator<String, String> getString = new InputSupplierCreator<>(USER_LOGGER::info, SCANNER::nextLine);

    public static InputSupplierCreator<BigDecimal, BigDecimal> getBigDecimal = new InputSupplierCreator<>(USER_LOGGER::info, () -> {
                while (!SCANNER.hasNextBigDecimal()) {
                    USER_LOGGER.error("Invalid input. Please enter a valid number.");
                    SCANNER.next();
                }
                BigDecimal input = SCANNER.nextBigDecimal();
                SCANNER.nextLine();
                return input;
            });

    public static InputSupplierCreator<LocalDateTime, String> getDateTime = new InputSupplierCreator<>(USER_LOGGER::info, () -> {
                while (true) {
                    try {
                        return SCANNER.nextLine();
                    } catch (DateTimeParseException e) {
                        USER_LOGGER.error("Invalid date format. Please enter a date in " + PATTERN + " format.");
                    }
                }
            }, input -> LocalDateTime.parse(input, DateTimeFormatter.ofPattern(PATTERN)));  // Convert the LocalDate back to String if needed
}
