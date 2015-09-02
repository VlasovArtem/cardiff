package com.provectus.cardiff.controller;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by blupashko on 31.08.2015.
 */

@RestController
@RequestMapping("/rest/tag")
public class TagController {

    @Autowired
    private TagService service;

    @RequestMapping(path = "/get", method = GET, produces = APPLICATION_JSON_VALUE)
    public  ResponseEntity getTag( @RequestParam(required = false) Long id) {
        try {
            service.getTag(id);
        } catch (Exception e) {
            return ResponseEntity.status(FORBIDDEN).body(JsonNodeFactory.instance.objectNode().put("error", e.getMessage()));
        }
        return ResponseEntity.ok(service.getTag(id));
    }

    @RequestMapping(path = "/add", method = POST, consumes = APPLICATION_JSON_VALUE,  produces = APPLICATION_JSON_VALUE)
    public ResponseEntity add(@RequestParam String tag) {
        try {
            service.addTag(tag);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(FORBIDDEN)
                    .body(JsonNodeFactory.instance.objectNode().put("error", e.getMessage()));
        }
        return ResponseEntity
                .status(OK)
                .body(JsonNodeFactory.instance.objectNode().put("success", "Tag successfully added"));
    }

}
