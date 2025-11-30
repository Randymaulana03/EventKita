-- Add paymentCode column to payments table
-- Run this manually if database already exists

ALTER TABLE payments ADD COLUMN payment_code VARCHAR(255) UNIQUE;

-- Verify column added
-- SELECT * FROM payments;
