package com.example.api.dto;

import com.example.api.dto.requests.WorkspaceRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.*;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class WorkspaceRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidWorkspaceRequestDTO() {
        WorkspaceRequestDTO dto = new WorkspaceRequestDTO("Private Office", BigDecimal.valueOf(100.00));

        Set<ConstraintViolation<WorkspaceRequestDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Expected no constraint violations");
    }

    @Test
    void testTypeIsBlank() {
        WorkspaceRequestDTO dto = new WorkspaceRequestDTO("   ", BigDecimal.valueOf(100.00));

        Set<ConstraintViolation<WorkspaceRequestDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Type is required", violations.iterator().next().getMessage());
    }

    @Test
    void testTypeTooLong() {
        String longType = "A".repeat(51);
        WorkspaceRequestDTO dto = new WorkspaceRequestDTO(longType, BigDecimal.valueOf(100.00));

        Set<ConstraintViolation<WorkspaceRequestDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Type can have at most 50 characters", violations.iterator().next().getMessage());
    }

    @Test
    void testPriceIsNull() {
        WorkspaceRequestDTO dto = new WorkspaceRequestDTO("Hot Desk", null);

        Set<ConstraintViolation<WorkspaceRequestDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Price is required", violations.iterator().next().getMessage());
    }

    @Test
    void testPriceIsZero() {
        WorkspaceRequestDTO dto = new WorkspaceRequestDTO("Hot Desk", BigDecimal.ZERO);

        Set<ConstraintViolation<WorkspaceRequestDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Price must be greater than zero", violations.iterator().next().getMessage());
    }
}
