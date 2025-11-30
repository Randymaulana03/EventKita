-- Quick setup: Create table and add sample ticket types for existing events
-- Execute this in MySQL Workbench or command line

USE eventkita_db;

-- Create table
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

CREATE INDEX idx_event_ticket_types_event_id ON event_ticket_types(event_id);

-- Add sample ticket types for existing events
-- Event ID 6 (adjust based on your data)
INSERT IGNORE INTO event_ticket_types (evenat_id, ticket_type, price, quota, available_quota)
VALUES 
(6, 'REGULAR', 50000, 100, 100),
(6, 'VIP', 150000, 50, 50),
(6, 'VVIP', 300000, 20, 20);

-- Jika ada event lain, tambahkan di sini
-- INSERT IGNORE INTO event_ticket_types (event_id, ticket_type, price, quota, available_quota)
-- VALUES (7, 'REGULAR', 75000, 200, 200);

SELECT 'Migration completed!' as status;
SELECT * FROM event_ticket_types;
