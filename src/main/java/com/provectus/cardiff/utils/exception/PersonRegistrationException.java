package com.provectus.cardiff.utils.exception;

/**
 * Created by artemvlasov on 29/08/15.
 */
public class PersonRegistrationException extends RuntimeException {
    public PersonRegistrationException() {
    }

    public PersonRegistrationException(String message) {
        super(message);
    }

    public PersonRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersonRegistrationException(Throwable cause) {
        super(cause);
    }

    public PersonRegistrationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
