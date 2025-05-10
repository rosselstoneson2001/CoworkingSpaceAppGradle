package com.example.services;

import com.example.entities.User;

/**
 * Service interface for managing user-related operations.
 * <p>
 * Extends the {@link CrudService} interface to provide basic CRUD operations for users,
 * and adds additional methods specific to user management, such as password checking.
 * </p>
 */
public interface UserService extends CrudService<User, Long> {

    boolean checkPassword(Long userId, String plainPassword);

}