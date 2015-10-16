package com.provectus.cardiff.utils.validator;

import com.provectus.cardiff.entities.CustomTag;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by artemvlasov on 16/10/15.
 */
public class CustomTagValidatorTest {
    CustomTag customTag;


    @Before
    public void setup() {
        customTag = new CustomTag();
        customTag.setTag("test");
        customTag.setDescription("Test description");
    }
    @Test
    public void validateTest() {
        assertTrue(CustomTagValidator.validate(customTag));
    }

    @Test
    public void validateWithNullDescriptionTest() {
        customTag.setDescription(null);
        assertTrue(CustomTagValidator.validate(customTag));
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithTagNullTest() {
        customTag.setTag(null);
        CustomTagValidator.validate(customTag);
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidTagExceedMaxLengthTest() {
        Random r = new Random();
        String alphabet = "123xyz";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 21; i++) {
            stringBuilder.append(alphabet.charAt(r.nextInt(alphabet.length())));
        }
        customTag.setTag(stringBuilder.toString());
        CustomTagValidator.validate(customTag);
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidTagLessThanMinLengthTest() {
        customTag.setTag("f");
        CustomTagValidator.validate(customTag);
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidTagCharactersTest() {
        customTag.setTag("4454");
        CustomTagValidator.validate(customTag);
    }
}
