package com.provectus.cardiff.utils.exception.card_booking;

/**
 * Created by artemvlasov on 25/09/15.
 */
public class CardBookingException extends RuntimeException {
    public CardBookingException() {
    }

    public CardBookingException(String message) {
        super(message);
    }

    public CardBookingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CardBookingException(Throwable cause) {
        super(cause);
    }

    public CardBookingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
