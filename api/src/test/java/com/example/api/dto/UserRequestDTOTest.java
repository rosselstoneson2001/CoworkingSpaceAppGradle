package com.example.api.dto;

import com.example.api.dto.requests.UserRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUserRequestDTO() {
        UserRequestDTO dto = new UserRequestDTO(
                "John",
                "Doe",
                "john.doe@example.com",
                "strongpassword123"
        );

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    void testBlankFirstName() {
        UserRequestDTO dto = new UserRequestDTO(
                "",
                "Doe",
                "john.doe@example.com",
                "strongpassword123"
        );

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("First name is required")));
    }

    @Test
    void testFirstNameTooLong() {
        UserRequestDTO dto = new UserRequestDTO(
                "A".repeat(51),
                "Doe",
                "john.doe@example.com",
                "strongpassword123"
        );

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("First name can have at most 50 characters")));
    }

    @Test
    void testBlankLastName() {
        UserRequestDTO dto = new UserRequestDTO(
                "John",
                "",
                "john.doe@example.com",
                "strongpassword123"
        );

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Last name is required")));
    }

    @Test
    void testInvalidEmail() {
        UserRequestDTO dto = new UserRequestDTO(
                "John",
                "Doe",
                "invalid-email",
                "strongpassword123"
        );

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Invalid email format")));
    }

    @Test
    void testBlankPassword() {
        UserRequestDTO dto = new UserRequestDTO(
                "John",
                "Doe",
                "john.doe@example.com",
                ""
        );

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Password is required")));
    }

    @Test
    void testPasswordTooShort() {
        UserRequestDTO dto = new UserRequestDTO(
                "John",
                "Doe",
                "john.doe@example.com",
                "123" // Too short
        );

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Password must be at least 8 characters")));
    }
}
