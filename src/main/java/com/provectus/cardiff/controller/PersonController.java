package com.provectus.cardiff.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import com.provectus.cardiff.service.PersonService;
import com.provectus.cardiff.utils.view.PersonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.provectus.cardiff.utils.ResponseEntityExceptionCreator.create;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
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
    private final static Logger LOGGER = LogManager.getLogger(PersonController.class);

    @RequestMapping(path = "/login",
            method = POST,
            consumes = APPLICATION_FORM_URLENCODED_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @RequiresGuest
    @ResponseStatus(value = OK)
    public void login(@RequestParam String loginData,
                      @RequestParam String password,
                      @RequestParam(defaultValue = "false") boolean rememberMe) {
        service.login(loginData, password, rememberMe);
    }

    @RequestMapping(path = "/registration", method = POST, consumes = APPLICATION_JSON_VALUE)
    @RequiresGuest
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
    @RequiresRoles(value = {"USER", "ADMIN"})
    @ResponseStatus(value = OK)
    public void authentication() {
        service.authentication();
    }

    /**
     * Find authenticated person
     * @return User
     */
    @RequestMapping(path = "/authenticated", method = GET, produces = APPLICATION_JSON_VALUE)
    @RequiresAuthentication
    public ResponseEntity authenticated() {
        return ResponseEntity.ok(service.authenticated());
    }

    @RequestMapping(path = "/logout", method = GET)
    @RequiresAuthentication
    public void logout() {
        service.logout();
    }

    @RequestMapping(path = "/password/update",
            method = PUT,
            consumes = APPLICATION_FORM_URLENCODED_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @RequiresAuthentication
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity changePassword(@RequestParam String oldPassword,
                                         @RequestParam String newPassword, Exception ex) {
        if(ex != null) {
            return create(FORBIDDEN, ex.getMessage());
        }
        service.changePassword(oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/delete/{id:\\d*}",
            method = DELETE)
    @RequiresRoles("ADMIN")
    @ResponseStatus(value = OK)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @RequestMapping(path = "/update",
            method = PUT)
    @RequiresAuthentication
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    @ResponseStatus(value = OK)
    public void update(@RequestBody Person person) {
        service.update(person);
    }

    @RequestMapping(path = "/admin",
            method = GET)
    @RequiresRoles("ADMIN")
    @JsonView(PersonView.TableLevel.class)
    @ResponseStatus(value = OK)
    public Page<Person> getAll(@RequestParam(defaultValue = "0", required = false) int page,
                               @RequestParam(defaultValue = "15", required = false) int size,
                               @RequestParam(defaultValue = "DESC", required = false) String direction,
                               @RequestParam(defaultValue = "createdDate", required = false) String property) {
        return service.getAll(new PageRequest(page, size, new Sort(Sort.Direction.valueOf(direction), property)));
    }

    @RequestMapping(path = "/authorized",
            method = GET)
    @RequiresAuthentication
    @ResponseStatus(value = OK)
    public void personAuthorized(@RequestParam(required = false) String hasRole) {
        service.authorized(PersonRole.valueOf(hasRole));
    }

    @RequestMapping(path = "/restore/{id:\\d*}", method = PUT)
    @RequiresRoles("ADMIN")
    @ResponseStatus(value = OK)
    public void restore(@PathVariable Long id) {
        service.restore(id);
    }

    @RequestMapping(path = "/update/role/{id:\\d*}", method = PUT)
    @RequiresRoles("ADMIN")
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
    @RequiresAuthentication
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
