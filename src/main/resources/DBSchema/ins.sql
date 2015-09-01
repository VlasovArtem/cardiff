INSERT INTO cardiff.person (id, name, login, password, email, phone_number, description, deleted, role) VALUES
(1,'Gordon Trick','chuhcha', 'dfasf','chuhcha@mail.ru',380698745232,'Likes to eat', false, 'USER'),
(2,'Symson Drul','gena', 'trer','gena@mail.ru',380675258565,'Likes to music', false, 'USER'),
(3,'Vadimiy Vadimiyavich','putin', 'yt15GThg','putinka@gmail.rcom',3800000000,'Likes to erotic', false, 'USER'),
(4,'Pordon Pordonovich','gore', 'OtUma','roremica@mail.ru',380786056065,'Likes to mobilephones', false, 'USER'),
(5,'Butoterbrod Dubovkiy','buter', 'dub','bub@mail.ru',380623211232,'Likes to cars', false, 'USER'),
(6,'Rostick Tostick','bogema', 'oLubvi','boge@ukr.net',380698745742,'Newby', false, 'ADMIN'),
(7,'Chick Reshetkin','recheto', 'utu','tutu@mail.ru',380698745221,'Newby', false, 'ADMIN'),
(8,'GTR Trol','trol', 'gtr','GTRtrol@mail.ru',380696545232,'Newby', true, 'ADMIN'),
(9,'Blonda Blond','tymophei', 'iytew','blonda@mail.ru',380698825232,'Beauty', false, 'ADMIN'),
(10,'Yric Chmirik','Chmir', 'dfasf','chmir@mail.ru',380696660000,'Newby', true, 'ADMIN');

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
    (1, 1, '02/10/2016', true, 'Cheese', 5,'скидка', false,1),
    (2, 445, '01/11/2010', true, 'Kiss', 7,'скидка', false,3),
    (3, 11, '02/10/1999', false, 'MusicTools', 8,'скидка', true,2),
    (4, 2, '12/12/2018', true, 'DjStar', 4,'скидка', false,2),
(5, 2145000, '03/11/2009', false, 'Bread', 9,'скидка', false,1),
(6, 123, '05/21/2021', false, 'PizzaStar', 10,'скидка', false,1),
(7, 4578, '05/13/1891', true, 'PizzaCheap', 5,'скидка', false,1),
(8, 145876421, '08/20/2000', true, 'Mobilochka', 3,'скидка', false,4),
(9, 4751, '03/23/2020', true, 'Tavriya', 3,'скидка', false,1),
(10, 54576,'09/29/2013', true, 'MotorStar', 6,'скидка', false,5),
(11, 55555555, '02/10/1996', true, 'STO', 5,'скидка', true,5);

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
(1,'06/10/2015','06/12/2015',2,9),
(2,'01/01/2010','06/10/2015',2,8),
(3,'08/20/2015','08/23/2015',6,3),
(4,'08/23/2015','08/25/2015',6,4),
(5,'08/25/2015','08/27/2015',2,5),
(6,'09/01/2015','09/02/2015',11,9),
(7,'01/02/2016','01/08/2016',10,3),
(8,'08/20/2015','01/09/2015',7,4),
(9,'08/21/2015','01/10/2015',9,9),
(10,'08/30/2015','01/10/2015',3,9);


INSERT INTO cardiff.discount_card_comment (id, comment_text, comment_date, discount_card_id, person_id) VALUES
(1,'They sell fake phones','08/02/2015',7,1),
(2,'Cruel store','02/20/2014',9,2),
(3,'Good shop','08/15/2015',3,2),
(4,'Best assortment','09/12/2014', 2,3),
(5,'High prices','08/15/2015',10,4),
(6,'Delicious','08/16/2015',6,9),
(7,'Its not so cheap','07/18/2015',7,7),
(8,'it good', '08/14/2015',7,6),
(9,'They robbers','08/16/2015',1,8),
(10,'there are beautiful assistans','08/20/2015',4,10);


INSERT INTO cardiff.discount_card_history (id, picked_date, return_date, discount_card_id, person_id) VALUES
(1,'06/10/2015','06/12/2015',2,9),
(2,'01/01/2010','06/10/2014',2,8),
(3,'08/20/2014','08/23/2014',6,3),
(4,'08/23/2014','08/25/2014',6,4),
(5,'08/25/2014','08/27/2014',2,5),
(6,'09/01/2014','09/02/2014',11,9),
(7,'01/02/2014','01/08/2014',10,3),
(8,'08/20/2014','09/01/2014',7,4),
(9,'08/21/2014','10/01/2014',9,9),
(10,'08/30/2014','10/01/2014',3,9);

































