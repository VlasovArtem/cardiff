package com.provectus.cardiff.controller;

import com.provectus.cardiff.entities.DiscountCard;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Дмитрий on 27.08.2015.
 */
@RestController
@RequestMapping("/card")
public interface DiscountCardController {
    @RequestMapping(path = "/addition", method = POST, consumes = APPLICATION_JSON_VALUE)
    @RequiresAuthentication
    public ResponseEntity addition(@RequestBody DiscountCard card);
}
