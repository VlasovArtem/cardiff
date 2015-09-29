ALTER TABLE book_card RENAME TO card_booking;
ALTER TABLE card_booking RENAME book_date_start TO booking_start_date;
ALTER TABLE card_booking RENAME book_date_end TO booking_end_date;
ALTER TABLE discount_card ADD COLUMN picked boolean;
ALTER TABLE discount_card ALTER picked SET DEFAULT false;
UPDATE discount_card  SET picked = false;
ALTER TABLE card_booking ALTER booking_start_date TYPE DATE;
ALTER TABLE card_booking ALTER booking_end_date TYPE DATE;
ALTER TABLE discount_card_history ALTER return_date DROP NOT NULL;