INSERT INTO cardiff.location (id, city, country, created_date) VALUES
(1, 'Odessa', 'Ukraine', NOW());

INSERT INTO cardiff.person (id, name, login, password, email, created_date, phone_number, description, deleted, role,
location_id) VALUES
/*password - testpassword1*/
(1, 'Vadim Guliaev', 'vadimguliaev', '$2a$10$aUSL7fit4kJsKWnMlyqthOoDAd2LmfNksRmdBU2jC0NDOaisZ3/Xy',
    'vadimguliaev@gmail.com', NOW(), 568965236, 'First test person', false, 'USER', 1);

INSERT INTO cardiff.discount_card (id, card_number, created_date, company_name,
amount_of_discount, description, deleted, person_id, picked) VALUES
(1, 1, NOW(), 'Cheese', 5,'скидка', false, 1, false);

INSERT INTO discount_card_history (id, picked_date, return_date, discount_card_id, person_id) VALUES
(1, now(), (date(now()) + 7), 1, 1),
(2, (date(now()) - 7), null, 1, 1);