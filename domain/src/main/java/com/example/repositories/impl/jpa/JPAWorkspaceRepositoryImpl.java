package com.example.repositories.impl.jpa;

import com.example.entities.Reservation;
import com.example.entities.Workspace;
import com.example.exceptions.RepositoryException;
import com.example.exceptions.WorkspaceNotFoundException;
import com.example.exceptions.enums.NotFoundErrorCodes;
import com.example.exceptions.enums.RepositoryErrorCodes;
import com.example.repositories.WorkspaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link WorkspaceRepository} interface using JPA for persisting Workspace entities.
 * Provides basic CRUD operations for Workspace entities with transaction management.
 *
 * <p>Each method in this class interacts with the EntityManager to perform database operations such as
 * adding, fetching, and removing {@link Workspace} entities.</p>
 *
 * <p>Logging is performed at each operation to track success, failure, and other relevant information.
 * Exceptions are logged and rethrown as {@link RepositoryException} or {@link WorkspaceNotFoundException} for handling by higher layers of the application.</p>
 */
@Repository("jpaWorkspace")
public class JPAWorkspaceRepositoryImpl implements WorkspaceRepository {

    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");
    private EntityManagerFactory emf;
    private EntityManager entityManager;

    /**
     * Default constructor that initializes the {@link EntityManagerFactory} and {@link EntityManager} for JPA.
     */
    public JPAWorkspaceRepositoryImpl() {
        emf = Persistence.createEntityManagerFactory("myJpaUnit");
        entityManager = emf.createEntityManager();
    }

    /**
     * Adds a new {@link Workspace} entity to the database.
     *
     * @param entity The {@link Workspace} entity to be added.
     * @throws RepositoryException If any error occurs while adding the workspace to the database.
     */
    public void add(Workspace entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            entityManager.persist(entity);

            transaction.commit();
            INTERNAL_LOGGER.info("Workspace added successfully: {}", entity);
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                INTERNAL_LOGGER.error(RepositoryErrorCodes.TRANSACTION_FAILED.getCode(), "Error occurred while attempting to create a workspace. Rolling back transaction.", e);
                transaction.rollback();
            }
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_INTEGRITY_VIOLATION.getCode(), "Error occurred while adding workspace: {}", e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_INTEGRITY_VIOLATION, "Failed to add workspace: \n" + e);
        }
    }

    /**
     * Retrieves all {@link Workspace} entities from the database.
     *
     * @return A list of all {@link Workspace} entities in the database.
     * @throws RepositoryException If any error occurs while fetching workspaces from the database.
     */
    @Override
    public List<Workspace> getAll() {
        try {
            List<Workspace> workspaces = entityManager.createQuery(
                            "SELECT w FROM Workspace w WHERE w.isActive = true", Workspace.class)
                    .getResultList();

            INTERNAL_LOGGER.info("Successfully retrieved {} workspaces.", workspaces.size());
            return workspaces;
        } catch (RuntimeException e) {
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR.getCode(), "Error occurred while fetching all workspaces: {}", e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR, "Failed to retrieve all workspaces: \n" + e);
        }
    }

    /**
     * Retrieves a {@link Workspace} entity by its ID.
     *
     * @param id The ID of the workspace to be retrieved.
     * @return An {@link Optional} containing the {@link Workspace} entity if found, or an empty {@link Optional} if not.
     * @throws RepositoryException If any error occurs while retrieving the workspace.
     */
    @Override
    public Optional<Workspace> getById(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        Optional<Workspace> workspace;
        try {
            transaction.begin();

            workspace = Optional.ofNullable(entityManager.createQuery(
                            "SELECT w FROM Workspace w WHERE w.id = :id AND w.isActive = true", Workspace.class)
                    .setParameter("id", id)
                    .getSingleResult());

            transaction.commit();
            if (workspace.isPresent()) {
                INTERNAL_LOGGER.info("Successfully retrieved workspace with ID: {}", id);
            } else {
                INTERNAL_LOGGER.warn(NotFoundErrorCodes.WORKSPACE_NOT_FOUND.getCode(), "No workspace found with ID: {}", id);
            }
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                INTERNAL_LOGGER.error(RepositoryErrorCodes.TRANSACTION_FAILED.getCode(), "Error occurred while attempting to retrieve reservation with ID: {}. Rolling back transaction.", id, e);
                transaction.rollback();
            }
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR.getCode(), "Error occurred while retrieving workspace by ID: {}", id, e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR, "Failed to retrieve workspace by ID: \n" + e);
        }
        return workspace;
    }

    /**
     * Retrieves an active workspace along with its associated reservations using JPQL.
     *
     * <p>This method fetches a {@link Workspace} entity with its reservations
     * using a JPQL query with a LEFT JOIN FETCH to avoid lazy-loading issues.</p>
     *
     * <p>If no active workspace is found for the given ID, a {@link WorkspaceNotFoundException} is thrown.
     * If an unexpected error occurs, a {@link RepositoryException} is thrown.</p>
     *
     * @param id The ID of the workspace to retrieve.
     * @return The workspace with its reservations if found.
     * @throws WorkspaceNotFoundException if no active workspace is found for the given ID.
     * @throws RepositoryException if an unexpected error occurs while fetching data.
     */
    @Override
    public Workspace getWorkspaceWithReservations(Long id) {
        try {

            return entityManager.createQuery(
                            "SELECT DISTINCT w FROM Workspace w LEFT JOIN FETCH w.reservations r " +
                                    "WHERE w.id = :id AND w.isActive = true AND r.isActive = true OR r IS NULL", Workspace.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            INTERNAL_LOGGER.warn("No workspace found with ID: {}", id);
            throw new WorkspaceNotFoundException(NotFoundErrorCodes.WORKSPACE_NOT_FOUND, "No active workspace found with ID:  \n Details: {}" + id);
        } catch (Exception e) {
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR.getCode(), "Error fetching workspace with reservations for ID: {}. Exception: {}", id, e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR, "An error occurred while fetching workspace with reservations.");
        }
    }

    /**
     * Removes a {@link Workspace} entity from the database by its ID.
     *
     * @param id The ID of the workspace to be removed.
     * @throws WorkspaceNotFoundException If no workspace is found with the given ID.
     * @throws RepositoryException        If any error occurs while removing the workspace from the database.
     */
    @Override
    public void remove(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            Workspace workspace = entityManager.find(Workspace.class, id);
            if (workspace != null && workspace.isActive()) {
                // Deactivate the workspace
                workspace.setActive(false);
                entityManager.merge(workspace);

                // Deactivate all related reservations
                for (Reservation reservation : workspace.getReservations()) {
                    reservation.setActive(false);
                    entityManager.merge(reservation);
                }
                transaction.commit();
                INTERNAL_LOGGER.info("Successfully removed workspace with ID: {}", id);
            } else {
                INTERNAL_LOGGER.warn(NotFoundErrorCodes.WORKSPACE_NOT_FOUND.getCode(), "No workspace found with ID: {}", id);
                transaction.rollback();
                throw new WorkspaceNotFoundException(NotFoundErrorCodes.WORKSPACE_NOT_FOUND, "Failed to delete workspace. Workspace not found with ID: " + id);
            }
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                INTERNAL_LOGGER.error(RepositoryErrorCodes.TRANSACTION_FAILED.getCode(), "Error occurred while attempting to remove a reservation with ID: {}. Rolling back transaction.", id, e);
                transaction.rollback();
            }
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_REMOVAL_ERROR.getCode(), "Error occurred while removing workspace with ID: {}", id, e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_REMOVAL_ERROR, "Failed to remove workspace: \n" + e);
        }
    }
}