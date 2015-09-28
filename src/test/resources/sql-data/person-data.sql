INSERT INTO cardiff.location (id, city, country, created_date) VALUES
(1, 'Odessa', 'Ukraine', NOW());

INSERT INTO cardiff.person (id, name, login, password, email, created_date, phone_number, description, deleted, role,
location_id) VALUES
(1, 'Vadim Guliaev', 'vadimguliaev', '$2a$10$aUSL7fit4kJsKWnMlyqthOoDAd2LmfNksRmdBU2jC0NDOaisZ3/Xy',
    'vadimguliaev@gmail.com', NOW(), 568965236, 'First test person', false, 'USER', 1), /*password - testpassword1*/
(2, 'Dmitriy Valnov', 'dmitriyvalnov', '$2a$10$Q8BBRd7rMrrKLC3iqHAGuOPU0OjO.XsiHkfDEXsHi1I0ZFmC08XNC',
    'dmitriyvalnov@gmail.com', NOW(), 632563263, 'Second test person', false, 'ADMIN', 1), /*password - testpassword2*/
(3, 'Alexandr Mahnov', 'alexandrmahnov', '$2a$10$fkpzjbKXcJj75yVgC5Cbw.VFugVM7inDvm3mw.V71V/NA8qWUPSXe',
    'alexandrmahnov@gmail.com', NOW(), 563256989, 'Third test person', true, 'USER', 1); /*password - testpassword3*/

INSERT INTO cardiff.discount_card (id, card_number, created_date, company_name,
amount_of_discount, description, deleted, person_id, picked) VALUES
(1, 1, NOW(), 'Cheese', 5,'скидка', false, 2, false);