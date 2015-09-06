package com.provectus.cardiff.utils.validator;

import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.utils.exception.EntityValidationException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

/**
 * {@code DiscountCardValidator} validate card number, expired date, company name, amount of discount, description of
 * {@code DiscountCard}.
 * Return true if validate data is matches patterns. Throws {@code EntityValidationException} with error message if
 * validate data not matches pattern.
 */
public class DiscountCardValidator extends EntityValidator {

    /**
     * Validate {@code DiscountCard}
     * @param card Optional of {@code DiscountCard}
     * @return true if all validate data matches their patterns otherwise throw {@code EntityValidationException}
     */
    public static boolean validate(Optional<DiscountCard> card) {
        return card.isPresent() && Arrays.stream(DiscountCardValidationInfo.values()).allMatch(dc -> {
            if (!validate(dc, card.get())) {
                throw new EntityValidationException(dc.getError());
            }
            return true;
        });
    }

    /**
     * Validate {@code DiscountCard} data
     * @param info Enum for one of validated data
     * @param discountCard Validated object
     * @return true if validated data is matches pattern
     */
    private static boolean validate(DiscountCardValidationInfo info, DiscountCard discountCard) {
        switch (info) {
            case CARD_NUMBER:
                return validate(String.valueOf(discountCard.getCardNumber()), info.getPattern());
            case EXPIRED_DATE:
                return LocalDateTime.now().isBefore(discountCard.getExpiredDate());
            case COMPANY_NAME:
                return validate(discountCard.getCompanyName(), info.getPattern());
            case AMOUNT_OF_DISCOUNT:
                return discountCard.getAmountOfDiscount() == 0 ||
                        validate(String.valueOf(discountCard.getAmountOfDiscount()), info.getPattern());
            case DESCRIPTION:
                return discountCard.getDescription() == null ||
                        validate(discountCard.getDescription(), info.getPattern());
            default:
                return false;
        }
    }

    /**
     * Enum contains error messages and patterns for validating data
     */
    public enum DiscountCardValidationInfo {
        CARD_NUMBER("Card number is required, should contains numbers, max - 16 and min - 1",
                "^[1-9][0-9]{0,15}$"),
        EXPIRED_DATE("Expire date cannot be less than current time",
                null),
        COMPANY_NAME("Company name is requires and max length is 50",
                "^.{1,50}$"),
        AMOUNT_OF_DISCOUNT("Amount of discount should be from 1 - 100",
                "^[1-9][0-9]$|^100$"),
        DESCRIPTION("Description length should be less than 500",
                ".{0,500}");
        private String error;
        private String pattern;

        DiscountCardValidationInfo(String error, String pattern) {
            this.error = error;
            this.pattern = pattern;
        }

        public String getError() {
            return error;
        }

        public String getPattern() {
            return pattern;
        }
    }
}
