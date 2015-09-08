package com.provectus.cardiff.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.service.DiscountCardService;
import com.provectus.cardiff.utils.View;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

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

    @RequestMapping(path = "/getByNumber", method = GET, produces = APPLICATION_JSON_VALUE)
    @RequiresAuthentication
    public  ResponseEntity getByCardNumber( @RequestParam(required = false) Long number) {
        try {
            service.findCardByNumber(number);
        } catch (Exception e) {
            return ResponseEntity.status(FORBIDDEN).body(JsonNodeFactory.instance.objectNode().put("error", e.getMessage()));
        }
        return ResponseEntity.ok(service.findCardByNumber(number));
    }

    @RequestMapping(path = "/update", method = PUT)
    @RequiresAuthentication
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    @ResponseStatus(value = OK)
    public  void update( @RequestBody DiscountCard card) {
        service.update(card);
    }

    @RequestMapping(path = "/delete", method = DELETE, produces = APPLICATION_JSON_VALUE)
    @RequiresAuthentication
    public  void delete( @RequestBody DiscountCard card) {
        service.delete(card);
    }
    @RequestMapping(path = "/getbytags", method = GET, produces = APPLICATION_JSON_VALUE)
    @RequiresAuthentication
    public ResponseEntity getByTags(@RequestParam Set<String> tags) {
        try {
            return ResponseEntity.ok(service.findByTags(tags));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(FORBIDDEN)
                    .body(JsonNodeFactory.instance.objectNode().put("error", e.getMessage()));
        }
    }

    @RequestMapping(path = "/getbyname", method = GET, produces = APPLICATION_JSON_VALUE)
    @RequiresAuthentication
    public ResponseEntity getByName(@RequestParam String name) {
        try {
            return ResponseEntity.ok(service.findByName(name.toLowerCase()));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(FORBIDDEN)
                    .body(JsonNodeFactory.instance.objectNode().put("error", e.getMessage()));
        }
    }

    @RequestMapping(path = "/get/all", method = GET)
    @RequiresRoles("ADMIN")
    @JsonView(View.FirstLevel.class)
    @ResponseStatus(value = OK)
    public Page<DiscountCard> getAll(@RequestParam(defaultValue = "0", required = false) int page,
                                     @RequestParam(defaultValue = "15", required = false) int size,
                                     @RequestParam(defaultValue = "DESC", required = false) String direction,
                                     @RequestParam(defaultValue = "createdDate", required = false) String property) {
        return service.getAll(new PageRequest(page, size, new Sort(Sort.Direction.valueOf(direction), property)));
    }


}
