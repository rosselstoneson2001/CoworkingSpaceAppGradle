package com.example.services;


import com.example.entities.Workspace;

/**
 * Service interface for managing workspace-related operations.
 * <p>
 * Extends the {@link CrudService} interface to provide basic CRUD operations for workspaces.
 * This service allows managing workspaces in the system, including creating, reading, updating, and deleting workspaces.
 * </p>
 */
public interface WorkspaceService extends CrudService<Workspace, Long> {

    Workspace getWorkspaceWithReservations(Long id);

}
