-- Create company table
CREATE TABLE if not exists company
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);