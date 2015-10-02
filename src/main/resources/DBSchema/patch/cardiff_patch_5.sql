--
-- Unverified tags table
--
CREATE TABLE cardiff.unverified_tags (
  id  BIGSERIAL PRIMARY KEY    NOT NULL,
  tag VARCHAR(50)        NOT NULL UNIQUE
);