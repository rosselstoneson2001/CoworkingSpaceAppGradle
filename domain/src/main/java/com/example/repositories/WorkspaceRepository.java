package com.example.repositories;

import com.example.entities.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Workspace} entities.
 * <p>
 * This interface extends {@link JpaRepository} and provides methods for accessing and manipulating workspace data.
 * It allows for basic CRUD operations for the {@link Workspace} entity, such as creating, reading, updating, and deleting workspaces.
 * Additionally, it provides custom query methods for fetching workspaces along with their reservations and for soft-deleting workspaces.
 * </p>
 *
 * <p>
 * The repository includes the following methods:
 * </p>
 *
 * <ul>
 *     <li>{@link #getWorkspaceWithReservations(Long id)} - Fetches a {@link Workspace} entity along with its active reservations (if any).</li>
 *     <li>{@link #deleteById(Long id)} - Marks a workspace as inactive (soft delete) by updating the {@code isActive} field.</li>
 * </ul>
 *
 * <p>
 * The {@link #deleteById(Long)} method is annotated with {@link Modifying} to indicate that it performs
 * an update operation in the database rather than a delete operation. The query modifies the workspace's
 * active status, rather than removing the workspace record from the database.
 * </p>
 *
 * @see Workspace
 * @see JpaRepository
 */
@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    /**
     * Fetches a {@link Workspace} along with its active reservations (if any).
     * The query performs a left join to include all reservations, ensuring that the workspace
     * is retrieved even if it has no active reservations.
     *
     * @param id the ID of the workspace to be retrieved
     * @return the {@link Workspace} entity with its active reservations, or {@code null} if no such workspace exists
     */
    @Query("SELECT w FROM Workspace w LEFT JOIN FETCH w.reservations r WHERE w.workspaceId = :id AND (r.isActive = true OR r IS NULL)")
    Workspace getWorkspaceWithReservations(Long id);

    /**
     * Marks a workspace as inactive (soft delete) by updating the {@code isActive} field.
     *
     * @param id the ID of the workspace to be marked as inactive
     */
    @Modifying
    @Query("UPDATE Workspace w SET w.isActive = false WHERE w.workspaceId = :id")
    @Override
    void deleteById(@Param("id") Long id);

}
