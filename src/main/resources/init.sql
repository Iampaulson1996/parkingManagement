/**
 * SQL-скрипт для инициализации схемы базы данных системы управления парковкой.
 */
CREATE TABLE parking_lot (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(100) NOT NULL,
                             address VARCHAR(255) NOT NULL,
                             capacity INTEGER NOT NULL CHECK (capacity > 0),
                             CONSTRAINT unique_name_address UNIQUE (name, address)
);

CREATE TABLE parking_space (
                               id SERIAL PRIMARY KEY,
                               parking_lot_id INTEGER NOT NULL REFERENCES parking_lot(id) ON DELETE CASCADE,
                               space_number VARCHAR(10) NOT NULL,
                               type VARCHAR(20) NOT NULL CHECK (type IN ('REGULAR', 'DISABLED', 'VIP')),
                               CONSTRAINT unique_space_number_per_lot UNIQUE (parking_lot_id, space_number)
);

CREATE TABLE client (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        phone VARCHAR(20),
                        email VARCHAR(100),
                        CONSTRAINT unique_email UNIQUE (email)
);

CREATE TABLE vehicle (
                         id SERIAL PRIMARY KEY,
                         client_id INTEGER NOT NULL REFERENCES client(id) ON DELETE CASCADE,
                         license_plate VARCHAR(20) NOT NULL,
                         brand VARCHAR(50),
                         model VARCHAR(50),
                         CONSTRAINT unique_license_plate UNIQUE (license_plate)
);

CREATE TABLE parking_record (
                                id SERIAL PRIMARY KEY,
                                parking_space_id INTEGER NOT NULL REFERENCES parking_space(id) ON DELETE CASCADE,
                                vehicle_id INTEGER NOT NULL REFERENCES vehicle(id) ON DELETE CASCADE,
                                client_id INTEGER NOT NULL REFERENCES client(id) ON DELETE CASCADE,
                                entry_time TIMESTAMP NOT NULL,
                                exit_time TIMESTAMP,
                                CHECK (exit_time IS NULL OR exit_time > entry_time)
);