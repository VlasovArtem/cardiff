---
--- Skype credentials
---
ALTER TABLE person ADD COLUMN skype VARCHAR(32) UNIQUE;
--- ALTER TABLE person ALTER skype SET NOT NULL