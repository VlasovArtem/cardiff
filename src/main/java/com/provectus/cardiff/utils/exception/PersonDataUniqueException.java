package com.provectus.cardiff.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by artemvlasov on 08/09/15.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class PersonDataUniqueException extends RuntimeException {
    public PersonDataUniqueException() {
    }

    public PersonDataUniqueException(String message) {
        super(message);
    }

    public PersonDataUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersonDataUniqueException(Throwable cause) {
        super(cause);
    }

    public PersonDataUniqueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
