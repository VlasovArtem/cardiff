INSERT INTO cardiff.role (id, name) VALUES
(1,'admin'),
(2,'user');

INSERT INTO cardiff.person (id, name, login, password, email, phone_number, description, deleted, role_id) VALUES
(1,'Gordon Trick','chuhcha', 'dfasf','chuhcha@mail.ru',380698745232,'Likes to eat', false,1),
(2,'Symson Drul','gena', 'trer','gena@mail.ru',380675258565,'Likes to music', false,1),
(3,'Vadimiy Vadimiyavich','putin', 'yt15GThg','putinka@gmail.rcom',3800000000,'Likes to erotic', false,1),
(4,'Pordon Pordonovich','gore', 'OtUma','roremica@mail.ru',380786056065,'Likes to mobilephones', false,1),
(5,'Butoterbrod Dubovkiy','buter', 'dub','bub@mail.ru',380623211232,'Likes to cars', false,1),
(6,'Rostick Tostick','bogema', 'oLubvi','boge@ukr.net',380698745742,'Newby', false,2),
(7,'Chick Reshetkin','recheto', 'utu','tutu@mail.ru',380698745221,'Newby', false,2),
(8,'GTR Trol','trol', 'gtr','GTRtrol@mail.ru',380696545232,'Newby', true,2),
(9,'Blonda Blond','tymophei', 'iytew','blonda@mail.ru',380698825232,'Beauty', false,2),
(10,'Yric Chmirik','Chmir', 'dfasf','chmir@mail.ru',380696660000,'Newby', true,2);

INSERT INTO cardiff.tag (id, tag) VALUES
(1,'cheese'),
(2,'сыр'),
(3,'erotic'),
(4,'music'),
(5,'musictoos'),
(6,'bread'),
(7,'pizza'),
(8,'mobilephone'),
(9,'car'),
(10,'repair'),
(11,'films'),
(12,'books');


INSERT INTO cardiff.discount_card (id, card_number, expired_date, available, company_name, amount_of_discount, description, deleted, person_id) VALUES
    (1, 00000000, '10/02/2016', true, 'Cheese', 5,'скидка', false,1),
    (2, 0000445000, '11/01/2010', true, 'Kiss', 7,'скидка', false,3),
    (3, 00011000, '10/02/1999', false, 'MusicTools', 8,'скидка', true,2),
    (4, 00000001, '12/12/2018', true, 'DjStar', 4,'скидка', false,2),
(5, 2145000, '11/03/2009', false, 'Bread', 9,'скидка', false,1),
(6, 123, '21/05/2021', false, 'PizzaStar', 10,'скидка', false,1),
(7, 4578, '13/05/1891', true, 'PizzaCheap', 5,'скидка', false,1),
(8, 145876421, '20/08/2000', true, 'Mobilochka', 3,'скидка', false,4),
(9, 0475100, '23/03/2020', true, 'Tavriya', 3,'скидка', false,1),
(10, 54576,'29/09/2013', true, 'MotorStar', 6,'скидка', false,5),
(11, 55555555, '10/02/1996', true, 'STO', 5,'скидка', true,5);

INSERT INTO cardiff.tag_card (id, discount_card_id, tag_id) VALUES
(1,1,1),
(2,1,2),
(3,2,3),
(4,2,11),
(5,2,12),
(6,3,4),
(7,3,5),
(8,4,4),
(9,5,6),
(10,6,7),
(11,7,7),
(12,8,8),
(13,9,1),
(14,9,2),
(15,9,6),
(16,9,11),
(17,9,12),
(18,10,9),
(19,11,9),
(20,10,11);

INSERT INTO cardiff.book_card (id, book_date_start, book_date_end, discount_card_id, person_id) VALUES
(1,'10/06/2015','12/06/2015',2,9),
(2,'01/01/2010','10/06/2015',2,8),
(3,'20/08/2015','23/08/2015',6,3),
(4,'23/08/2015','25/08/2015',6,4),
(5,'25/08/2015','27/08/2015',2,5),
(6,'01/09/2015','02/09/2015',11,9),
(7,'02/01/2016','08/01/2016',10,3),
(8,'20/08/2015','01/09/2015',7,4),
(9,'21/08/2015','01/10/2015',9,9),
(10,'30/08/2015','01/10/2015',3,9);


INSERT INTO cardiff.discount_card_comment (id, comment_text, comment_date, discount_card_id, person_id) VALUES
(1,'They sell fake phones','02/08/2015',7,1),
(2,'Cruel store','20/02/2014',9,2),
(3,'Good shop','15/08/2015',3,2),
(4,'Best assortment','12/09/2014', 2,3),
(5,'High prices','15/08/2015',10,4),
(6,'Delicious','16/08/2015',6,9),
(7,'Its not so cheap','18/07/2015',7,7),
(8,'it good', '14/08/2015',7,6),
(9,'They robbers','16/08/2015',1,8),
(10,'there are beautiful assistans','20/08/2015',4,10);


INSERT INTO cardiff.discount_card_history (id, picked_date, return_date, discount_card_id, person_id) VALUES
(1,'10/06/2015','12/06/2015',2,9),
(2,'01/01/2010','10/06/2014',2,8),
(3,'20/08/2014','23/08/2014',6,3),
(4,'23/08/2014','25/08/2014',6,4),
(5,'25/08/2014','27/08/2014',2,5),
(6,'01/09/2014','02/09/2014',11,9),
(7,'02/01/2014','08/01/2014',10,3),
(8,'20/08/2014','01/09/2014',7,4),
(9,'21/08/2014','01/10/2014',9,9),
(10,'30/08/2014','01/10/2014',3,9);

































