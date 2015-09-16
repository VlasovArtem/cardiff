package com.provectus.cardiff.utils.validator;

import com.provectus.cardiff.entities.Tag;
import com.provectus.cardiff.utils.exception.EntityValidationException;

import java.util.Optional;

/**
 * {@code TagValidator} validate tag of {@code Tag}.
 * Return true if tag matches pattern. Throws {@code EntityValidationException} with error message if tag not
 * matches pattern.
 */
public class TagValidator extends EntityValidator {
    private final static String ERROR = "Tag is required, min length - 3, max length - 30 and should contains only " +
            "characters from a-z";
    private final static String PATTERN = "^[A-Za-z -]{3,30}$";

    /**
     * Validate {@code Tag}
     * @param tag Optional of {@code Tag}
     * @return return true if tag matches pattern
     */
    public static boolean validate(Tag tag) {
        try {
            Optional.of(tag).ifPresent(t -> {
                if(!validate(t.getTag(), PATTERN)) {
                    throw new EntityValidationException(ERROR);
                }
            });
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
}
