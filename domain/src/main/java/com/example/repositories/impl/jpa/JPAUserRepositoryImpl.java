package com.example.repositories.impl.jpa;

import com.example.entities.User;
import com.example.exceptions.RepositoryException;
import com.example.exceptions.enums.NotFoundErrorCodes;
import com.example.exceptions.enums.RepositoryErrorCodes;
import com.example.repositories.UserRepository;
import com.example.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link UserRepository} interface using JPA for persisting User entities.
 * Provides basic CRUD operations for User entities with transaction management.
 *
 * <p>Each method in this class interacts with the EntityManager to perform database operations such as
 * adding, fetching, and removing {@link User} entities.</p>
 *
 * <p>Logging is performed at each operation to track success, failure, and other relevant information.
 * Exceptions are logged and rethrown as {@link RepositoryException} for handling by higher layers of the application.</p>
 */
@Repository("jpaUser")
public class JPAUserRepositoryImpl implements UserRepository {

    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");
    private EntityManagerFactory emf;
    private EntityManager entityManager;

    /**
     * Default constructor that initializes the {@link EntityManagerFactory} and {@link EntityManager} for JPA.
     */
    public JPAUserRepositoryImpl() {
        emf = Persistence.createEntityManagerFactory("myJpaUnit");
        entityManager = emf.createEntityManager();
    }

    /**
     * Adds a new {@link User} entity to the database.
     *
     * @param entity The {@link User} entity to be added.
     * @throws RepositoryException If any error occurs while adding the user to the database.
     */
    @Override
    public void add(User entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {

            transaction.begin();
            String hashedPassword = PasswordUtils.hashPassword(entity.getPassword());
            entity.setPassword(hashedPassword);
            entityManager.persist(entity);

            transaction.commit();
            INTERNAL_LOGGER.info("User added successfully: {}", entity);
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                INTERNAL_LOGGER.error(RepositoryErrorCodes.TRANSACTION_FAILED.getCode(), "Error occurred while attempting to create a user: {}. Rolling back transaction.", e);
                transaction.rollback();
            }
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_INTEGRITY_VIOLATION.getCode(), "Error occurred while adding user: {}", e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_INTEGRITY_VIOLATION, "Failed to add user \n" + e);
        }
    }

    /**
     * Retrieves all {@link User} entities from the database.
     *
     * @return A list of all {@link User} entities in the database.
     * @throws RepositoryException If any error occurs while fetching users from the database.
     */
    @Override
    public List<User> getAll() {
        EntityTransaction transaction = entityManager.getTransaction();
        List<User> users;
        try {
            transaction.begin();

            users = entityManager.createQuery("SELECT u FROM users u WHERE u.isActive = true ", User.class).getResultList();

            transaction.commit();
            INTERNAL_LOGGER.info("Successfully retrieved {} users.", users.size());
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                INTERNAL_LOGGER.error(RepositoryErrorCodes.TRANSACTION_FAILED.getCode(), "Error occurred while attempting to retrieve all users: {}. Rolling back transaction.", e);
                transaction.rollback();
            }
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR.getCode(), "Error occurred while fetching users: {}", e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR,"Failed to retrieve users: \n" + e);
        }
        return users;
    }

    /**
     * Retrieves a {@link User} entity by its ID.
     *
     * @param id The ID of the user to be retrieved.
     * @return An {@link Optional} containing the {@link User} entity if found, or an empty {@link Optional} if not.
     * @throws RepositoryException If any error occurs while retrieving the user.
     */
    @Override
    public Optional<User> getById(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        Optional<User> user;
        try {
            transaction.begin();

            user = Optional.ofNullable(entityManager.createQuery(
                    "SELECT u FROM users u WHERE u.id = :id AND u.isActive = true", User.class)
                    .setParameter("id", id)
                    .getSingleResult());

            transaction.commit();
            if (user.isPresent()) {
                INTERNAL_LOGGER.info("Successfully retrieved user with ID: {}", id);
            } else {
                INTERNAL_LOGGER.warn("No user found with ID: {}", id);
            }
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                INTERNAL_LOGGER.error(RepositoryErrorCodes.TRANSACTION_FAILED.getCode(), "Error occurred while attempting to get user with ID: {}. Rolling back transaction.", id, e);
                transaction.rollback();
            }
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR.getCode(), "Error occurred while retrieving user by ID: {}", id, e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR, "Failed to retrieve user by ID: " + e);
        }
        return user;
    }

    /**
     * Removes a {@link User} entity from the database by its ID.
     *
     * @param id The ID of the user to be removed.
     * @throws RepositoryException If any error occurs while removing the user from the database.
     */
    @Override
    public void remove(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            User user = entityManager.find(User.class, id);
            if (user != null) {
                user.setActive(false);
                entityManager.merge(user);

                transaction.commit();
                INTERNAL_LOGGER.info("Successfully removed user with ID: {}", id);
            } else {
                INTERNAL_LOGGER.warn(NotFoundErrorCodes.USER_NOT_FOUND.getCode(), "No user found with ID: {}", id);
                transaction.rollback();
            }
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                INTERNAL_LOGGER.error(RepositoryErrorCodes.TRANSACTION_FAILED.getCode(), "Error occurred while attempting to remove user with ID: {}. Rolling back transaction.", id, e);
                transaction.rollback();
            }
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_REMOVAL_ERROR.getCode(), "Error occurred while removing user with ID: {}", id, e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_REMOVAL_ERROR, "Failed to remove user: \n" + e);
        }
    }
}