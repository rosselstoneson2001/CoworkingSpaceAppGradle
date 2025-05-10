package com.example.repositories.impl.jdbc;

import com.example.config.JDBConnection;
import com.example.entities.Reservation;
import com.example.exceptions.enums.ErrorCodes;
import com.example.repositories.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCReservationRepositoryImpl implements ReservationRepository {

    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");
    private final DataSource dataSource;

    public JDBCReservationRepositoryImpl() {
        this.dataSource = JDBConnection.getDataSource();
    }

    @Override
    public List<Reservation> getReservationsByCustomer(String customerName) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT reservation_id, workspace_id, customer_name, start_datetime, end_datetime, reservation_created_at " +
                "FROM reservation WHERE customer_name = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    reservations.add(new Reservation(
                            resultSet.getLong("reservation_id"),
                            resultSet.getLong("workspace_id"),
                            resultSet.getString("customer_name"),
                            resultSet.getTimestamp("start_datetime").toLocalDateTime(),
                            resultSet.getTimestamp("end_datetime").toLocalDateTime(),
                            resultSet.getTimestamp("reservation_created_at").toLocalDateTime()
                    ));
                }
                INTERNAL_LOGGER.info("Retrieved {} reservations for customer: {}", reservations.size(), customerName);
            }
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(ErrorCodes.DATABASE_ERROR.getCode(), "Error fetching reservations for customer: {} \nDetails: {}", customerName, e.getMessage(), e);
        }
        return reservations;
    }

    @Override
    public List<Reservation> findReservationsByWorkspace(Long workspaceId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT reservation_id, workspace_id, customer_name, start_datetime, end_datetime, reservation_created_at " +
                "FROM reservation WHERE workspace_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, workspaceId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    reservations.add(new Reservation(
                            resultSet.getLong("reservation_id"),
                            resultSet.getLong("workspace_id"),
                            resultSet.getString("customer_name"),
                            resultSet.getTimestamp("start_datetime").toLocalDateTime(),
                            resultSet.getTimestamp("end_datetime").toLocalDateTime(),
                            resultSet.getTimestamp("reservation_created_at").toLocalDateTime()
                    ));
                }
                INTERNAL_LOGGER.info("Retrieved {} reservations for workspace ID: {}", reservations.size(), workspaceId);
            }
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(ErrorCodes.DATABASE_ERROR.getCode(), "Error fetching reservations for workspace ID: {} \nDetails: {}", workspaceId, e.getMessage(), e);
        }
        return reservations;
    }

    @Override
    public void add(Reservation entity) {
        String sql = "INSERT INTO reservation (workspace_id, customer_name, start_datetime, end_datetime) " +
                "VALUES (?, ?, ?, ?) RETURNING reservation_id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, entity.getWorkspaceId());
            statement.setString(2, entity.getCustomerName());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getStartDateTime()));
            statement.setTimestamp(4, Timestamp.valueOf(entity.getEndDateTime()));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("---" + entity.getReservationId() + entity.getCustomerName() + entity.getWorkspaceId());
                    entity.setReservationId(resultSet.getLong("reservation_id"));
                    INTERNAL_LOGGER.info("Reservation added successfully with ID: {}", entity.getReservationId());
                }
            }
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(ErrorCodes.DATABASE_ERROR.getCode(), "Error adding reservation! \nDetails: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT reservation_id, workspace_id, customer_name, start_datetime, end_datetime, reservation_created_at " +
                "FROM reservation";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                reservations.add(new Reservation(
                        resultSet.getLong("reservation_id"),
                        resultSet.getLong("workspace_id"),
                        resultSet.getString("customer_name"),
                        resultSet.getTimestamp("start_datetime").toLocalDateTime(),
                        resultSet.getTimestamp("end_datetime").toLocalDateTime(),
                        resultSet.getTimestamp("reservation_created_at").toLocalDateTime()
                ));
            }
            INTERNAL_LOGGER.info("Retrieved {} reservations from database", reservations.size());
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(ErrorCodes.DATABASE_ERROR.getCode(), "Error retrieving reservations! \nDetails: {}", e.getMessage(), e);
        }
        return reservations;
    }

    @Override
    public Optional<Reservation> getById(Long id) {
        String sql = "SELECT reservation_id, workspace_id, customer_name, start_datetime, end_datetime, reservation_created_at " +
                "FROM reservation WHERE reservation_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Reservation reservation = new Reservation(
                            resultSet.getLong("reservation_id"),
                            resultSet.getLong("workspace_id"),
                            resultSet.getString("customer_name"),
                            resultSet.getTimestamp("start_datetime").toLocalDateTime(),
                            resultSet.getTimestamp("end_datetime").toLocalDateTime(),
                            resultSet.getTimestamp("reservation_created_at").toLocalDateTime()
                    );
                    return Optional.of(reservation);
                }
            }
            INTERNAL_LOGGER.warn("Reservation with ID {} not found", id);
        } catch (SQLException e) {
            INTERNAL_LOGGER.error(ErrorCodes.DATABASE_ERROR.getCode(), "Error fetching reservation by ID: {} \nDetails: {}", id, e.getMessage(), e);
        }
        return Optional.empty();
    }

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
            INTERNAL_LOGGER.error(ErrorCodes.DATABASE_ERROR.getCode(), "Error deleting reservation with ID: {} \nDetails: ", id, e.getMessage(), e);
        }
    }
}
