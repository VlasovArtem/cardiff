package com.provectus.cardiff.utils.validator;

import com.provectus.cardiff.entities.BookCard;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.provectus.cardiff.utils.validator.BookCardValidator.validate;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by artemvlasov on 08/09/15.
 */
public class BookCardValidatorTest {
    private BookCard bookCard;

    @Before
    public void setup() {
        bookCard = new BookCard();
        bookCard.setCreatedDate(LocalDateTime.now());
        bookCard.setBookDateEnd(LocalDateTime.now().plusDays(7l));
    }

    @Test
    public void validateTest() {
        assertTrue(validate(Optional.ofNullable(bookCard)));
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidCreatedDateTest() {
        bookCard.setCreatedDate(LocalDateTime.now().minusDays(1));
        validate(Optional.ofNullable(bookCard));
    }

    @Test
    public void validateWithNullTest() {
        assertFalse(validate(Optional.ofNullable(null)));
    }
}
