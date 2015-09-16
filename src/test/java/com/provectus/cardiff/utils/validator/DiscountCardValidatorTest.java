package com.provectus.cardiff.utils.validator;

import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static com.provectus.cardiff.utils.validator.DiscountCardValidator.validate;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by artemvlasov on 06/09/15.
 */
public class DiscountCardValidatorTest {
    private DiscountCard discountCard;
    @Before
    public void createDiscountCard() {
        discountCard = new DiscountCard();
        discountCard.setDescription("Discount card description");
        discountCard.setAmountOfDiscount(10);
        discountCard.setCardNumber(325);
        discountCard.setCompanyName("Famous Pizza");
    }

    @Test
    public void validateTest() {
        assertTrue(validate(discountCard));
    }

    @Test
    public void validateWithNullTest() {
        assertFalse(validate(null));
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidCardNumberLessThanMinLengthTest() {
        discountCard.setCardNumber(0);
        validate(discountCard);
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidCardNumberGreaterThanMaxLengthTest() {
        discountCard.setCardNumber(25635698563475211l);
        validate(discountCard);
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidCompanyNameNullTest() {
        discountCard.setCompanyName(null);
        validate(discountCard);
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidAmountOfDiscountTest() {
        discountCard.setAmountOfDiscount(110);
        validate(discountCard);
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidDescriptionTest() {
        Random r = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 600; i++) {
            builder.append((char)(r.nextInt(26) + 'a'));
        }
        discountCard.setDescription(builder.toString());
        validate(discountCard);
    }
}
