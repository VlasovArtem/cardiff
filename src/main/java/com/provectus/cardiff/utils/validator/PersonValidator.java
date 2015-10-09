package com.provectus.cardiff.utils.validator;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * {@code PersonValidator} validate name, login, password, email, phone number, description of
 * {@code Person}.
 * Return true if validate data is matches patterns. Throws {@code EntityValidationException} with error message if
 * validate data not matches pattern.
 */
public class PersonValidator extends EntityValidator {

    /**
     * Validate {@code PersonValidator}
     * @param person Optional of {@code PersonValidator}
     * @return true if all validate data matches their patterns otherwise throw {@code EntityValidationException}
     */
    public static boolean validate(Person person, boolean update) {
        try {
            Optional.of(person).ifPresent(p -> {
                ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
                Arrays.stream(PersonValidationInfo.values()).allMatch(dc -> {
                    if(update && dc.equals(PersonValidationInfo.PASSWORD)) {
                        return true;
                    }
                    if (!validate(dc, p)) {
                        objectNode.put(dc.name().toLowerCase(), dc.getError());
                    }
                    return true;
                });
                if (objectNode.size() != 0) {
                    throw new EntityValidationException(objectNode, "Person form contains invalid data");
                }
            });
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * Validate {@code PersonValidator} data
     * @param info Enum for one of validated data
     * @param person Validated object
     * @return true if validated data is matches pattern
     */
    private static boolean validate(PersonValidationInfo info, Person person) {
        switch (info) {
            case NAME:
                if(Objects.equals(person.getName(), "")) {
                    person.setName(null);
                }
                return person.getName() == null ||
                        validate(person.getName(), info.pattern);
            case LOGIN:
                return validate(person.getLogin(), info.pattern);
            case PASSWORD:
                return validate(person.getPassword(), info.pattern);
            case PHONE_NUMBER:
                return validate(String.valueOf(person.getPhoneNumber()), info.pattern);
            case DESCRIPTION:
                if(Objects.equals(person.getDescription(), "")) {
                    person.setDescription(null);
                }
                return person.getDescription() == null ||
                        validate(String.valueOf(person.getDescription()), info.pattern);
            case EMAIL:
                return EmailValidator.getInstance().isValid(person.getEmail());
            default:
                return false;
        }
    }

    /**
     * Enum contains error messages and patterns for validating data
     */
    public enum PersonValidationInfo {
        NAME("Name should not contains any digits and length should be 6 - 100",
                "^[\\p{L} .'\\-]{6,100}$"),
        LOGIN("Login should contains next characters: a-z 0-9 _ -. And length should be 6 - 100",
                "^[A-Za-z0-9_\\- .]{6,100}$"),
        PASSWORD("Min length of password should be 8 and max 128",
                "^.{8,128}$"),
        PHONE_NUMBER("Phone number should contains",
                "[0-9]{9,10}"),
        DESCRIPTION("Description length should be less than 500",
                ".{0,500}"),
        EMAIL("Email is not matches standard pattern",
                "");
        private String error;
        private String pattern;

        PersonValidationInfo(String error, String pattern) {
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
