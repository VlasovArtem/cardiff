package com.provectus.cardiff.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by artemvlasov on 08/09/15.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class DataUniqueException extends RuntimeException {
    public DataUniqueException() {
    }

    public DataUniqueException(String message) {
        super(message);
    }

    public DataUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataUniqueException(Throwable cause) {
        super(cause);
    }

    public DataUniqueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
