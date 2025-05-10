package com.example.repositories.impl.jdbc;

import com.example.config.JDBConnection;
import com.example.entities.Reservation;
import com.example.entities.Workspace;
import com.example.exceptions.RepositoryException;
import com.example.exceptions.enums.RepositoryErrorCodes;
import com.example.repositories.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link ReservationRepository} using JDBC for interacting with the reservation data in a relational database.
 * Provides methods to perform CRUD operations on reservations, such as adding, retrieving, updating, and deleting reservations.
 */
public class JDBCReservationRepositoryImpl implements ReservationRepository {

    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");
    private final DataSource dataSource;

    /**
     * Constructor initializes the repository with a data source from the {@link JDBConnection} class.
     */
    public JDBCReservationRepositoryImpl() {
        this.dataSource = JDBConnection.getDataSource();
    }

    /**
     * Retrieves all reservations made by a specific customer.
     *
     * @param customerName the name of the customer whose reservations are to be retrieved.
     * @return a list of {@link Reservation} objects for the given customer.
     * @throws RepositoryException if there is an error interacting with the database.
     */
    @Override
    public List<Reservation> getReservationsByCustomer(String customerName) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.reservation_id, r.customer_name, r.start_datetime, r.end_datetime, r.reservation_created_at, " +
                "w.workspace_id, w.type, w.price " +
                "FROM reservation r " +
                "JOIN workspace w ON r.workspace_id = w.workspace_id " +
                "WHERE r.customer_name = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customerName);
            statement.setString(1, customerName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {

                    Workspace workspace = new Workspace(
                            resultSet.getLong("workspace_id"),
                            resultSet.getBigDecimal("price"),
                            resultSet.getString("type")
                    );

                    reservations.add(new Reservation(
                            resultSet.getLong("reservation_id"),
                            workspace, // Now using the Workspace entity instead of just workspaceId
                            resultSet.getString("customer_name"),
                            resultSet.getTimestamp("start_datetime").toLocalDateTime(),
                            resultSet.getTimestamp("end_datetime").toLocalDateTime(),
                            resultSet.getTimestamp("reservation_created_at").toLocalDateTime()
                    ));
                }
                INTERNAL_LOGGER.info("Retrieved {} reservations for customer: {}", reservations.size(), customerName);
            }
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR.getCode(), "Error fetching reservations for customer: {} \nDetails: {}", customerName, e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR, "Failed to retrieve reservations by customer: \n" + e);
        }
        return reservations;
    }

    /**
     * Retrieves all reservations made for a specific workspace.
     *
     * @param workspaceId the ID of the workspace whose reservations are to be retrieved.
     * @return a list of {@link Reservation} objects for the given workspace.
     * @throws RepositoryException if there is an error interacting with the database.
     */
    @Override
    public List<Reservation> findReservationsByWorkspace(Long workspaceId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.reservation_id, r.customer_name, r.start_datetime, r.end_datetime, r.reservation_created_at, " +
                "w.workspace_id, w.type, w.price " +
                "FROM reservation r " +
                "JOIN workspace w ON r.workspace_id = w.workspace_id " +
                "WHERE w.workspace_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, workspaceId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Workspace workspace = new Workspace(
                            resultSet.getLong("workspace_id"),
                            resultSet.getBigDecimal("price"),
                            resultSet.getString("type")
                    );

                    reservations.add(new Reservation(
                            resultSet.getLong("reservation_id"),
                            workspace,
                            resultSet.getString("customer_name"),
                            resultSet.getTimestamp("start_datetime").toLocalDateTime(),
                            resultSet.getTimestamp("end_datetime").toLocalDateTime(),
                            resultSet.getTimestamp("reservation_created_at").toLocalDateTime()
                    ));
                }
                INTERNAL_LOGGER.info("Retrieved {} reservations for workspace ID: {}", reservations.size(), workspaceId);
            }
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR.getCode(), "Error fetching reservations for workspace ID: {} \nDetails: {}", workspaceId, e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR, "Failed to retrieve reservations by Workspace: \n" + e);
        }
        return reservations;
    }

    /**
     * Adds a new reservation to the database.
     *
     * @param entity the reservation to be added.
     * @throws RepositoryException if there is an error while adding the reservation.
     */
    @Override
    public void add(Reservation entity) {
        String sql = "INSERT INTO reservation (workspace_id, customer_name, start_datetime, end_datetime) " +
                "VALUES (?, ?, ?, ?) RETURNING reservation_id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, entity.getWorkspace().getWorkspaceId());
            statement.setString(2, entity.getCustomerName());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getStartDateTime()));
            statement.setTimestamp(4, Timestamp.valueOf(entity.getEndDateTime()));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    entity.setReservationId(resultSet.getLong("reservation_id"));
                    INTERNAL_LOGGER.info("Reservation added successfully with ID: {}", entity.getReservationId());
                }
            }
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_INTEGRITY_VIOLATION.getCode(), "Error adding reservation! \nDetails: {}", e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_INTEGRITY_VIOLATION, "Failed to add reservation: \n" + e);
        }
    }

    /**
     * Retrieves all reservations from the database.
     *
     * @return a list of all {@link Reservation} objects in the database.
     * @throws RepositoryException if there is an error interacting with the database.
     */
    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.reservation_id, r.workspace_id, r.customer_name, " +
                "r.start_datetime, r.end_datetime, r.reservation_created_at, " +
                "w.workspace_id, w.type, w.price " +
                "FROM reservation r " +
                "JOIN workspace w ON r.workspace_id = w.workspace_id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Workspace workspace = new Workspace(
                        resultSet.getLong("workspace_id"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getString("type")
                );

                reservations.add(new Reservation(
                        resultSet.getLong("reservation_id"),
                        workspace,
                        resultSet.getString("customer_name"),
                        resultSet.getTimestamp("start_datetime").toLocalDateTime(),
                        resultSet.getTimestamp("end_datetime").toLocalDateTime(),
                        resultSet.getTimestamp("reservation_created_at").toLocalDateTime()
                ));
            }
            INTERNAL_LOGGER.info("Retrieved {} reservations from database", reservations.size());
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR.getCode(), "Error retrieving reservations! \nDetails: {}", e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR, "Failed to retrieve all reservations: \n" + e);
        }
        return reservations;
    }

    /**
     * Retrieves a reservation by its ID.
     *
     * @param id the ID of the reservation to be retrieved.
     * @return an {@link Optional} containing the reservation if found, or empty if not found.
     * @throws RepositoryException if there is an error interacting with the database.
     */
    @Override
    public Optional<Reservation> getById(Long id) {
        String sql = "SELECT r.reservation_id, r.workspace_id, r.customer_name, " +
                "r.start_datetime, r.end_datetime, r.reservation_created_at, " +
                "w.workspace_id, w.type, w.price " +
                "FROM reservation r " +
                "JOIN workspace w ON r.workspace_id = w.workspace_id " +
                "WHERE r.reservation_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Workspace workspace = new Workspace(
                            resultSet.getLong("workspace_id"),
                            resultSet.getBigDecimal("price"),
                            resultSet.getString("type")
                    );

                    Reservation reservation = new Reservation(
                            resultSet.getLong("reservation_id"),
                            workspace,
                            resultSet.getString("customer_name"),
                            resultSet.getTimestamp("start_datetime").toLocalDateTime(),
                            resultSet.getTimestamp("end_datetime").toLocalDateTime(),
                            resultSet.getTimestamp("reservation_created_at").toLocalDateTime()
                    );
                    INTERNAL_LOGGER.info("Successfully retrieved reservation with ID: {}", id);
                    return Optional.of(reservation);
                }
            }
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR.getCode(), "Error fetching reservation by ID: {} \nDetails: {}", id, e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR, "Failed to retrieve reservation by ID: \n" + e);
        }
        return Optional.empty();
    }

    /**
     * Removes a reservation from the database.
     *
     * @param id the ID of the reservation to be removed.
     * @throws RepositoryException if there is an error while deleting the reservation.
     */
    @Override
    public void remove(Long id) {
        String sql = "DELETE FROM reservation WHERE reservation_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                INTERNAL_LOGGER.info("Reservation with ID {} has been removed", id);
            } else {
                INTERNAL_LOGGER.warn("No reservation found to delete with ID {}", id);
            }
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_REMOVAL_ERROR.getCode(), "Error deleting reservation with ID: {} \nDetails: ", id, e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_REMOVAL_ERROR, "Failed to remove reservation: \n" + e);
        }
    }
}
