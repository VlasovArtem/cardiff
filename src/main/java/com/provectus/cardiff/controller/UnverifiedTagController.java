package com.provectus.cardiff.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.provectus.cardiff.entities.UnverifiedTag;
import com.provectus.cardiff.service.UnverifiedTagService;
import com.provectus.cardiff.utils.view.DiscountCardView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Дмитрий on 10.10.2015.
 */
@RestController
@RequestMapping("/rest/untag")
public class UnverifiedTagController {

    @Autowired
    private UnverifiedTagService service;

    @RequestMapping(path = "admin/get/page", method = GET)
    @ResponseStatus(value = OK)
    @JsonView(DiscountCardView.BasicLevel.class)
    public Page<UnverifiedTag> getAll(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "15", required = false) int size,
            @RequestParam(defaultValue = "DESC", required = false) String direction,
            @RequestParam(defaultValue = "createdDate", required = false) String property) {
        return service.getAll(new PageRequest(page, size, new Sort(Sort.Direction.valueOf(direction), property)));
    }

    @RequestMapping(path = "/admin/adopt", method = POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public void adoptTag(@RequestParam long tagId) {
        service.adoptTag(tagId);
    }
    @RequestMapping(path = "/delete", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public  void delete(@RequestParam long tagId) {
        service.delete(tagId);
    }
}
