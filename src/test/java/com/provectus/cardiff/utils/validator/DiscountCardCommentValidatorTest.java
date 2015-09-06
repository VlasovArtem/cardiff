package com.provectus.cardiff.utils.validator;

import com.provectus.cardiff.entities.DiscountCardComment;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static com.provectus.cardiff.utils.validator.DiscountCardCommentValidator.*;
import static java.util.Optional.ofNullable;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by artemvlasov on 06/09/15.
 */
public class DiscountCardCommentValidatorTest {
    private DiscountCardComment cardComment;

    @Before
    public void createTag() {
        cardComment = new DiscountCardComment();
        cardComment.setComment("This restaurant has awesome pizza");
    }

    @Test
    public void validateTest() {
        assertTrue(validate(ofNullable(cardComment)));
    }

    @Test
    public void validateNullTest() {
        assertFalse(validate(ofNullable(null)));
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidCommentZeroLengthTest() {
        cardComment.setComment("");
        validate(ofNullable(cardComment));
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidCommentGreaterThatMaxLengthTest() {
        Random r = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 600; i++) {
            builder.append((char)(r.nextInt(26) + 'a'));
        }
        cardComment.setComment(builder.toString());
        validate(ofNullable(cardComment));
    }
}
