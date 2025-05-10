package com.example.repositories.impl.jdbc;

import com.example.config.JDBConnection;
import com.example.entities.Workspace;
import com.example.exceptions.enums.ErrorCodes;
import com.example.repositories.WorkspaceRepository;
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

public class JDBCWorkspaceRepositoryImpl implements WorkspaceRepository {

    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");
    private final DataSource dataSource;

    public JDBCWorkspaceRepositoryImpl() {
        this.dataSource = JDBConnection.getDataSource();
    }

    @Override
    public void add(Workspace entity) {
        String sql = "INSERT INTO workspace (type, price) VALUES (?, ?) RETURNING workspace_id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getType());
            statement.setBigDecimal(2, entity.getPrice());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                entity.setWorkspaceId(resultSet.getLong("workspace_id")); // Retrieve and set generated ID
            }
            INTERNAL_LOGGER.info("Workspace inserted successfully! ID={}, Type='{}', Price={}",
                    resultSet.getLong("workspace_id"), entity.getType(), entity.getPrice());
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(ErrorCodes.DATABASE_ERROR.getCode(), "Failed to insert workspace! Type='{}', Price={}, Error={}",
            entity.getType(), entity.getPrice(), e.getMessage(), e);
        }
    }

    @Override
    public List<Workspace> getAll() {
        List<Workspace> workspaces = new ArrayList<>();
        String sql = "SELECT workspace_id, type, price FROM workspace";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                workspaces.add(new Workspace(
                        resultSet.getLong("workspace_id"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getString("type")

                ));
            }
            INTERNAL_LOGGER.info("Retrieved {} workspaces from database", workspaces.size());
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(ErrorCodes.DATABASE_ERROR.getCode(), "Failed to retrieve workspaces! \nDetails={}", e.getMessage(), e);

        }
        return workspaces;
    }

    @Override
    public Optional<Workspace> getById(Long id) {
        String sql = "SELECT workspace_id, type, price FROM workspace WHERE workspace_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Workspace workspace = new Workspace(
                        resultSet.getLong("workspace_id"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getString("type")
                );
                return Optional.of(workspace);
            }
            INTERNAL_LOGGER.warn("Workspace with ID {} not found", id);
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(ErrorCodes.DATABASE_ERROR.getCode(), "Error fetching workspace by ID! \nDetails={}", e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public void remove(Long id) {
        String sql = "DELETE FROM workspace WHERE workspace_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            if(affectedRows > 0) {
                INTERNAL_LOGGER.info("Workspace with ID {} has been removed", id);
            } else {
                INTERNAL_LOGGER.warn("No workspace found to delete with ID {}", id);
            }
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(ErrorCodes.DATABASE_ERROR.getCode(), "Error deleting workspace! \nDetails={}", e.getMessage(), e);
        }
    }
}
