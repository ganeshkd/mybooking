DROP TABLE IF EXISTS booking CASCADE;
DROP TABLE IF EXISTS show_seat CASCADE;
DROP TABLE IF EXISTS show CASCADE;
DROP TABLE IF EXISTS seat CASCADE;
DROP TABLE IF EXISTS theater CASCADE;
DROP TABLE IF EXISTS movie CASCADE;

-- Create MOVIE table
CREATE TABLE movie (
    movie_id BIGSERIAL PRIMARY KEY,
    movie_name VARCHAR(100) UNIQUE NOT NULL,
    language VARCHAR(25),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
is_active BOOLEAN DEFAULT TRUE

);
-- Create THEATER table
CREATE TABLE theater (
    theater_id BIGSERIAL PRIMARY KEY,
    theater_name VARCHAR(100) UNIQUE NOT NULL,
    location_url VARCHAR(255),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
is_active BOOLEAN DEFAULT TRUE
    
);

-- Create SEAT table (seats belong to theaters)
CREATE TABLE seat (
    seat_id BIGSERIAL PRIMARY KEY,
    seat_name VARCHAR(15) NOT NULL, -- Row + Number A-12
theater_id INTEGER NOT NULL,
    FOREIGN KEY (theater_id) REFERENCES theater(theater_id) ON DELETE RESTRICT,
    UNIQUE(theater_id, seat_name) -- Unique seat per theater
);
-- Create SHOW table
CREATE TABLE show (
    show_id BIGSERIAL PRIMARY KEY,
    show_date DATE NOT NULL,
show_time TIME NOT NULL,
theater_id INTEGER NOT NULL,
movie_id INTEGER NOT NULL,
ticket_price DECIMAL(8,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (theater_id) REFERENCES theater(theater_id) ON DELETE RESTRICT,
FOREIGN KEY (movie_id) REFERENCES movie (movie_id) ON DELETE RESTRICT

);
-- Create SHOW_SEAT table (represents seat availability for specific shows)
CREATE TABLE show_seat (
    show_seat_id BIGSERIAL PRIMARY KEY,
    show_id INTEGER NOT NULL,
    seat_id INTEGER NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    seat_status VARCHAR(20) DEFAULT 'available' CHECK (seat_status IN ('available', 'booked', 'blocked')),
    FOREIGN KEY (show_id) REFERENCES show(show_id) ON DELETE CASCADE,
    FOREIGN KEY (seat_id) REFERENCES seat(seat_id) ON DELETE RESTRICT,
    UNIQUE(show_id, seat_id) -- One record per seat per show
);
-- Create BOOKING table
CREATE TABLE booking (
    booking_id BIGSERIAL PRIMARY KEY,
    booking_date TIMESTAMP NOT NULL,
show_id INTEGER NOT NULL,
    customer_name VARCHAR(100),
    customer_email VARCHAR(100),
    customer_phone VARCHAR(15),
    total_amount DECIMAL(10,2) DEFAULT 0.00,
    booking_status VARCHAR(20) DEFAULT 'confirmed' CHECK (booking_status IN ('confirmed', 'cancelled', 'completed')),
    FOREIGN KEY (show_id) REFERENCES show(show_id) ON DELETE RESTRICT
);
-- Create BOOKING_SEAT table (many-to-many relationship between booking and show_seat)
CREATE TABLE booking_seat (
    booking_seat_id BIGSERIAL PRIMARY KEY,
    booking_id INTEGER NOT NULL,
    show_seat_id INTEGER NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES booking(booking_id) ON DELETE CASCADE,
    FOREIGN KEY (show_seat_id) REFERENCES show_seat(show_seat_id) ON DELETE RESTRICT,
    UNIQUE(booking_id, show_seat_id)
);



-- Create indexes
CREATE INDEX idx_show_date_time ON show(show_date, show_time);
CREATE INDEX idx_show_theater ON show(theater_id);
CREATE INDEX idx_show_movie ON show(movie_id);
CREATE INDEX idx_seat_theater ON seat(theater_id);
CREATE INDEX idx_show_seat_show ON show_seat(show_id);
CREATE INDEX idx_show_seat_availability ON show_seat(is_available);
CREATE INDEX idx_booking_show ON booking(show_id);
CREATE INDEX idx_booking_date ON booking(booking_date);

-- Sample data
INSERT INTO movie (movie_name, language) VALUES
    ('The Dark Knight', 'English'),
    ('Inception', 'English'),
    ('Dangal', 'Hindi'),
    ('3 Idiots', 'Hindi');

INSERT INTO theater (theater_name, location_url) VALUES
    ('PVR Cinemas Forum Mall', 'https://maps.google.com/pvr-forum'),
    ('INOX Garuda Mall', 'https://maps.google.com/inox-garuda'),
    ('Cinepolis Nexus Mall', 'https://maps.google.com/cinepolis-nexus');

-- Insert seats for theater 1 (assuming 50 seats: 5 rows, 10 seats each)
INSERT INTO seat (seat_name, theater_id)
SELECT 
    CONCAT(row_letter, seat_num) as seat_name,
    1 as theater_id
FROM 
    (SELECT CHR(65 + generate_series(0, 4)) as row_letter) rows
    CROSS JOIN 
    (SELECT generate_series(1, 10) as seat_num) seats;

-- Add shows
INSERT INTO show (show_date, show_time, theater_id, movie_id, ticket_price) VALUES
    ('2025-06-05', '10:00:00', 1, 1, 250.00),
    ('2025-06-05', '14:00:00', 1, 1, 300.00),
    ('2025-06-05', '18:00:00', 1, 2, 280.00),
    ('2025-06-06', '10:00:00', 2, 3, 200.00);

-- Create show_seat records for all shows (automatically create availability)
INSERT INTO show_seat (show_id, seat_id, is_available)
SELECT s.show_id, st.seat_id, TRUE
FROM show s
CROSS JOIN seat st
WHERE st.theater_id = s.theater_id;
