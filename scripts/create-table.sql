-- Create Workspace table
CREATE TABLE workspace
(
    workspace_id SERIAL PRIMARY KEY,
    type         VARCHAR(50)    NOT NULL,
    price        DECIMAL(10, 2) NOT NULL
);

-- Create Reservation table
CREATE TABLE reservation
(
    reservation_id         SERIAL PRIMARY KEY,
    workspace_id           BIGINT          NOT NULL REFERENCES workspace (workspace_id) ON DELETE CASCADE,
    customer_name          VARCHAR(100) NOT NULL,
    start_datetime         TIMESTAMP    NOT NULL,
    end_datetime           TIMESTAMP    NOT NULL,
    reservation_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create User table
CREATE TABLE users
(
    user_id    SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255) NOT NULL
);

