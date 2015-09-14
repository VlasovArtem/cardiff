package com.provectus.cardiff.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.service.DiscountCardService;
import com.provectus.cardiff.utils.view.DiscountCardView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.access.vote.AuthenticatedVoter.IS_AUTHENTICATED_FULLY;
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
    @ResponseStatus(OK)
    @Secured({"ADMIN", "USER"})
    public void add(@RequestBody DiscountCard card) {
        service.add(card);
    }

    @RequestMapping(path = "/get/by/number", method = GET, produces = APPLICATION_JSON_VALUE)
    @JsonView(DiscountCardView.BasicLevel.class)
    public ResponseEntity getByCardNumber(@RequestParam(required = true) long number) {
        Optional<DiscountCard> discountCards = service.search(number);
        if(discountCards.isPresent()) {
            return ResponseEntity.ok(discountCards);
        }
        return ResponseEntity.status(NOT_FOUND).build();
    }

    @RequestMapping(path = "/update", method = PUT)
    @ResponseStatus(value = OK)
    @Secured(IS_AUTHENTICATED_FULLY)
    public  void update(@RequestBody DiscountCard card) {
        service.update(card);
    }

    @RequestMapping(path = "/delete", method = DELETE, produces = APPLICATION_JSON_VALUE)
    @Secured("ADMIN")
    public  void delete(@RequestBody DiscountCard card) {
        service.delete(card);
    }

    @RequestMapping(path = "/get/by/tags", method = GET, produces = APPLICATION_JSON_VALUE)
    @JsonView(DiscountCardView.BasicLevel.class)
    public ResponseEntity getByTags(@RequestParam(required = true) Set<String> tags) {
        Optional<List<DiscountCard>> discountCards = service.search(tags);
        if(discountCards.isPresent()) {
            return ResponseEntity.ok(discountCards);
        }
        return ResponseEntity.status(NOT_FOUND).build();
    }

    @RequestMapping(path = "/get/by/name", method = GET, produces = APPLICATION_JSON_VALUE)
    @JsonView(DiscountCardView.BasicLevel.class)
    public ResponseEntity getByName(@RequestParam(required = true, value = "company_name") String companyName) {
        Optional<List<DiscountCard>> discountCards = service.search(companyName);
        if(discountCards.isPresent()) {
            return ResponseEntity.ok(discountCards.get());
        }
        return ResponseEntity.status(NOT_FOUND).build();
    }

    @RequestMapping(path = "/get/all", method = GET)
    @JsonView(DiscountCardView.BasicLevel.class)
    @ResponseStatus(value = OK)
    public Page<DiscountCard> getAll(@RequestParam(defaultValue = "0", required = false) int page,
                                     @RequestParam(defaultValue = "15", required = false) int size,
                                     @RequestParam(defaultValue = "DESC", required = false) String direction,
                                     @RequestParam(defaultValue = "createdDate", required = false) String property) {
        return service.getAll(new PageRequest(page, size, new Sort(Sort.Direction.valueOf(direction), property)));
    }

    @RequestMapping(path = "/get/{cardId}/available", method = GET)
    @ResponseStatus(OK)
    public Object findAvailable(@PathVariable long cardId) {
        return service.findAvailable(cardId);
    }

}
