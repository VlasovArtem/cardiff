package com.provectus.cardiff.utils.validator;

import com.provectus.cardiff.entities.DiscountCardComment;
import com.provectus.cardiff.utils.exception.EntityValidationException;

import java.util.Optional;

/**
 * {@code DiscountCardCommentValidator} validate comment of {@code DiscountCardComment}.
 * Return true if comment matches pattern. Throws {@code EntityValidationException} with error message if comment not
 * matches pattern.
 */
public class DiscountCardCommentValidator extends EntityValidator {
    private final static String ERROR = "Comment text is required, max length - 500";
    private final static String PATTERN = "^.{1,500}$";

    /**
     * Validate {@code DiscountCardComment}
     * @param cardComment Optional of {@code DiscountCardComment}
     * @return return true if discount card comment matches pattern
     */
    public static boolean validate(Optional<DiscountCardComment> cardComment) {
        if(cardComment.isPresent()) {
            if(!validate(cardComment.get().getComment(), PATTERN)) {
                throw new EntityValidationException(ERROR);
            }
            return true;
        }
        return false;
    }
}
