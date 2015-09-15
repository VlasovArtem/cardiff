package com.provectus.cardiff.utils.exception.handler;

import com.provectus.cardiff.utils.exception.EntityValidationException;
import com.provectus.cardiff.utils.exception.PersonAuthenticationException;
import com.provectus.cardiff.utils.exception.PersonAuthorizationException;
import com.provectus.cardiff.utils.exception.PersonDataUniqueException;
import com.provectus.cardiff.utils.exception.PersonLoginException;
import com.provectus.cardiff.utils.exception.PersonRegistrationException;
import com.provectus.cardiff.utils.exception.PersonUpdateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.provectus.cardiff.utils.ResponseEntityExceptionCreator.create;
import static org.springframework.http.HttpStatus.*;

/**
 * Created by artemvlasov on 29/08/15.
 */
@ControllerAdvice
public class PersonControllerExceptionHandler {
    @ExceptionHandler(PersonDataUniqueException.class)
    public ResponseEntity personDataUniqueHandler() {
        return ResponseEntity.status(CONFLICT).build();
    }

    @ExceptionHandler(PersonAuthenticationException.class)
    public ResponseEntity authenticationHandler(Exception ex) {
        return create(FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(PersonAuthorizationException.class)
    public ResponseEntity authorizationHandler() {
        return create(UNAUTHORIZED, "Person has no permission");
    }

    @ExceptionHandler(PersonRegistrationException.class)
    public ResponseEntity registrationHandler(Exception ex) {
        return create(FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(PersonLoginException.class)
    public ResponseEntity loginHandler() {
        return create(NOT_FOUND, "There is an error in email, login or password");
    }

    @ExceptionHandler(PersonUpdateException.class)
    public ResponseEntity updateHandler(Exception ex) {
        return create(FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(EntityValidationException.class)
    public ResponseEntity validatorHandler(EntityValidationException ex) {
        if(ex.getObjectNode() != null) {
            return create(NOT_ACCEPTABLE, ex.getMessage(), ex.getObjectNode());
        } else {
            return create(NOT_ACCEPTABLE, ex.getMessage());
        }
    }
}
