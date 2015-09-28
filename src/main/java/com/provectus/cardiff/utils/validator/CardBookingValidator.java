package com.provectus.cardiff.utils.validator;

import com.provectus.cardiff.entities.CardBooking;
import com.provectus.cardiff.utils.exception.EntityValidationException;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Validate {@link CardBooking} created date (book start date).
 */
public class CardBookingValidator {
    private static final String ERROR = "Booking start date cannot be less than current date";

    /**
     * Validate {@link CardBooking} created date
     * @param cardBooking Optional of {@link CardBooking}
     * @return true if created date (book start date) is after or equals current date
     */
    public static boolean validate(Optional<CardBooking> cardBooking) {
        if(cardBooking.isPresent()) {
            if(cardBooking.get().getBookingStartDate().isBefore(LocalDate.now())) {
                throw new EntityValidationException(ERROR);
            }
            return true;
        }
        return false;
    }

}
