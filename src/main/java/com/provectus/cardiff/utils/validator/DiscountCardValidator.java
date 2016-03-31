package com.provectus.cardiff.utils.validator;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.utils.exception.EntityValidationException;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public static boolean validate(DiscountCard card) {
        try {
            Optional.of(card).ifPresent(dCard -> {
                ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
                Arrays.stream(DiscountCardValidationInfo.values()).allMatch(info -> {
                    if (!validate(info, dCard)) {
                        objectNode.put(info.name().toLowerCase(), info.getError());
                    }
                    return true;
                });
                if(objectNode.size() != 0) {
                    throw new EntityValidationException(objectNode, "Discount card form contains invalid data");
                }
            });
        } catch (NullPointerException e) {
            return false;
        }
        return true;
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
            case COMPANY_NAME:
                return validate(discountCard.getCompanyName(), info.getPattern());
            case AMOUNT_OF_DISCOUNT:
                return discountCard.getAmountOfDiscount() == 0 ||
                        validate(String.valueOf(discountCard.getAmountOfDiscount()), info.getPattern());
            case DESCRIPTION:
                if(Objects.equals(discountCard.getDescription(), "")) {
                    discountCard.setDescription(null);
                }
                String convertedDescription;
                if(discountCard.getDescription() != null && discountCard.getDescription().contains("\n")) {
                    String[] sentences = discountCard.getDescription().split("\n");
                    convertedDescription = Stream.of(sentences).collect(Collectors.joining());
                    if(sentences.length + convertedDescription.length() > 500) {
                        return false;
                    }
                } else {
                    convertedDescription = discountCard.getDescription();
                }
                return convertedDescription == null ||
                        validate(convertedDescription, info.getPattern());
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
        COMPANY_NAME("Company name is requires and max length is 50",
                "^.{1,50}$"),
        AMOUNT_OF_DISCOUNT("Amount of discount should be from 0 - 100",
                "^(?!0|$)[0-9][0-9]?$|^100$"),
        DESCRIPTION("Description length should be less than 150",
                ".{0,150}");
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
