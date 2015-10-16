package com.provectus.cardiff.utils.validator;

import com.provectus.cardiff.entities.CustomTag;
import com.provectus.cardiff.utils.exception.EntityValidationException;

import java.util.Optional;

/**
 * {@code TagValidator} validate tag of {@code Tag}.
 * Return true if tag matches pattern. Throws {@code EntityValidationException} with error message if tag not
 * matches pattern.
 */
public class CustomTagValidator extends EntityValidator {
    private final static String TAG_ERROR = "Tag is required, min length - 3, max length - 20 and should contains only letters from a-z";
    private final static String TAG_PATTERN = "^[A-Za-z ]{3,20}$";
    private final static String DESCRIPTION_ERROR = "Description length should not be greater than 50 letters";
    private final static String DESCRIPTION_PATTERN = ".{0,50}";

    /**
     * Validate {@code Tag}
     * @param tag Optional of {@code Tag}
     * @return return true if tag matches pattern
     */
    public static boolean validate(CustomTag tag) {
        try {
            Optional.of(tag).ifPresent(t -> {
                if(!validate(t.getTag(), TAG_PATTERN)) {
                    throw new EntityValidationException(TAG_ERROR);
                }
                if(t.getDescription() != null && !validate(t.getDescription(), DESCRIPTION_PATTERN)) {
                    throw new EntityValidationException(DESCRIPTION_ERROR);
                }
            });
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
}
