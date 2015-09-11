package com.provectus.cardiff.utils.validator;

import com.provectus.cardiff.entities.Tag;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static com.provectus.cardiff.utils.validator.TagValidator.validate;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by artemvlasov on 06/09/15.
 */
public class TagValidatorTest {
    private Tag tag;
    @Before
    public void createTag() {
        tag = new Tag();
        tag.setTag("Pizza");
    }

    @Test
    public void validateTest() {
        assertTrue(validate(tag));
    }

    @Test
    public void validateNullTest() {
        assertFalse(validate(null));
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithTagNullTest() {
        tag.setTag(null);
        validate(tag);
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidTagLessThanMinLengthTest() {
        tag.setTag("Ta");
        validate(tag);
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidTagGreaterThanMaxLengthTest() {
        Random r = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 31; i++) {
            builder.append((char)(r.nextInt(26) + 'a'));
        }
        tag.setTag(builder.toString());
        validate(tag);
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidTagUnacceptableCharactersTest() {
        tag.setTag("dfs35f");
        validate(tag);
    }
}
