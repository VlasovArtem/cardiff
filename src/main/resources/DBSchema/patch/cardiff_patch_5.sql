--
-- Custom tag table
--
CREATE TABLE cardiff.custom_tag (
  id  BIGSERIAL PRIMARY KEY NOT NULL,
  created_date DATE,
  tag VARCHAR(20) NOT NULL UNIQUE,
  description VARCHAR(50),
  author_id BIGINT REFERENCES cardiff.person (id)
);