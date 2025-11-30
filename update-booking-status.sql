-- Update booking status to PAID for testing
USE eventkita_db;

-- Check current bookings
SELECT id, user_id, event_id, quantity, total_amount, status, created_at 
FROM bookings 
WHERE event_id = 6;

-- Update to PAID
UPDATE bookings 
SET status = 'PAID', paid_at = NOW() 
WHERE event_id = 6 AND status = 'PENDING';

-- Verify
SELECT id, user_id, event_id, quantity, total_amount, status, paid_at 
FROM bookings 
WHERE event_id = 6;

SELECT 'Bookings updated to PAID!' as result;
