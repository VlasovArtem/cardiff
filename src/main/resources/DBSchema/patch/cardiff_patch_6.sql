---
--- Skype credentials
---
ALTER TABLE person ADD COLUMN skype VARCHAR(32) UNIQUE;
-- UPDATE person SET skype = "Please add skype";
-- ALTER TABLE person ALTER skype SET NOT NULL