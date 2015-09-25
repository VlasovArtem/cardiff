package com.provectus.cardiff.utils.exception.person;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by artemvlasov on 12/09/15.
 */
public class PersonAuthenticationException extends AuthenticationException {
    public PersonAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public PersonAuthenticationException(String msg) {
        super(msg);
    }
}
