package com.example.services.impl;


import com.example.entities.Workspace;
import com.example.exceptions.InvalidWorkspaceException;
import com.example.exceptions.WorkspaceNotFoundException;
import com.example.exceptions.enums.NotFoundErrorCodes;
import com.example.exceptions.enums.ValidationErrorCodes;
import com.example.repositories.WorkspaceRepository;
import com.example.services.WorkspaceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Handles the logic for managing workspaces, including creating,
 * retrieving, and removing workspaces, as well as checking workspace availability.
 * Implementation of the {@link WorkspaceService} interface for managing workspace-related operations.
 */
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    /**
     * Creates a new workspace.
     * Validates the workspace data and adds the workspace to the repository.
     *
     * @param workspace the workspace to be created
     * @throws InvalidWorkspaceException if any required field is invalid (type or price)
     */
    @Override
    public void create(Workspace workspace) {
        if (workspace.getType().isEmpty()) {
            throw new InvalidWorkspaceException(ValidationErrorCodes.MISSING_FIELD, "Workspace type is required.");
        }
        if (workspace.getPrice() == null || workspace.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidWorkspaceException(ValidationErrorCodes.MISSING_FIELD, "Workspace price is required.");
        }
        workspaceRepository.add(workspace);
    }

    /**
     * Retrieves all workspaces.
     *
     * @return a list of all workspaces
     * @throws InvalidWorkspaceException if no workspaces are found
     */
    @Override
    public List<Workspace> getAll() {
        List<Workspace> workspaces = workspaceRepository.getAll();
        if (workspaces.isEmpty()) {
            throw new WorkspaceNotFoundException(NotFoundErrorCodes.WORKSPACE_NOT_FOUND, "Failed to delete workspace. No workspace found.");
        } else {
            return workspaces;
        }
    }

    /**
     * Retrieves a workspace by its ID.
     *
     * @param workspaceId the ID of the workspace
     * @return an Optional containing the workspace if found, empty otherwise
     */
    @Override
    public Optional<Workspace> getById(Long workspaceId) {
        return workspaceRepository.getById(workspaceId);
    }

    /**
     * Retrieves an active workspace along with its associated reservations.
     *
     * @param id The ID of the workspace to retrieve.
     * @return The workspace with its reservations if found.
     */
    @Override
    public Workspace getWorkspaceWithReservations(Long id) {
        return workspaceRepository.getWorkspaceWithReservations(id);
    }

    /**
     * Removes a workspace by its ID.
     *
     * @param workspaceId the ID of the workspace to remove
     */
    @Override
    public void remove(Long workspaceId) {
        workspaceRepository.remove(workspaceId);
    }

}
