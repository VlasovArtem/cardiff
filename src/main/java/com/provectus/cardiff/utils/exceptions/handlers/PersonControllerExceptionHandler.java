package com.provectus.cardiff.utils.exceptions.handlers;

import com.provectus.cardiff.utils.exceptions.PersonLoginException;
import com.provectus.cardiff.utils.exceptions.PersonRegistrationException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.provectus.cardiff.utils.ResponseEntityExceptionCreator.create;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Created by artemvlasov on 29/08/15.
 */
@ControllerAdvice
public class PersonControllerExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity authenticationHandler(Exception ex) {
        return create(FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
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
}