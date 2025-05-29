package com.example.services.impl;

import com.example.domain.entities.Workspace;
import com.example.domain.exceptions.InvalidWorkspaceException;
import com.example.domain.exceptions.WorkspaceNotFoundException;
import com.example.domain.repositories.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WorkspaceServiceTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @InjectMocks
    private WorkspaceServiceImpl workspaceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave_ValidWorkspace_Success() {
        Workspace workspace = new Workspace();
        workspace.setType("Office");
        workspace.setPrice(BigDecimal.valueOf(100));

        workspaceService.save(workspace);

        verify(workspaceRepository, times(1)).save(workspace);
    }

    @Test
    public void testFindAll_WithWorkspaces_ReturnsList() {
        Workspace workspace = new Workspace();
        workspace.setType("Office");
        workspace.setPrice(BigDecimal.valueOf(100));

        when(workspaceRepository.findAll()).thenReturn(List.of(workspace));

        List<Workspace> result = workspaceService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    public void testFindById_Found_ReturnsWorkspace() {
        Workspace workspace = new Workspace();
        workspace.setType("Office");
        workspace.setPrice(BigDecimal.valueOf(100));

        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));

        Optional<Workspace> result = workspaceService.findById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    public void testGetWorkspaceWithReservations_ReturnsWorkspace() {
        Workspace workspace = new Workspace();
        when(workspaceRepository.getWorkspaceWithReservations(1L)).thenReturn(workspace);

        Workspace result = workspaceService.getWorkspaceWithReservations(1L);

        assertNotNull(result);
    }

    @Test
    public void testFindById_NotFound_ReturnsEmpty() {
        when(workspaceRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Workspace> result = workspaceService.findById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeleteById_DeletesSuccessfully() {
        doNothing().when(workspaceRepository).deleteById(1L);

        workspaceService.deleteById(1L);

        verify(workspaceRepository, times(1)).deleteById(1L);
    }

    // --- EXCEPTION CASES ---

    @Test
    public void testSave_InvalidType_ThrowsException() {
        Workspace workspace = new Workspace();
        workspace.setType("");
        workspace.setPrice(BigDecimal.valueOf(100));

        assertThrows(InvalidWorkspaceException.class, () -> workspaceService.save(workspace));
        verify(workspaceRepository, never()).save(any());
    }

    @Test
    public void testSave_InvalidPrice_ThrowsException() {
        Workspace workspace = new Workspace();
        workspace.setType("Office");
        workspace.setPrice(BigDecimal.ZERO);

        assertThrows(InvalidWorkspaceException.class, () -> workspaceService.save(workspace));
        verify(workspaceRepository, never()).save(any());
    }

    @Test
    public void testFindAll_EmptyList_ThrowsException() {
        when(workspaceRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(WorkspaceNotFoundException.class, () -> workspaceService.findAll());
    }
}
