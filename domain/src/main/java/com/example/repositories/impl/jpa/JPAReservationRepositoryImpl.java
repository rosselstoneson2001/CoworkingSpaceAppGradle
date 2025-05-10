package com.example.repositories.impl.jpa;

import com.example.entities.Reservation;
import com.example.exceptions.RepositoryException;
import com.example.exceptions.enums.RepositoryErrorCodes;
import com.example.repositories.ReservationRepository;
import com.example.repositories.impl.json.ReservationRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * JPA implementation of the {@link ReservationRepository}.
 * <p>
 * This class interacts with the database using the Java Persistence API (JPA).
 * It provides CRUD operations for {@link Reservation} entities such as adding, removing, and fetching reservations,
 * including queries filtered by customer and workspace.
 * </p>
 */
public class JPAReservationRepositoryImpl implements ReservationRepository {

    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger(ReservationRepositoryImpl.class);
    private EntityManagerFactory emf;
    private EntityManager entityManager;

    /**
     * Default constructor that initializes the {@link EntityManagerFactory} and {@link EntityManager} for JPA.
     */
    public JPAReservationRepositoryImpl() {
        emf = Persistence.createEntityManagerFactory("myJpaUnit");
        entityManager = emf.createEntityManager();
    }

    /**
     * Adds a new reservation to the database.
     * <p>
     * This method persists a new {@link Reservation} entity to the database within a transaction.
     * </p>
     *
     * @param entity the reservation to be added.
     */
    @Override
    public void add(Reservation entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            entityManager.persist(entity);

            transaction.commit();
            INTERNAL_LOGGER.info("Reservation added successfully with ID: {}", entity.getReservationId());
        } catch (Exception e) {
            if (transaction.isActive())
                INTERNAL_LOGGER.error(RepositoryErrorCodes.TRANSACTION_FAILED.getCode(), "Error occurred while attempting to create a reservation: {}. Rolling back transaction.", e);
            transaction.rollback();
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_INTEGRITY_VIOLATION.getCode(), "Error adding reservation! \nDetails: {}", e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_INTEGRITY_VIOLATION, "Failed to add reservation: \n" + e);
        }
    }

    /**
     * Retrieves all reservations from the database.
     * <p>
     * This method fetches all {@link Reservation} entities from the database.
     * </p>
     *
     * @return a list of all reservations.
     */
    @Override
    public List<Reservation> getAll() {
        try {
            List<Reservation> reservations = entityManager.createQuery(
                    "SELECT r FROM reservation r WHERE r.isActive = true", Reservation.class)
                    .getResultList();

            INTERNAL_LOGGER.info("Retrieved {} reservations from database", reservations.size());
            return reservations;
        } catch (Exception e) {
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR.getCode(), "Error retrieving reservations! \nDetails: {}", e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR, "Failed to retrieve all reservations: \n" + e);
        }
    }

    /**
     * Retrieves a reservation by its ID.
     * <p>
     * This method fetches a {@link Reservation} entity based on the provided ID.
     * </p>
     *
     * @param id the ID of the reservation.
     * @return an {@link Optional} containing the reservation if found, or empty if not.
     */
    @Override
    public Optional<Reservation> getById(Long id) {
        try {
            Reservation reservation = entityManager.find(Reservation.class, id);

            if (reservation != null && reservation.isActive()) {
                INTERNAL_LOGGER.info("Reservation found with ID: {}", id);
                return Optional.of(reservation);
            } else {
                INTERNAL_LOGGER.warn("No reservation found with ID: {}", id);
                return Optional.empty();
            }
        } catch (Exception e) {
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR.getCode(), "Error fetching reservation by ID: {} \nDetails: {}", id, e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR, "Failed to retrieve reservation by ID: \n" + e);
        }
    }

    /**
     * Removes a reservation by its ID.
     * <p>
     * This method removes the {@link Reservation} entity from the database if it exists.
     * </p>
     *
     * @param id the ID of the reservation to remove.
     */
    @Override
    public void remove(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            Reservation reservation = entityManager.find(Reservation.class, id);
            if (reservation != null) {
                reservation.setActive(false);
                entityManager.merge(reservation);
                INTERNAL_LOGGER.info("Reservation removed successfully with ID: {}", id);
            } else {
                INTERNAL_LOGGER.warn("Reservation with ID: {} not found, no removal performed", id);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive())
                INTERNAL_LOGGER.error(RepositoryErrorCodes.TRANSACTION_FAILED.getCode(), "Error occurred while attempting to remove reservation with ID: {}. Rolling back transaction.", id, e);
            transaction.rollback();
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_REMOVAL_ERROR.getCode(), "Error removing reservation with ID: {} \nDetails: {}", id, e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_REMOVAL_ERROR, "Failed to remove reservation: \n" + e);
        }
    }

    /**
     * Retrieves reservations made by a specific customer.
     * <p>
     * This method uses JPQL fetches all reservations made by the specified customer from the database.
     * </p>
     *
     * @param customerName the name of the customer whose reservations to retrieve.
     * @return a list of reservations made by the customer.
     */
    @Override
    public List<Reservation> getReservationsByCustomer(String customerName) { //  JPQL
        String jpql = "SELECT r FROM reservation r WHERE r.customerName = :customerName AND r.isActive = true";
        try {
            return entityManager.createQuery(jpql, Reservation.class)
                    .setParameter("customerName", customerName)
                    .getResultList();
        } catch (PersistenceException e) {
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR.getCode(), "Error fetching reservations for customer: {} \nDetails: {}", customerName, e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR, "Failed to retrieve reservations by customer: \n" + e);
        }
    }

    /**
     * Retrieves reservations for a specific workspace.
     * <p>
     * This method uses JPA Criteria API to fetch reservations filtered by workspace ID.
     * </p>
     *
     * @param workspaceId the ID of the workspace whose reservations to retrieve.
     * @return a list of reservations for the specified workspace.
     */
    @Override
    public List<Reservation> findReservationsByWorkspace(Long workspaceId) { // Criteria API
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reservation> criteriaQuery = criteriaBuilder.createQuery(Reservation.class);

        entityManager.getTransaction().begin();

        Root<Reservation> reservationRoot = criteriaQuery.from(Reservation.class);
        Predicate workspacePredicate = criteriaBuilder.equal(reservationRoot.get("workspace"), workspaceId);
        Predicate activePredicate = criteriaBuilder.equal(reservationRoot.get("isActive"), true);
        criteriaQuery.select(reservationRoot).where(workspacePredicate, activePredicate);

        entityManager.getTransaction().commit();
        try {
            return entityManager.createQuery(criteriaQuery).getResultList();
        } catch (PersistenceException e) {
            INTERNAL_LOGGER.error(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR.getCode(), "Error fetching reservations for workspace ID: {} \nDetails: {}", workspaceId, e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_RETRIEVAL_ERROR, "Failed to retrieve reservations by workspace: \n" + e);
        }
    }


}
