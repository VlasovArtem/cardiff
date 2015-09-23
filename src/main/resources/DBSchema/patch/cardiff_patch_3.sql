ALTER TABLE  discount_card  DROP CONSTRAINT discount_card_card_number_key ;
ALTER TABLE discount_card ADD CONSTRAINT card_number_company_name_key UNIQUE (card_number, company_name);