package com.provectus.cardiff.controller;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.service.DiscountCardService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Дмитрий on 27/08/15.
 */
@RestController
@RequestMapping("/rest/card")
public class DiscountCardController {
    @Autowired
    private DiscountCardService service;

    @RequestMapping(path = "/add", method = POST, consumes = APPLICATION_JSON_VALUE)
    @RequiresAuthentication
    @ResponseStatus(OK)
    public ResponseEntity add(@RequestBody DiscountCard card) {
        try {
            service.add(card);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(FORBIDDEN)
                    .body(JsonNodeFactory.instance.objectNode().put("error", e.getMessage()));
        }
        return ResponseEntity
                .status(OK)
                .body(JsonNodeFactory.instance.objectNode().put("success", "Discount card successfully added"));
    }

    @RequestMapping(path = "/getbytags", method = GET, produces = APPLICATION_JSON_VALUE)
  //  @RequiresAuthentication
    public ResponseEntity add(@RequestParam Set<String> tags) {
        try {
            return ResponseEntity.ok(service.findByTags(tags));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(FORBIDDEN)
                    .body(JsonNodeFactory.instance.objectNode().put("error", e.getMessage()));
        }
    }


}
