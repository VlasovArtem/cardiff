package com.provectus.cardiff.controller.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.provectus.cardiff.controller.DiscountCardController;
import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.service.DiscountCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

/**
 * Created by Дмитрий on 27/08/15.
 */
@Component
public class DiscountCardControllerImpl implements DiscountCardController {
    @Autowired
    private DiscountCardService service;
    @Override
    public ResponseEntity addition(@RequestBody DiscountCard card) {
        try {
            service.cardAdding(card);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(FORBIDDEN)
                    .body(JsonNodeFactory.instance.objectNode().put("error",e.getMessage()));
        }
        return ResponseEntity
                .status(OK)
                .body(JsonNodeFactory.instance.objectNode().put("success", "Discount card successfully added"));
    }


}
