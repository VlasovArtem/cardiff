ALTER TABLE book_card RENAME TO card_booking;
ALTER TABLE card_booking RENAME book_date_start TO booking_start_date;
ALTER TABLE card_booking RENAME book_date_end TO booking_end_date;
ALTER TABLE discount_card ADD COLUMN available boolean;
ALTER TABLE discount_card ALTER available SET DEFAULT true;
UPDATE discount_card  SET available = true;