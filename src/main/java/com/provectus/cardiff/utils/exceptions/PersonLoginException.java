package com.provectus.cardiff.utils.exceptions;

/**
 * Created by artemvlasov on 28/08/15.
 */
public class PersonLoginException extends RuntimeException {
    public PersonLoginException() {
    }

    public PersonLoginException(String message) {
        super(message);
    }

    public PersonLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersonLoginException(Throwable cause) {
        super(cause);
    }

    public PersonLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
