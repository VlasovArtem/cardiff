package com.provectus.cardiff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by blupashko on 31.08.2015.
 */

@RestController
@RequestMapping("/tag")
public interface TagController {

    @Autowired
    @RequestMapping(path = "/get", method = GET, produces = APPLICATION_JSON_VALUE)

    ResponseEntity getTag(@RequestParam(required = false) Long id);
}
