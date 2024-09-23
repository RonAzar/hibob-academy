-- Create employees table
CREATE TABLE if not exists employees
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    role       VARCHAR(50) CHECK (role IN ('admin', 'manager', 'employee', 'hr')) NOT NULL,
    company_id INT REFERENCES company (id)
);