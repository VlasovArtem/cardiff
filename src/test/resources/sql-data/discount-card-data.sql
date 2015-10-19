INSERT INTO cardiff.discount_card (id, card_number, created_date, company_name,
amount_of_discount, description, deleted, person_id, picked) VALUES
(1, 1, NOW(), 'Cheese', 5,'скидка', false, 1, false),
(2, 2, NOW(), 'Test', 5,'скидка', false, 1, true),
(3, 3, NOW(), 'Test2', 5,'скидка', false, 2, true),
(4, 4, NOW(), 'Test3', 5,'скидка', false, 2, false),
(5, 5, NOW(), 'Test4', 5,'скидка', false, 1, false);