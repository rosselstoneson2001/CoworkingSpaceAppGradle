package com.example.services.impl;

import com.example.entities.Workspace;
import com.example.exceptions.InvalidWorkspaceException;
import com.example.repositories.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WorkspaceServiceTest {

    private WorkspaceRepository workspaceRepository;
    private WorkspaceServiceImpl workspaceService;

    @BeforeEach
    void setUp() {
        workspaceRepository = mock(WorkspaceRepository.class);
        workspaceService = new WorkspaceServiceImpl(workspaceRepository);
    }

    @Test
    void shouldCreateWorkspaceSuccessfully() {
        Workspace workspace = new Workspace(new BigDecimal("100.0"), "Office");

        workspaceService.create(workspace);

        verify(workspaceRepository).add(workspace);
    }

    @Test
    void shouldReturnAllWorkspaces() {
        Workspace workspace1 = new Workspace(new BigDecimal("100.0"), "Office");
        Workspace workspace2 = new Workspace(new BigDecimal("150.0"), "Meeting");
        List<Workspace> workspaces = List.of(workspace1, workspace2);

        when(workspaceRepository.getAll()).thenReturn(workspaces);

        List<Workspace> result = workspaceService.getAll();

        assertEquals(workspaces, result);
    }

    @Test
    void shouldReturnWorkspaceById() {
        Workspace workspace = new Workspace(new BigDecimal("100.0"), "Office");
        when(workspaceRepository.getById(1L)).thenReturn(Optional.of(workspace));

        Optional<Workspace> result = workspaceService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(workspace, result.get());
    }

    @Test
    void shouldRemoveWorkspaceById() {
        workspaceService.remove(1L);

        verify(workspaceRepository).remove(1L);
    }

    @Test
    void shouldSaveWorkspaces() {
        workspaceService.save();

        verify(workspaceRepository).save();
    }

    @Test
    void shouldLoadWorkspaces() {
        workspaceService.load();

        verify(workspaceRepository).load();
    }

    // Exception testing

    @Test
    void shouldThrowInvalidWorkspaceExceptionWhenWorkspaceIsNull() {
        InvalidWorkspaceException thrown = assertThrows(InvalidWorkspaceException.class,
                () -> workspaceService.create(null));

        assertEquals("Workspace object cannot be null.", thrown.getMessage());
    }

    @Test
    void shouldThrowInvalidWorkspaceExceptionWhenTypeIsNull() {
        Workspace workspace = new Workspace(new BigDecimal("123.32"), null);

        InvalidWorkspaceException thrown = assertThrows(InvalidWorkspaceException.class,
                () -> workspaceService.create(workspace));

        assertEquals("Workspace type is required.", thrown.getMessage());
    }

    @Test
    void shouldThrowInvalidWorkspaceExceptionWhenPriceIsNull() {
        Workspace workspace = new Workspace(null, "Office");

        InvalidWorkspaceException thrown = assertThrows(InvalidWorkspaceException.class,
                () -> workspaceService.create(workspace));

        assertEquals("Workspace price is required.", thrown.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRepositoryThrowsError() {
        Workspace workspace = new Workspace(new BigDecimal("100.0"), "Office");

        doThrow(new InvalidWorkspaceException("Invalid data")).when(workspaceRepository).add(any());

        InvalidWorkspaceException thrown = assertThrows(InvalidWorkspaceException.class,
                () -> workspaceService.create(workspace));

        assertEquals("Invalid data", thrown.getMessage());
    }

}
