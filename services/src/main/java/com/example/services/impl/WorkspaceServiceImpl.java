package com.example.services.impl;


import com.example.domain.entities.Workspace;
import com.example.domain.exceptions.InvalidWorkspaceException;
import com.example.domain.exceptions.WorkspaceNotFoundException;
import com.example.domain.exceptions.enums.NotFoundErrorCodes;
import com.example.domain.exceptions.enums.ValidationErrorCodes;
import com.example.domain.repositories.WorkspaceRepository;
import com.example.services.WorkspaceService;
import com.example.services.notifications.events.WorkspaceConfitmationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Handles the logic for managing workspaces, including creating,
 * retrieving, and removing workspaces, as well as checking workspace availability.
 * Implementation of the {@link WorkspaceService} interface for managing workspace-related operations.
 */
@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Autowired
    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository, ApplicationEventPublisher eventPublisher) {
        this.workspaceRepository = workspaceRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Creates a new workspace.
     * Validates the workspace data and adds the workspace to the repository.
     *
     * @param workspace the workspace to be created
     * @throws InvalidWorkspaceException if any required field is invalid (type or price)
     */
    @Transactional
    @Override
    public void save(Workspace workspace) {
        if (workspace.getType().isEmpty()) {
            throw new InvalidWorkspaceException(ValidationErrorCodes.MISSING_FIELD, "Workspace type is required.");
        }
        if (workspace.getPrice() == null || workspace.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidWorkspaceException(ValidationErrorCodes.MISSING_FIELD, "Workspace price is required.");
        }
        workspaceRepository.save(workspace);
        eventPublisher.publishEvent(new WorkspaceConfitmationEvent(workspace));
    }

    /**
     * Retrieves all workspaces.
     *
     * @return a list of all workspaces
     * @throws InvalidWorkspaceException if no workspaces are found
     */
    @Transactional(readOnly = true)
    @Override
    public List<Workspace> findAll() {
        List<Workspace> workspaces = workspaceRepository.findAll();
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
    @Transactional(readOnly = true)
    @Override
    public Optional<Workspace> findById(Long workspaceId) {
        return workspaceRepository.findById(workspaceId);
    }

    /**
     * Retrieves an active workspace along with its associated reservations.
     *
     * @param id The ID of the workspace to retrieve.
     * @return The workspace with its reservations if found.
     */
    @Transactional(readOnly = true)
    @Override
    public Workspace getWorkspaceWithReservations(Long id) {
        return workspaceRepository.getWorkspaceWithReservations(id);
    }

    /**
     * Removes a workspace by its ID.
     *
     * @param workspaceId the ID of the workspace to remove
     */
    @Transactional
    @Override
    public void deleteById(Long workspaceId) {
        workspaceRepository.deleteById(workspaceId);
    }

}
