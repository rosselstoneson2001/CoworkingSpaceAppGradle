package com.example.repositories;

import com.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * This interface extends {@link JpaRepository} and provides methods for accessing and manipulating user data.
 * It allows for basic CRUD operations for the {@link User} entity, such as creating, reading, updating, and deleting users.
 * Additionally, it provides custom query methods for updating the status of a user.
 * </p>
 *
 * <p>
 * The repository includes the following methods:
 * </p>
 *
 * <ul>
 *     <li>{@link #deleteById(Long id)} - Marks a user as inactive (soft delete) by updating the {@code isActive} field.</li>
 * </ul>
 *
 * <p>
 * The {@link #deleteById(Long)} method is annotated with {@link Modifying} to indicate that it performs
 * an update operation in the database rather than a delete operation. The query modifies the user's
 * active status, rather than removing the user record from the database.
 * </p>
 *
 * @see User
 * @see JpaRepository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Marks a user as inactive (soft delete) by updating the {@code isActive} field.
     *
     * @param id the ID of the user to be marked as inactive
     */
    @Modifying
    @Query("UPDATE users u SET u.isActive = false WHERE u.userId = :id")
    @Override
    void deleteById(@Param("id") Long id);

}
