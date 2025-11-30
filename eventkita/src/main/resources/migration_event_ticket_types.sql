-- Migration: Add event_ticket_types table for dynamic ticket configuration
-- Date: 2025-11-30
-- Purpose: Allow organizers to select which ticket types (REGULAR, VIP, VVIP) are available per event

CREATE TABLE IF NOT EXISTS event_ticket_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    ticket_type VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    quota INT NOT NULL,
    available_quota INT NOT NULL,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    UNIQUE KEY unique_event_ticket_type (event_id, ticket_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Index untuk query cepat berdasarkan event_id
CREATE INDEX idx_event_ticket_types_event_id ON event_ticket_types(event_id);

-- Sample data untuk testing (optional - bisa di-comment jika tidak perlu)
-- INSERT INTO event_ticket_types (event_id, ticket_type, price, quota, available_quota)
-- SELECT id, 'REGULAR', 50000, 100, 100 FROM events WHERE id = 1;
