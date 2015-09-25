package com.provectus.cardiff.utils.validator;

import com.provectus.cardiff.entities.CardBooking;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.provectus.cardiff.utils.validator.CardBookingValidator.validate;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by artemvlasov on 08/09/15.
 */
public class CardBookingValidatorTest {
    private CardBooking cardBooking;

    @Before
    public void setup() {
        cardBooking = new CardBooking();
        cardBooking.setCreatedDate(LocalDateTime.now());
        cardBooking.setBookingEndDate(LocalDateTime.now().plusDays(7l));
    }

    @Test
    public void validateTest() {
        assertTrue(validate(Optional.ofNullable(cardBooking)));
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidCreatedDateTest() {
        cardBooking.setCreatedDate(LocalDateTime.now().minusDays(1));
        validate(Optional.ofNullable(cardBooking));
    }

    @Test
    public void validateWithNullTest() {
        assertFalse(validate(Optional.ofNullable(null)));
    }
}
