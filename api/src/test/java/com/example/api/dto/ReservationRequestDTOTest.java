package com.example.api.dto;

import com.example.api.dto.requests.ReservationRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReservationRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidReservationRequestDTO() {
        ReservationRequestDTO dto = new ReservationRequestDTO(
                1L,
                2L,
                "Alice Smith",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        Set<ConstraintViolation<ReservationRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    void testNullWorkspaceId() {
        ReservationRequestDTO dto = ReservationRequestDTO.builder()
                .workspaceId(null)
                .userId(2L)
                .customerName("Alice")
                .startDateTime(LocalDateTime.now().plusHours(1))
                .endDateTime(LocalDateTime.now().plusHours(2))
                .build();

        Set<ConstraintViolation<ReservationRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Workspace ID is required")));
    }

    @Test
    void testNullUserId() {
        ReservationRequestDTO dto = ReservationRequestDTO.builder()
                .workspaceId(1L)
                .userId(null)
                .customerName("Alice")
                .startDateTime(LocalDateTime.now().plusHours(1))
                .endDateTime(LocalDateTime.now().plusHours(2))
                .build();

        Set<ConstraintViolation<ReservationRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("User ID is required")));
    }

    @Test
    void testBlankCustomerName() {
        ReservationRequestDTO dto = ReservationRequestDTO.builder()
                .workspaceId(1L)
                .userId(2L)
                .customerName(" ")
                .startDateTime(LocalDateTime.now().plusHours(1))
                .endDateTime(LocalDateTime.now().plusHours(2))
                .build();

        Set<ConstraintViolation<ReservationRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Customer name is required")));
    }

    @Test
    void testNullStartDateTime() {
        ReservationRequestDTO dto = ReservationRequestDTO.builder()
                .workspaceId(1L)
                .userId(2L)
                .customerName("Alice")
                .startDateTime(null)
                .endDateTime(LocalDateTime.now().plusHours(2))
                .build();

        Set<ConstraintViolation<ReservationRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Start time is required")));
    }

    @Test
    void testPastStartDateTime() {
        ReservationRequestDTO dto = ReservationRequestDTO.builder()
                .workspaceId(1L)
                .userId(2L)
                .customerName("Alice")
                .startDateTime(LocalDateTime.now().minusHours(1))
                .endDateTime(LocalDateTime.now().plusHours(2))
                .build();

        Set<ConstraintViolation<ReservationRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Start time must be in the future")));
    }

    @Test
    void testNullEndDateTime() {
        ReservationRequestDTO dto = ReservationRequestDTO.builder()
                .workspaceId(1L)
                .userId(2L)
                .customerName("Alice")
                .startDateTime(LocalDateTime.now().plusHours(1))
                .endDateTime(null)
                .build();

        Set<ConstraintViolation<ReservationRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("End time is required")));
    }

    @Test
    void testPastEndDateTime() {
        ReservationRequestDTO dto = ReservationRequestDTO.builder()
                .workspaceId(1L)
                .userId(2L)
                .customerName("Alice")
                .startDateTime(LocalDateTime.now().plusHours(1))
                .endDateTime(LocalDateTime.now().minusHours(1))
                .build();

        Set<ConstraintViolation<ReservationRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("End time must be in the future")));
    }
}
