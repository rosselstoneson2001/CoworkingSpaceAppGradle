package com.example.repositories;

import com.example.entities.User;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * This interface provides methods for accessing and manipulating user data.
 * It extends the {@link CrudRepository} to inherit basic CRUD operations
 * for the {@link User} entity, such as creating, reading, updating, and deleting users.
 * </p>
 */
public interface UserRepository extends CrudRepository<User, Long> {

}
