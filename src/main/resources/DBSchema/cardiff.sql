--
--Schema: cardiff;
--

CREATE SCHEMA cardiff;

--
-- Name: book_card; Type: TABLE; Schema: cardiff; Owner: postgres;
--

CREATE TABLE cardiff.book_card (
	id		INT PRIMARY KEY		NOT NULL,
	book_date_start TIMESTAMP   NOT NULL,
	book_date_end 	TIMESTAMP	NOT NULL,
	dicount_card_id INT	REFERENCES cardiff.discount_card(id),
	client_id		INT	REFERENCES cardiff.client(id),
);

--
-- Name: discount_card_comment; Type: TABLE; Schema: cardiff; Owner: postgres;
--

CREATE TABLE cardiff.discount_card_comment (
	id		INT PRIMARY KEY		NOT NULL,
	comment_text	VARCHAR(500)NOT NULL,
	comment_date 	TIMESTAMP	NOT NULL,
	dicount_card_id INT	REFERENCES cardiff.discount_card(id),
	client_id		INT	REFERENCES cardiff.client(id),
);

--
-- Name: client; Type: TABLE; Schema: cardiff; Owner: postgres;
--

CREATE TABLE cardiff.client (
	id		INT PRIMARY KEY		NOT NULL,
	name			VARCHAR(100)	NOT NULL,
	login			VARCHAR(100)	NOT NULL,
	password		BYTEA			NOT NULL,
	email			VARCHAR(50)		NOT NULL,
	phone_number 	BIGSERIAL,
	description 	VARCHAR(500),
	deleted			BOOLEAN			NOT NULL,
);

--
-- Name: discount_card; Type: TABLE; Schema: cardiff; Owner: postgres;
--

CREATE TABLE cardiff.discount_card (
	id		INT PRIMARY KEY		NOT NULL,
	card_number			BIGINT		NOT NULL,
	expired_date		DATE ,
	available 			BOOLEAN		NOT NULL,
	company_name 		VARCHAR(50)	NOT NULL,
	amount_of_discount 	INT			NOT NULL,
	description 		VARCHAR(500),
	deleted				BOOLEAN		NOT NULL,
	client_id			INT	REFERENCES cardiff.client(id),
);

--
-- Name: discount_card_history; Type: TABLE; Schema: cardiff; Owner: postgres;
--

CREATE TABLE cardiff.discount_card_history (
	id			INT PRIMARY KEY		NOT NULL,
	picked_date			TIMESTAMP  	NOT NULL,
	return_date			TIMESTAMP 	NOT NULL,
	dicount_card_id INT	REFERENCES cardiff.discount_card(id),
	client_id		INT	REFERENCES cardiff.client(id),
);

--
-- Name: tag_card; Type: TABLE; Schema: cardiff; Owner: postgres;
--

CREATE TABLE cardiff.tag_card (
	id		INT PRIMARY KEY		NOT NULL,
	dicount_card_id INT	REFERENCES cardiff.discount_card(id),
	tag_id			INT	REFERENCES cardiff.tag(id),
);

--
-- Name: tag; Type: TABLE; Schema: cardiff; Owner: postgres;
--

CREATE TABLE cardiff.tag (
	id		INT PRIMARY KEY		NOT NULL,
	tag			VARCHAR(50)		NOT NULL,
);
