INSERT INTO cardiff.location (id, city, country, created_date) VALUES
(1, 'Odessa', 'Ukraine', NOW());

INSERT INTO cardiff.person (id, name, login, password, email, created_date, phone_number, description, deleted, role,
location_id) VALUES
/*password - testpassword1*/
(1, 'Vadim Guliaev', 'vadimguliaev', '$2a$10$aUSL7fit4kJsKWnMlyqthOoDAd2LmfNksRmdBU2jC0NDOaisZ3/Xy',
    'vadimguliaev@gmail.com', NOW(), 568965236, 'First test person', false, 'USER', 1),
(2, 'Dmitriy Valnov', 'dmitriyvalnov', '$2a$10$Q8BBRd7rMrrKLC3iqHAGuOPU0OjO.XsiHkfDEXsHi1I0ZFmC08XNC',
    'dmitriyvalnov@gmail.com', NOW(), 632563263, 'Second test person', false, 'ADMIN', 1);

insert into cardiff.custom_tag (id, tag, created_date, description, author_id) values
(1, 'test', NOW(), 'test description', 1);

insert into tag(id, tag) values
(1, 'exist tag');