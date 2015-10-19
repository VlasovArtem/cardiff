INSERT INTO discount_card_history (id, picked_date, return_date, discount_card_id, person_id) VALUES
(1, now(), (date(now()) + 7), 1, 1),
(2, (date(now()) - 7), null, 1, 1);