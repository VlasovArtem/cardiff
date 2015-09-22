--
--Schema: cardiff;
--

CREATE SCHEMA cardiff;

SET search_path = cardiff;
set datestyle to ISO, MDY;

--
-- Name: person; Type: TABLE; Schema: cardiff; Owner: postgres;
--

CREATE TABLE cardiff.location (
  id BIGSERIAL PRIMARY KEY NOT NULL,
  created_date DATE,
  city VARCHAR(50) NOT NULL,
  country VARCHAR(50) NOT NULL,
  CONSTRAINT city_county UNIQUE (city, country)
);

CREATE TABLE cardiff.person (
  id           BIGSERIAL PRIMARY KEY    NOT NULL,
  name         VARCHAR(100),
  login        VARCHAR(100)       NOT NULL  UNIQUE,
  password     BYTEA              NOT NULL,
  email        VARCHAR(254)        NOT NULL  UNIQUE,
  created_date DATE,
  phone_number BIGINT,
  description  VARCHAR(500),
  deleted      BOOLEAN            NOT NULL,
  role         VARCHAR(10) NOT NULL DEFAULT 'USER',
  location_id BIGINT REFERENCES cardiff.location (id)
  ON DELETE NO ACTION
  ON UPDATE CASCADE
);

--
-- Name: discount_card; Type: TABLE; Schema: cardiff; Owner: postgres;
--

CREATE TABLE cardiff.discount_card (
  id                 BIGSERIAL PRIMARY KEY      NOT NULL,
  card_number        BIGINT               NOT NULL UNIQUE,
  created_date       DATE,
  company_name       VARCHAR(50)          NOT NULL,
  amount_of_discount INT                  NOT NULL,
  description        VARCHAR(500),
  deleted            BOOLEAN              NOT NULL,
  person_id          BIGINT REFERENCES cardiff.person (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

--
-- Name: book_card; Type: TABLE; Schema: cardiff; Owner: postgres;
--

CREATE TABLE cardiff.book_card (
  id              BIGSERIAL PRIMARY KEY        NOT NULL,
  book_date_start TIMESTAMP              NOT NULL,
  book_date_end   TIMESTAMP              NOT NULL,
  discount_card_id BIGINT REFERENCES cardiff.discount_card (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  person_id       BIGINT REFERENCES cardiff.person (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

--
-- Name: discount_card_comment; Type: TABLE; Schema: cardiff; Owner: postgres;
--

CREATE TABLE cardiff.discount_card_comment (
  id              BIGSERIAL PRIMARY KEY        NOT NULL,
  comment_text    VARCHAR(500)           NOT NULL,
  comment_date    TIMESTAMP              NOT NULL,
  discount_card_id BIGINT REFERENCES cardiff.discount_card (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  person_id       BIGINT REFERENCES cardiff.person (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

--
-- Name: discount_card_history; Type: TABLE; Schema: cardiff; Owner: postgres;
--

CREATE TABLE cardiff.discount_card_history (
  id              BIGSERIAL PRIMARY KEY    NOT NULL,
  picked_date     TIMESTAMP          NOT NULL,
  return_date     TIMESTAMP          NOT NULL,
  discount_card_id BIGINT REFERENCES cardiff.discount_card (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  person_id       BIGINT REFERENCES cardiff.person (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

--
-- Name: tag; Type: TABLE; Schema: cardiff; Owner: postgres;
--

CREATE TABLE cardiff.tag (
  id  BIGSERIAL PRIMARY KEY    NOT NULL,
  tag VARCHAR(50)        NOT NULL UNIQUE
);

--
-- Name: tag_card; Type: TABLE; Schema: cardiff; Owner: postgres;
--

CREATE TABLE cardiff.tag_card (
  id              BIGSERIAL PRIMARY KEY    NOT NULL,
  discount_card_id BIGINT REFERENCES cardiff.discount_card (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  tag_id          BIGINT REFERENCES cardiff.tag (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

ALTER database cardiff SET search_path TO cardiff;
--
-- ONLY FOR DEVELOPMENT
--
ALTER SEQUENCE discount_card_comment_id_seq RESTART WITH 1000;
ALTER SEQUENCE discount_card_history_id_seq RESTART WITH  1000;
ALTER SEQUENCE discount_card_id_seq RESTART WITH  1000;
ALTER SEQUENCE tag_id_seq RESTART WITH  1000;
ALTER SEQUENCE book_card_id_seq RESTART WITH  1000;
ALTER SEQUENCE location_id_seq RESTART WITH  1000;
ALTER SEQUENCE person_id_seq RESTART WITH  1000;
ALTER SEQUENCE tag_card_id_seq RESTART WITH  1000;

--
-- Spring Security Remember me table
--
CREATE TABLE persistent_logins (
  username varchar(64) not null,
  series varchar(64) primary key,
  token varchar(64) not null,
  last_used timestamp not null
)