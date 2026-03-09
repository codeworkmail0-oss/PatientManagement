CREATE TABLE IF NOT EXISTS address (
    id UUID PRIMARY KEY,
    apartment VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    street_name VARCHAR(255),
    street_number VARCHAR(255),
    zip_code VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS patient (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    gender SMALLINT CHECK (gender BETWEEN 0 AND 1),
    address_id UUID NOT NULL UNIQUE REFERENCES address(id),
    registered_date TIMESTAMP(6),
    updated_date TIMESTAMP(6)
    );