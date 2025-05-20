package com.example.repositories.impl.jdbc;

import com.example.config.JDBConnection;
import com.example.entities.User;
import com.example.exceptions.RepositoryException;
import com.example.exceptions.enums.NotFoundErrorCodes;
import com.example.exceptions.enums.RepositoryErrorCodes;
import com.example.repositories.UserRepository;
import com.example.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link UserRepository} using JDBC for interacting with the user data in a relational database.
 * Provides methods to perform CRUD operations on users, such as adding, retrieving, updating, and deleting users.
 */
@Repository("jdbcUser")
public class JDBCUserRepositoryImpl implements UserRepository {

    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");
    private final DataSource dataSource;

    public JDBCUserRepositoryImpl() {
        this.dataSource = JDBConnection.getDataSource();
    }

    /**
     * Adds a new user to the database.
     * Passwords are hashed before being stored.
     *
     * @param entity The user to be added.
     * @throws RepositoryException if there is an error while inserting the user.
     */
    @Override
    public void add(User entity) {

        // Hash the password before saving it to the database
        String hashedPassword = PasswordUtils.hashPassword(entity.getPassword());
        entity.setPassword(hashedPassword);

        String sql = "INSERT INTO users (first_name, last_name, email, password) VALUES (?, ?, ?, ?) RETURNING user_id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPassword());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    entity.setUserId(resultSet.getLong("user_id"));
                    INTERNAL_LOGGER.info("User added successfully with ID: {}", entity.getUserId());
                }
            }
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_INTEGRITY_VIOLATION.getCode(), "Failed to create a user! \nDetails: {}", e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_INTEGRITY_VIOLATION, "Failed to create a user: \n" + e);
        }
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all users.
     * @throws RepositoryException if there is an error while fetching the users.
     */
    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("user_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR.getCode(), "Error fetching all users! \nDetails: {}", e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR, "Failed to retrieve all users: \n" + e);
        }

        return users;
    }

    /**
     * Retrieves a user by their unique user ID.
     *
     * @param id The ID of the user to retrieve.
     * @return An Optional containing the user if found, otherwise an empty Optional.
     * @throws RepositoryException if there is an error while fetching the user.
     */
    @Override
    public Optional<User> getById(Long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User(
                            resultSet.getLong("user_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    );
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR.getCode(), "Error fetching user with ID: {} \nDetails: {}", id, e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR, "Failed to retrieve user by ID. \n" + e);
        }

        return Optional.empty();
    }

    /**
     * Removes a user from the database by their user ID.
     *
     * @param id The ID of the user to remove.
     * @throws RepositoryException if there is an error while removing the user.
     */
    @Override
    public void remove(Long id) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                INTERNAL_LOGGER.info("User with ID {} deleted successfully.", id);
            } else {
                INTERNAL_LOGGER.warn(NotFoundErrorCodes.USER_NOT_FOUND.getCode(), "No user found with ID {} to delete.", id);
            }
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_REMOVAL_ERROR.getCode(), "Failed to remove user with ID: {}! \nDetails: {}", id, e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_REMOVAL_ERROR, "Failed to remove user by ID. \n" + e);
        }
    }
}
