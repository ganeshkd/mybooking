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
