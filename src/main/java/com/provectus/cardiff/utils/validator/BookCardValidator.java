package com.provectus.cardiff.utils.validator;

import com.provectus.cardiff.entities.BookCard;
import com.provectus.cardiff.utils.exception.EntityValidationException;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Validate {@link BookCard} created date (book start date).
 */
public class BookCardValidator {
    private static final String ERROR = "Booking start date cannot be less than current date";

    /**
     * Validate {@link BookCard} created date
     * @param bookCard Optional of {@link BookCard}
     * @return true if created date (book start date) is after or equals current date
     */
    public static boolean validate(Optional<BookCard> bookCard) {
        if(bookCard.isPresent()) {
            if(bookCard.get().getCreatedDate().toLocalDate().isBefore(LocalDate.now())) {
                throw new EntityValidationException(ERROR);
            }
            return true;
        }
        return false;
    }

}
