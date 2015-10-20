package com.provectus.cardiff.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.provectus.cardiff.entities.CustomTag;
import com.provectus.cardiff.service.CustomTagService;
import com.provectus.cardiff.utils.view.PersonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Дмитрий on 10.10.2015.
 */
@RestController
@RequestMapping("/rest/tag/custom")
public class CustomTagController {

    @Autowired
    private CustomTagService service;

    @RequestMapping(path = "/add", method = POST)
    @ResponseStatus(OK)
    public void addTag (@RequestBody CustomTag customTag) {
        service.addTag(customTag);
    }

    @RequestMapping(path = "/admin/get/page", method = GET)
    @ResponseStatus(value = OK)
    @JsonView(PersonView.TableLevel.class)
    public Page<CustomTag> getAll (
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "15", required = false) int size,
            @RequestParam(defaultValue = "DESC", required = false) String direction,
            @RequestParam(defaultValue = "createdDate", required = false) String property) {
        return service.getAll(new PageRequest(page, size, new Sort(Sort.Direction.valueOf(direction), property)));
    }

    @RequestMapping(path = "/admin/accept/{tagId}", method = PUT, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public void acceptTag (@PathVariable long tagId) {
        service.acceptTag(tagId);
    }

    @RequestMapping(path = "/admin/delete/{tagId}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public void delete (@RequestParam long tagId) {
        service.delete(tagId);
    }

    @RequestMapping(path = "/admin/count", method = GET)
    public ResponseEntity countUnAcceptedTags () {
        return ResponseEntity.ok(JsonNodeFactory.instance.objectNode().put("count",service.countUnacceptedTags()));
    }
}
