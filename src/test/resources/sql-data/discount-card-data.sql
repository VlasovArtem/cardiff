INSERT INTO cardiff.discount_card (id, card_number, created_date, company_name,
amount_of_discount, description, deleted, person_id, picked, location_id) VALUES
(1, 1, NOW(), 'Cheese', 5,'скидка', false, 1, false, 1),
(2, 2, NOW(), 'Test', 5,'скидка', false, 1, true, 1),
(3, 3, NOW(), 'Test2', 5,'скидка', false, 2, true, 1),
(4, 4, NOW(), 'Test3', 5,'скидка', false, 2, false, 1),
(5, 5, NOW(), 'Test4', 5,'скидка', true, 1, false, 1);