
--  <-------------------- Workspaces ----------------------->

-- Insert sample data into workspace table
INSERT INTO workspace (type, price)
VALUES
    ('Office', 1500.00),
    ('Meeting Room', 500.00),
    ('Private Desk', 800.00);

-- Get all workspaces from the workspace table
SELECT workspace_id, type, price FROM workspace;

-- Get a workspace by workspace_id
SELECT workspace_id, type, price
FROM workspace
WHERE workspace_id = ?;

-- Delete a workspace by workspace_id
DELETE FROM workspace
WHERE workspace_id = ?;

--  <-------------------- Reservations ----------------------->

-- Insert sample data into reservation table
INSERT INTO reservation (workspace_id, customer_name, start_datetime, end_datetime)
VALUES
    (1, 'John Doe', '2025-03-01 09:00:00', '2025-03-01 17:00:00'),
    (2, 'Jane Smith', '2025-03-02 10:00:00', '2025-03-02 14:00:00');

-- Get reservations by customer name
SELECT reservation_id, workspace_id, customer_name, start_datetime, end_datetime, reservation_created_at
FROM reservation
WHERE customer_name = ?;

-- Get reservations by workspace ID
SELECT reservation_id, workspace_id, customer_name, start_datetime, end_datetime, reservation_created_at
FROM reservation
WHERE workspace_id = ?;


-- Get all reservations
SELECT reservation_id, workspace_id, customer_name, start_datetime, end_datetime, reservation_created_at
FROM reservation;

-- Get reservation by ID
SELECT reservation_id, workspace_id, customer_name, start_datetime, end_datetime, reservation_created_at
FROM reservation
WHERE reservation_id = ?;

-- Delete reservation by ID
DELETE FROM reservation
WHERE reservation_id = ?;


--  <-------------------- Users ----------------------->

-- Insert sample user data
INSERT INTO "user" (first_name, last_name, email, password)
VALUES
    ('John', 'Doe', 'john.doe@example.com', '$2a$12$C.DQ.exLzhkfza4EQa6bvOycq.YhhvpG01ZkZW0cZx6zLz.TjtKnW'),
    ('Jane', 'Smith', 'jane.smith@example.com', '$2a$12$/8x9c8Uup6uUD5wUJ8Osl.7mAjY2U3uHvX9GPUCnSnFjAylCAUDmK'),
    ('Mike', 'Johnson', 'mike.johnson@example.com', '$2a$12$aSg7KSKRkfIlO.j.Wg0NTOlnGtFsBmDZKUpCBkIUewQsqRuwA4vFq');

-- Get all users
SELECT * FROM "user";

-- Get user by ID
SELECT * FROM "user" WHERE user_id = ?;

-- Delete user by ID
DELETE FROM "user" WHERE user_id = ?;
