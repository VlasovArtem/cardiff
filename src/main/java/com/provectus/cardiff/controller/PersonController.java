package com.provectus.cardiff.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import com.provectus.cardiff.service.PersonService;
import com.provectus.cardiff.utils.security.AuthenticatedPersonPrincipalUtil;
import com.provectus.cardiff.utils.view.PersonView;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by artemvlasov on 27/08/15.
 */
@RestController
@RequestMapping("/rest/person")
public class PersonController {
    @Autowired
    private PersonService service;
    private final static Logger LOGGER = Logger.getLogger(PersonController.class);

    @RequestMapping(path = "/login",
            method = POST,
            consumes = APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(value = OK)
    public void login(@RequestParam String loginData,
                      @RequestParam String password,
                      @RequestParam(required = false) boolean rememberMe) {}

    @RequestMapping(path = "/registration", method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity registration(@RequestBody Person person) {
        service.registration(person);
        return ResponseEntity
                .status(OK)
                .body(JsonNodeFactory.instance.objectNode().put("success", "Person successfully registered"));
    }

    /**
     * Check if use is already authenticated
     */
    @RequestMapping(path = "/authentication", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(value = OK)
    public ResponseEntity authentication() {
        if(AuthenticatedPersonPrincipalUtil.containAuthorities(PersonRole.ADMIN)) {
            return ResponseEntity.ok(JsonNodeFactory.instance.objectNode().put("role", "ADMIN"));
        } else {
            return ResponseEntity.ok(JsonNodeFactory.instance.objectNode().put("role", "USER"));
        }
    }

    /**
     * Find authenticated person
     * @return User
     */
    @RequestMapping(path = "/authenticated", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity authenticated() {
        return ResponseEntity.ok(service.authenticated());
    }

    @RequestMapping(path = "/logout", method = GET)
    public void logout() {}

    @RequestMapping(path = "/password/update",
            method = PUT,
            consumes = APPLICATION_FORM_URLENCODED_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity changePassword(@RequestParam String oldPassword,
                                         @RequestParam String newPassword) {
        service.changePassword(oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/admin/delete/{id:\\d*}", method = DELETE)
    public ResponseEntity delete(@PathVariable Long id, HttpServletRequest request) {
        service.delete(id, request);
        return ResponseEntity.status(OK).build();
    }

    @RequestMapping(path = "/update", method = PUT)
    @ResponseStatus(value = OK)
    public void update(@RequestBody Person person) {
        service.update(person);
    }

    @RequestMapping(path = "/admin/get/page", method = GET)
    @JsonView(PersonView.TableLevel.class)
    @ResponseStatus(value = OK)
    public Page<Person> getAll(@RequestParam(defaultValue = "0", required = false) int page,
                               @RequestParam(defaultValue = "15", required = false) int size,
                               @RequestParam(defaultValue = "DESC", required = false) String direction,
                               @RequestParam(defaultValue = "createdDate", required = false) String property) {
        return service.getAll(new PageRequest(page, size, new Sort(Sort.Direction.valueOf(direction), property)));
    }

    @RequestMapping(path = "/authorized", method = GET)
    public ResponseEntity personAuthorized(@RequestParam String hasRole) {
        if(!service.authorized(PersonRole.valueOf(hasRole))) {
            return ResponseEntity.status(FORBIDDEN).build();
        }
        return ResponseEntity.status(OK).build();
    }

    @RequestMapping(path = "/admin/restore/{id:\\d*}", method = PUT)
    @ResponseStatus(value = OK)
    public void restore(@PathVariable Long id) {
        service.restore(id);
    }

    @RequestMapping(path = "/admin/update/role/{id:\\d*}", method = PUT)
    @ResponseStatus(value = OK)
    public void changeRole(@PathVariable Long id) {
        service.changeRole(id);
    }

    @RequestMapping(path = "/check/email", method = POST)
    @ResponseStatus(value = OK)
    public void checkEmail(@RequestParam String email) {
        service.checkEmail(email);
    }

    @RequestMapping(path = "/check/login", method = POST)
    @ResponseStatus(value = OK)
    public void checkLogin(@RequestParam String login) {
        service.checkLogin(login);
    }

    @RequestMapping(path = "/check/phone", method = POST)
    @ResponseStatus(value = OK)
    public void checkPhoneNumber(@RequestParam long phone) {
        service.checkPhoneNumber(phone);
    }

    @RequestMapping(path = "/get/{cardId}", method = GET)
    @JsonView(PersonView.BasicLevel.class)
    public ResponseEntity find(@PathVariable long cardId) {
        Person person = service.find(cardId);
        if (person == null) {
            return ResponseEntity.status(NOT_FOUND).build();
        } else {
            return ResponseEntity.ok(person);
        }
    }
}
