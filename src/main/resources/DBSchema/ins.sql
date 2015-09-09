INSERT INTO cardiff.location (id, city, country, created_date) VALUES
(1, 'Odessa', 'Ukraine', NOW()),
(2, 'Kazan', 'Russia', NOW()),
(3, 'Palo Alto', 'United States', NOW()),
(4, 'Burgas', 'Bulgaria', NOW());

INSERT INTO cardiff.person (name, login, password, email, created_date, phone_number, description, deleted, role,
location_id) VALUES
('Gordon Trick','chuhcha', 'dfasf','chuhcha@mail.ru', NOW(),698745232,'Likes to eat', false, 'USER', 1),
('Symson Drul','gena', 'trer','gena@mail.ru', NOW(),675258565,'Likes to music', false, 'USER', 1),
('Vadimiy Vadimiyavich','putin', 'yt15GThg','putinka@gmail.rcom', NOW(),563255696,'Likes to erotic', false, 'USER', 3),
('Pordon Pordonovich','gore', 'OtUma','roremica@mail.ru', NOW(),786056065,'Likes to mobilephones', false, 'USER', 2),
('Butoterbrod Dubovkiy','buter', 'dub','bub@mail.ru', NOW(),623211232,'Likes to cars', false, 'USER', 4),
('Rostick Tostick','bogema', 'oLubvi','boge@ukr.net', NOW(),698745742,'Newby', false, 'ADMIN', 2),
('Chick Reshetkin','recheto', 'utu','tutu@mail.ru', NOW(),698745221,'Newby', false, 'ADMIN', 4),
('GTR Trol','trol', 'gtr','GTRtrol@mail.ru', NOW(),696545232,'Newby', true, 'ADMIN', 1),
('Blonda Blond','tymophei', 'iytew','blonda@mail.ru', NOW(),698825232,'Beauty', false, 'ADMIN', 4),
('Yric Chmirik','Chmir', 'dfasf','chmir@mail.ru', NOW(),696660000,'Newby', true, 'ADMIN', 3);

INSERT INTO cardiff.tag (tag) VALUES
('cheese'),
('сыр'),
('erotic'),
('music'),
('musictoos'),
('bread'),
('pizza'),
('mobilephone'),
('car'),
('repair'),
('films'),
('books');


INSERT INTO cardiff.discount_card (card_number, created_date, expired_date, available, company_name,
amount_of_discount, description, deleted, person_id) VALUES
(1, NOW(),'02/10/2016', true, 'Cheese', 5,'скидка', false,1),
(445, NOW(), '01/11/2010', true, 'Kiss', 7,'скидка', false,3),
(11, NOW(), '02/10/1999', false, 'MusicTools', 8,'скидка', true,2),
(2, NOW(), '12/12/2018', true, 'DjStar', 4,'скидка', false,2),
(2145000, NOW(), '03/11/2009', false, 'Bread', 9,'скидка', false,1),
(123, NOW(), '05/21/2021', false, 'PizzaStar', 10,'скидка', false,1),
(4578, NOW(), '05/13/1891', true, 'PizzaCheap', 5,'скидка', false,1),
(145876421, NOW(), '08/20/2000', true, 'Mobilochka', 3,'скидка', false,4),
(4751, NOW(), '03/23/2020', true, 'Tavriya', 3,'скидка', false,1),
(54576, NOW(),'09/29/2013', true, 'MotorStar', 6,'скидка', false,5),
(55555555, NOW(), '02/10/1996', true, 'STO', 5,'скидка', true,5);

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

INSERT INTO cardiff.book_card (book_date_start, book_date_end, discount_card_id, person_id) VALUES
('06/10/2015','06/12/2015',2,9),
('01/01/2010','06/10/2015',2,8),
('08/20/2015','08/23/2015',6,3),
('08/23/2015','08/25/2015',6,4),
('08/25/2015','08/27/2015',2,5),
('09/01/2015','09/02/2015',11,9),
('01/02/2016','01/08/2016',10,3),
('08/20/2015','01/09/2015',7,4),
('08/21/2015','01/10/2015',9,9),
('08/30/2015','01/10/2015',3,9);


INSERT INTO cardiff.discount_card_comment (comment_text, comment_date, discount_card_id, person_id) VALUES
('They sell fake phones','08/02/2015',7,1),
('Cruel store','02/20/2014',9,2),
('Good shop','08/15/2015',3,2),
('Best assortment','09/12/2014', 2,3),
('High prices','08/15/2015',10,4),
('Delicious','08/16/2015',6,9),
('Its not so cheap','07/18/2015',7,7),
('it good', '08/14/2015',7,6),
('They robbers','08/16/2015',1,8),
('there are beautiful assistans','08/20/2015',4,10);


INSERT INTO cardiff.discount_card_history (picked_date, return_date, discount_card_id, person_id) VALUES
('06/10/2015','06/12/2015',2,9),
('01/01/2010','06/10/2014',2,8),
('08/20/2014','08/23/2014',6,3),
('08/23/2014','08/25/2014',6,4),
('08/25/2014','08/27/2014',2,5),
('09/01/2014','09/02/2014',11,9),
('01/02/2014','01/08/2014',10,3),
('08/20/2014','09/01/2014',7,4),
('08/21/2014','10/01/2014',9,9),
('08/30/2014','10/01/2014',3,9);

































