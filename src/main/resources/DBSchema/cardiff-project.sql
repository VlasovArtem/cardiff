--
-- PostgreSQL database dump
--

CREATE SCHEMA cardiff;

SET search_path = cardiff, pg_catalog;

--
-- Name: book_card; Type: TABLE; Schema: cardiff;
--

CREATE TABLE book_card (
    id bigint NOT NULL,
    book_date_start timestamp without time zone,
    book_date_end timestamp without time zone,
    discount_card_id bigint,
    user_id bigint
);

--
-- Name: discount_card; Type: TABLE; Schema: cardiff;
--

CREATE TABLE discount_card (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    amount_of_discount integer,
    availiable boolean NOT NULL,
    card_number bigint NOT NULL,
    company_name character varying(275) NOT NULL,
    deleted boolean NOT NULL,
    description character varying(520),
    expired_date timestamp without time zone,
    user_id bigint
);

--
-- Name: discount_card_comment; Type: TABLE; Schema: cardiff;
--

CREATE TABLE discount_card_comment (
    id integer NOT NULL,
    comment character varying(520),
    comment_date date,
    discount_card_id integer,
    user_id integer
);

--
-- Name: discount_card_history; Type: TABLE; Schema: cardiff;
--

CREATE TABLE discount_card_history (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    comment character varying(520) NOT NULL,
    return_date timestamp without time zone,
    discount_card_id bigint,
    user_id bigint
);

--
-- Name: tag; Type: TABLE; Schema: cardiff;
--

CREATE TABLE tag (
    id bigint NOT NULL,
    tag character varying(50) NOT NULL
);

--
-- Name: tag_card; Type: TABLE; Schema: cardiff;
--

CREATE TABLE tag_card (
    discount_card_id bigint NOT NULL,
    tag_id bigint NOT NULL
);

--
-- Name: user; Type: TABLE; Schema: cardiff;
--

CREATE TABLE "user" (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    deleted boolean NOT NULL,
    description character varying(520),
    email character varying(70),
    login character varying(120),
    name character varying(120),
    password bytea,
    phone_number bigint
);

--
-- Name: book_card_pkey; Type: CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY book_card
    ADD CONSTRAINT book_card_pkey PRIMARY KEY (id);


--
-- Name: discount_card_comment_id; Type: CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY discount_card_comment
    ADD CONSTRAINT discount_card_comment_id PRIMARY KEY (id);


--
-- Name: discount_card_history_pkey; Type: CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY discount_card_history
    ADD CONSTRAINT discount_card_history_pkey PRIMARY KEY (id);


--
-- Name: discount_card_pkey; Type: CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY discount_card
    ADD CONSTRAINT discount_card_pkey PRIMARY KEY (id);


--
-- Name: tag_card_pkey; Type: CONSTRAINT; Schema: cardiff; 
--

ALTER TABLE ONLY tag_card
    ADD CONSTRAINT tag_card_pkey PRIMARY KEY (discount_card_id, tag_id);


--
-- Name: tag_pkey; Type: CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY tag
    ADD CONSTRAINT tag_pkey PRIMARY KEY (id);


--
-- Name: uk_ew1hvam8uwaknuaellwhqchhb; Type: CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT uk_ew1hvam8uwaknuaellwhqchhb UNIQUE (login);


--
-- Name: uk_o57lceede8jwia5g1f6mgkidq; Type: CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY tag
    ADD CONSTRAINT uk_o57lceede8jwia5g1f6mgkidq UNIQUE (tag);


--
-- Name: uk_ob8kqyqqgmefl0aco34akdtpe; Type: CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT uk_ob8kqyqqgmefl0aco34akdtpe UNIQUE (email);


--
-- Name: uk_pxq0nyr0raoccj4vooi9oskqx; Type: CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY discount_card
    ADD CONSTRAINT uk_pxq0nyr0raoccj4vooi9oskqx UNIQUE (card_number);


--
-- Name: user_pkey; Type: CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: fk_aw7qe87n4ybshnai16t1ce2p; Type: FK CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY tag_card
    ADD CONSTRAINT fk_aw7qe87n4ybshnai16t1ce2p FOREIGN KEY (discount_card_id) REFERENCES discount_card(id);


--
-- Name: fk_h0iuol5d7c5nbu79weayvap0k; Type: FK CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY discount_card_history
    ADD CONSTRAINT fk_h0iuol5d7c5nbu79weayvap0k FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: fk_kmar62i90awn4v6ihqjso9dli; Type: FK CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY book_card
    ADD CONSTRAINT fk_kmar62i90awn4v6ihqjso9dli FOREIGN KEY (discount_card_id) REFERENCES discount_card(id);


--
-- Name: fk_li0nvst0qllvgq43ia09f0hkj; Type: FK CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY discount_card
    ADD CONSTRAINT fk_li0nvst0qllvgq43ia09f0hkj FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: fk_omtk5tia96t2rse36uooh9uv2; Type: FK CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY discount_card_history
    ADD CONSTRAINT fk_omtk5tia96t2rse36uooh9uv2 FOREIGN KEY (discount_card_id) REFERENCES discount_card(id);


--
-- Name: fk_pfftiyfma617owua4yecqqwsq; Type: FK CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY tag_card
    ADD CONSTRAINT fk_pfftiyfma617owua4yecqqwsq FOREIGN KEY (tag_id) REFERENCES tag(id);


--
-- Name: fk_r8ennhfxegl2m2cy2e8ryqfxt; Type: FK CONSTRAINT; Schema: cardiff;
--

ALTER TABLE ONLY book_card
    ADD CONSTRAINT fk_r8ennhfxegl2m2cy2e8ryqfxt FOREIGN KEY (user_id) REFERENCES "user"(id);

