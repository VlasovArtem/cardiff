package com.provectus.cardiff.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.entities.Tag;
import com.provectus.cardiff.service.DiscountCardService;
import com.provectus.cardiff.utils.SearchEngine;
import com.provectus.cardiff.utils.view.DiscountCardView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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
    public void add (@RequestBody DiscountCard card) {
        service.add(card);
    }

    @RequestMapping(path = "/get/by/number", method = GET, produces = APPLICATION_JSON_VALUE)
    @JsonView(DiscountCardView.BasicLevel.class)
    public ResponseEntity getByCardNumber (@RequestParam(required = true) long number) {
        Optional<DiscountCard> discountCards = service.searchByCardNumber (number);
        if(discountCards.isPresent()) {
            return ResponseEntity.ok(discountCards.get());
        }
        return ResponseEntity.status(NOT_FOUND).build();
    }

    @RequestMapping(path = "/update", method = PUT)
    @ResponseStatus(value = OK)
    public void update (@RequestBody DiscountCard card) {
        service.update(card);
    }

    @RequestMapping(path = "/delete", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public void delete (@RequestParam long cardId) {
        service.delete(cardId);
    }

    @RequestMapping(path = "/get/by/tags", method = GET, produces = APPLICATION_JSON_VALUE)
    @JsonView(DiscountCardView.BasicLevel.class)
    public ResponseEntity getByTags (@RequestParam(required = true) Set<String> tags) {
        Optional<List<DiscountCard>> discountCards = service.searchByTags(tags);
        if(discountCards.isPresent()) {
            return ResponseEntity.ok(discountCards.get());
        }
        return ResponseEntity.status(NOT_FOUND).build();
    }

    @RequestMapping(path = "/get/by/name", method = GET, produces = APPLICATION_JSON_VALUE)
    @JsonView(DiscountCardView.BasicLevel.class)
    public ResponseEntity getByName (@RequestParam(required = true, value = "company_name") String companyName) {
        List<DiscountCard> discountCards = SearchEngine.search(service.findAll(), companyName, service
                .searchByCompanyName (companyName));

        if(discountCards.size() > 0) {
            return ResponseEntity.ok(discountCards);
        }
        return ResponseEntity.status(NOT_FOUND).build();
    }

    @RequestMapping(path = "/get/page", method = GET)
    @ResponseStatus(value = OK)
    @JsonView(DiscountCardView.BasicLevel.class)
    public Page<DiscountCard> getAll(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "15", required = false) int size,
            @RequestParam(defaultValue = "DESC", required = false) String direction,
            @RequestParam(defaultValue = "createdDate", required = false) String property) {
        return service.getAll(new PageRequest(page, size, new Sort(Sort.Direction.valueOf(direction), property)));
    }

    @RequestMapping(path = "/get/page/search", method = GET)
    @ResponseStatus(value = OK)
    @JsonView(DiscountCardView.BasicLevel.class)
    public Page<DiscountCard> getAll(
            @RequestParam(required = false) Set<String> tags,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) Long locationId,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "15", required = false) int size,
            @RequestParam(defaultValue = "DESC", required = false) String direction,
            @RequestParam(defaultValue = "createdDate", required = false) String property) {
        return service.getAll(tags, companyName, locationId, new PageRequest(page, size, new Sort(Sort.Direction.valueOf(direction), property)));
    }

    @RequestMapping(path = "/get/{cardId}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public ResponseEntity findAvailable (@PathVariable long cardId) {
        Optional<DiscountCard> dc = service.getCard(cardId);
        if(!dc.isPresent()) {
            return ResponseEntity.status(NOT_FOUND).build();
        }
        return ResponseEntity.ok(dc.get());
    }

    @RequestMapping(path = "/owner/page", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @JsonView(DiscountCardView.BasicLevel.class)
    public Page<DiscountCard> getAuthenticatedPersonDiscountCards (
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "15", required = false) int size,
            @RequestParam(defaultValue = "DESC", required = false) String direction,
            @RequestParam(defaultValue = "createdDate", required = false) String property) {
        return service.getAuthenticatedPersonDiscountCards(new PageRequest(page, size, new Sort(Sort.Direction.valueOf(direction), property)));
    }

    @RequestMapping(path = "/check", method = GET)
    @ResponseStatus(OK)
    public void checkDiscountCard (@RequestParam long cardNumber, @RequestParam String
            companyName) {
        service.checkDiscountCardIsUnique(cardNumber, companyName);
    }

    @RequestMapping(path = "/auth/{cardId}", method = GET)
    public ResponseEntity authPersonDiscountCard(@PathVariable long cardId) {
        return ResponseEntity.ok(JsonNodeFactory.instance.objectNode().put("authPersonCard" ,service
                .authPersonDiscountCard(cardId)));
    }

    @RequestMapping(path = "/count", method = GET)
    public long count() {
        return service.count();
    }

    @RequestMapping(path = "/top", method = GET)
    @JsonView(DiscountCardView.BasicLevel.class)
    public ResponseEntity top5() {
        return ResponseEntity.ok(service.top5());
    }
}
