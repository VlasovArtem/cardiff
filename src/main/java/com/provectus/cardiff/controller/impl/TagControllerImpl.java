package com.provectus.cardiff.controller.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.provectus.cardiff.controller.TagController;
import com.provectus.cardiff.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by blupashko on 26.08.2015.
 */

@Component
public class TagControllerImpl implements TagController {

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
}

