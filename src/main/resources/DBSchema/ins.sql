INSERT INTO cardiff.location (id, city, country, created_date) VALUES
(1, 'Odessa', 'Ukraine', NOW()),
(2, 'Kazan', 'Russia', NOW()),
(3, 'Palo Alto', 'United States', NOW()),
(4, 'Burgas', 'Bulgaria', NOW());

INSERT INTO cardiff.person (id, name, login, password, email, created_date, phone_number, description, deleted, role,
location_id) VALUES
(1, 'Gordon Trick','chuhcha', 'dfasf','chuhcha@mail.ru', NOW(),698745232,'Likes to eat', false, 'USER', 1),
(2, 'Symson Drul','gena', 'trer','gena@mail.ru', NOW(),675258565,'Likes to music', false, 'USER', 1),
(3, 'Vadimiy Vadimiyavich','putin', 'yt15GThg','putinka@gmail.rcom', NOW(),563255696,'Likes to erotic', false, 'USER',
 3),
(4, 'Pordon Pordonovich','gore', 'OtUma','roremica@mail.ru', NOW(),786056065,'Likes to mobilephones', false, 'USER', 2),
(5, 'Butoterbrod Dubovkiy','buter', 'dub','bub@mail.ru', NOW(),623211232,'Likes to cars', false, 'USER', 4),
(6, 'Rostick Tostick','bogema', 'oLubvi','boge@ukr.net', NOW(),698745742,'Newby', false, 'ADMIN', 2),
(7, 'Chick Reshetkin','recheto', 'utu','tutu@mail.ru', NOW(),698745221,'Newby', false, 'ADMIN', 4),
(8, 'GTR Trol','trol', 'gtr','GTRtrol@mail.ru', NOW(),696545232,'Newby', true, 'ADMIN', 1),
(9, 'Blonda Blond','tymophei', 'iytew','blonda@mail.ru', NOW(),698825232,'Beauty', false, 'ADMIN', 4),
(10, 'Yric Chmirik','Chmir', 'dfasf','chmir@mail.ru', NOW(),696660000,'Newby', true, 'ADMIN', 3);

INSERT INTO cardiff.tag (id, tag) VALUES
(1, 'cheese'),
(2, 'сыр'),
(3, 'erotic'),
(4, 'music'),
(5, 'musictoos'),
(6, 'bread'),
(7, 'pizza'),
(8, 'mobilephone'),
(9, 'car'),
(10, 'repair'),
(11, 'films'),
(12, 'books');


INSERT INTO cardiff.discount_card (id, card_number, created_date, company_name,
amount_of_discount, description, deleted, person_id, picked) VALUES
(1, 1, NOW(), 'Cheese', 5,'скидка', false, 1, false),
(2, 445, NOW(), 'Kiss', 7,'скидка', false, 3, false),
(3, 11, NOW(), 'MusicTools', 8,'скидка', true, 2, false),
(4, 2, NOW(), 'DjStar', 4,'скидка', false, 2, false),
(5, 2145000, NOW(), 'Bread', 9,'скидка', false, 1, false),
(6, 123, NOW(), 'PizzaStar', 10,'скидка', false, 1, false),
(7, 4578, NOW(), 'PizzaCheap', 5,'скидка', false, 1, false),
(8, 145876421, NOW(), 'Mobilochka', 3,'скидка', false, 2, false),
(9, 4751, NOW(), 'Tavriya', 3,'скидка', false, 1, true),
(10, 54576, NOW(), 'MotorStar', 6,'скидка', false, 2, true),
(11, 55555555, NOW(), 'STO', 5,'скидка', true, 2, true);

INSERT INTO cardiff.tag_card (discount_card_id, tag_id) VALUES
(1,1),
(1,2),
(2,3),
(2,11),
(2,12),
(3,4),
(3,5),
(4,4),
(5,6),
(6,7),
(7,7),
(8,8),
(9,1),
(9,2),
(9,6),
(9,11),
(9,12),
(10,9),
(11,9),
(10,11);

INSERT INTO cardiff.card_booking (id, booking_start_date, booking_end_date, discount_card_id, person_id) VALUES
(1, '06/10/2015','06/12/2015',2,9),
(2, '01/01/2010','06/10/2015',2,8),
(3, '08/20/2015','08/23/2015',6,3),
(4, '08/23/2015','08/25/2015',6,4),
(5, '08/25/2015','08/27/2015',2,5),
(6, '09/01/2015','09/02/2015',11,9),
(7, '01/02/2016','01/08/2016',10,3),
(8, '08/20/2015','01/09/2015',7,4),
(9, '08/21/2015','01/10/2015',9,9),
(10, '08/30/2015','01/10/2015',3,9);


INSERT INTO cardiff.discount_card_comment (id, comment_text, comment_date, discount_card_id, person_id) VALUES
(1, 'They sell fake phones','08/02/2015',7,1),
(2, 'Cruel store','02/20/2014',9,2),
(3, 'Good shop','08/15/2015',3,2),
(4, 'Best assortment','09/12/2014', 2,3),
(5, 'High prices','08/15/2015',10,4),
(6, 'Delicious','08/16/2015',6,9),
(7, 'Its not so cheap','07/18/2015',7,7),
(8, 'it good', '08/14/2015',7,6),
(9, 'They robbers','08/16/2015',1,8),
(10, 'there are beautiful assistans','08/20/2015',4,10);


INSERT INTO cardiff.discount_card_history (id, picked_date, return_date, discount_card_id, person_id) VALUES
(1, '06/10/2015','06/12/2015',2,9),
(2, '01/01/2010','06/10/2014',2,8),
(3, '08/20/2014','08/23/2014',6,3),
(4, '08/23/2014','08/25/2014',6,4),
(5, '08/25/2014','08/27/2014',2,5),
(6, '09/01/2014','09/02/2014',11,9),
(7, '01/02/2014','01/08/2014',10,3),
(8, '08/20/2014','09/01/2014',7,4),
(9, '08/21/2014','10/01/2014',9,9),
(10, '08/30/2014','10/01/2014',3,9);

































