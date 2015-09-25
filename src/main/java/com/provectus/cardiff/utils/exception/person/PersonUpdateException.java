package com.provectus.cardiff.utils.exception.person;

/**
 * Created by artemvlasov on 05/09/15.
 */
public class PersonUpdateException extends RuntimeException {
    public PersonUpdateException() {
    }

    public PersonUpdateException(String message) {
        super(message);
    }

    public PersonUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersonUpdateException(Throwable cause) {
        super(cause);
    }

    public PersonUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
