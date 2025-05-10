package com.example.repositories;

import com.example.entities.Workspace;

/**
 * Repository interface for managing {@link Workspace} entities.
 * <p>
 * This interface provides methods for accessing and manipulating workspace data.
 * It extends the {@link CrudRepository} to inherit basic CRUD operations
 * for the {@link Workspace} entity, such as creating, reading, updating, and deleting workspaces.
 * </p>
 */
public interface WorkspaceRepository extends CrudRepository<Workspace, Long> {

    Workspace getWorkspaceWithReservations(Long id);
}
