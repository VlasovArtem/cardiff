ALTER TABLE discount_card ADD COLUMN location_id BIGINT REFERENCES location (id);
UPDATE discount_card SET location_id = (SELECT location_id FROM person WHERE id = discount_card.person_id);
ALTER TABLE discount_card ALTER location_id SET NOT NULL;