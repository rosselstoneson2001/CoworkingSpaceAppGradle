package com.example.domain.repositories;

import com.example.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations and custom queries
 * for managing user records in the database.
 * </p>
 *
 * <p>
 * This repository includes:
 * </p>
 * <ul>
 *     <li>{@link #deleteById(Long)}: Soft-deletes a user by setting {@code isActive} to {@code false}.</li>
 *     <li>{@link #findByEmail(String)}: Finds a user by their email address.</li>
 * </ul>
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

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user
     * @return the user with the given email, or {@code null} if not found
     */
    User findByEmail(String email);

}
