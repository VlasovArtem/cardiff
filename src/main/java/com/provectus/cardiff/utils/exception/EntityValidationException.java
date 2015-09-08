package com.provectus.cardiff.utils.exception;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by artemvlasov on 06/09/15.
 */
public class EntityValidationException extends RuntimeException {
    private ObjectNode objectNode = null;

    public EntityValidationException() {
    }

    public EntityValidationException(ObjectNode objectNode, String message) {
        super(message);
        this.objectNode = objectNode;
    }

    public EntityValidationException(String message) {
        super(message);
    }

    public EntityValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityValidationException(Throwable cause) {
        super(cause);
    }

    public EntityValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ObjectNode getObjectNode() {
        return objectNode;
    }
}
