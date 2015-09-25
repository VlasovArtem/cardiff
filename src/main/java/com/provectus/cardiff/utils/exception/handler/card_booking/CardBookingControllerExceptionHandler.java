package com.provectus.cardiff.utils.exception.handler.card_booking;

import com.provectus.cardiff.utils.ResponseEntityExceptionCreator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * Created by artemvlasov on 25/09/15.
 */
@ControllerAdvice
public class CardBookingControllerExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception ex) {
        return ResponseEntityExceptionCreator.create(FORBIDDEN, ex.getMessage());
    }
}
