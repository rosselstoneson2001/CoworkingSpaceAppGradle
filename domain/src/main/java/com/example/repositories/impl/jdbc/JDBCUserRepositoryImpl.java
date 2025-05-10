package com.example.repositories.impl.jdbc;

import com.example.config.JDBConnection;
import com.example.entities.User;
import com.example.exceptions.enums.ErrorCodes;
import com.example.repositories.UserRepository;
import com.example.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCUserRepositoryImpl implements UserRepository {

    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");
    private final DataSource dataSource;

    public JDBCUserRepositoryImpl() {
        this.dataSource = JDBConnection.getDataSource();
    }

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
            INTERNAL_LOGGER.error(ErrorCodes.INVALID_USER.getCode(), "Error adding user! \nDetails: {}", e.getMessage(), e);
        }
    }

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
            INTERNAL_LOGGER.error("Error fetching all users! \nDetails: {}", e.getMessage(), e);
        }

        return users;
    }

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
            INTERNAL_LOGGER.error("Error fetching user by ID! \nDetails: {}", e.getMessage(), e);
        }

        return Optional.empty();
    }

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
                INTERNAL_LOGGER.warn("No user found with ID {} to delete.", id);
            }
        } catch (SQLException e) {
            INTERNAL_LOGGER.error("Error removing user! \nDetails: {}", e.getMessage(), e);
        }
    }
}
