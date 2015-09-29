INSERT INTO cardiff.location (id, city, country, created_date) VALUES
(1, 'Odessa', 'Ukraine', NOW());

INSERT INTO cardiff.person (id, name, login, password, email, created_date, phone_number, description, deleted, role,
location_id) VALUES
/*password - testpassword1*/
(1, 'Vadim Guliaev', 'vadimguliaev', '$2a$10$aUSL7fit4kJsKWnMlyqthOoDAd2LmfNksRmdBU2jC0NDOaisZ3/Xy',
    'vadimguliaev@gmail.com', NOW(), 568965236, 'First test person', false, 'USER', 1),
(2, 'Dmitriy Valnov', 'dmitriyvalnov', '$2a$10$Q8BBRd7rMrrKLC3iqHAGuOPU0OjO.XsiHkfDEXsHi1I0ZFmC08XNC',
    'dmitriyvalnov@gmail.com', NOW(), 632563263, 'Second test person', false, 'ADMIN', 1); /*password - testpassword2*/;

INSERT INTO cardiff.discount_card (id, card_number, created_date, company_name,
amount_of_discount, description, deleted, person_id, picked) VALUES
(1, 1, NOW(), 'Cheese', 5,'скидка', false, 1, false),
(2, 2, NOW(), 'Test', 5,'скидка', false, 1, true),
(3, 3, NOW(), 'Test2', 5,'скидка', false, 2, true);

INSERT INTO cardiff.card_booking (id, booking_start_date, booking_end_date, discount_card_id, person_id) VALUES
(1, '10/25/2015', '11/01/2015', 3, 1),
(2, '10/25/2015', '11/01/2015', 1, 2);