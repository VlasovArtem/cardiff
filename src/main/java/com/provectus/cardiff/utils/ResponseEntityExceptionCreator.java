package com.provectus.cardiff.utils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by artemvlasov on 28/08/15.
 */
public class ResponseEntityExceptionCreator {
    public static ResponseEntity create(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(JsonNodeFactory.instance.objectNode().put("error", message));
    }
}
