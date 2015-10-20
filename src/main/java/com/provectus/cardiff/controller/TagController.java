package com.provectus.cardiff.controller;

import com.provectus.cardiff.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by blupashko on 31.08.2015.
 */

@RestController
@RequestMapping("/rest/tag")
public class TagController {

    @Autowired
    private TagService service;

    @RequestMapping(path = "/get/{tagId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public  ResponseEntity getTag (@PathVariable long tagId) {
        return ResponseEntity.ok(service.getTag(tagId));
    }

    @RequestMapping(path = "/get/all", method = GET, produces = APPLICATION_JSON_VALUE)
    public  ResponseEntity getAll () {
        return ResponseEntity.ok(service.findAll());
    }
}
