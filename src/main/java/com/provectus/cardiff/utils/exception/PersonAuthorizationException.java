package com.provectus.cardiff.utils.exception;

/**
 * Created by artemvlasov on 13/09/15.
 */
public class PersonAuthorizationException extends RuntimeException {
    public PersonAuthorizationException() {
    }

    public PersonAuthorizationException(String message) {
        super(message);
    }

    public PersonAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersonAuthorizationException(Throwable cause) {
        super(cause);
    }

    public PersonAuthorizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
