INSERT INTO person (id, name, login, password, skype, email, created_date, phone_number, description, deleted,
role, location_id) VALUES
(1, 'Vadim Guliaev', 'vadimguliaev', '$2a$10$aUSL7fit4kJsKWnMlyqthOoDAd2LmfNksRmdBU2jC0NDOaisZ3/Xy', 'testskype1',
    'vadimguliaev@gmail.com', NOW(), 568965236, 'First test person', false, 'USER', 1), /*password - testpassword1*/
(2, 'Dmitriy Valnov', 'dmitriyvalnov', '$2a$10$Q8BBRd7rMrrKLC3iqHAGuOPU0OjO.XsiHkfDEXsHi1I0ZFmC08XNC', 'testskype2',
    'dmitriyvalnov@gmail.com', NOW(), 632563263, 'Second test person', false, 'ADMIN', 1), /*password - testpassword2*/
(3, 'Alexandr Mahnov', 'alexandrmahnov', '$2a$10$fkpzjbKXcJj75yVgC5Cbw.VFugVM7inDvm3mw.V71V/NA8qWUPSXe', 'testskype3',
    'alexandrmahnov@gmail.com', NOW(), 563256989, 'Third test person', true, 'USER', 1); /*password - testpassword3*/