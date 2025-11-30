USE eventkita_db;

-- Check payments table columns
DESC payments;

-- Check bookings table columns  
DESC bookings;

-- Check sample data
SELECT id, payment_code FROM payments LIMIT 5;
SELECT id, qr_code, status FROM bookings LIMIT 5;
