package com.provectus.cardiff.utils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by artemvlasov on 28/08/15.
 */
public class ResponseEntityExceptionCreator {

    /**
     * Create {@code ResponseEntity} for spring mvc
     * @param status Status that should have response if method throw different type of exceptions
     * @param message Message that should contains body of response, that match to exception information
     * @return {@code ResponseEntity} that contains Error Status and error message that warped with help of
     * {@code ObjectNode}
     */
    public static ResponseEntity create(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(JsonNodeFactory.instance.objectNode().put("error", message));
    }
}
