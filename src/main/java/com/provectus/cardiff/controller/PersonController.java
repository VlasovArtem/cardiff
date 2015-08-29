package com.provectus.cardiff.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import com.provectus.cardiff.utils.View;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by artemvlasov on 20/08/15.
 */
@RestController
@RequestMapping("/rest/person")
public interface PersonController {

    @RequestMapping(path = "/login",
            method = POST,
            consumes = APPLICATION_FORM_URLENCODED_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @JsonView(View.FirstLevel.class)
    @RequiresGuest
    ResponseEntity login(
            @RequestParam String loginData,
            @RequestParam String password,
            @RequestParam boolean rememberMe);

    @RequestMapping(path = "/registration", method = POST, consumes = APPLICATION_JSON_VALUE)
    @RequiresGuest
    ResponseEntity registration(@RequestBody Person person);

    /**
     * Check if use is already authenticated
     * @return ResponseEntity with status ok or unauthorized
     */
    @RequestMapping(path = "/authentication", method = GET, produces = APPLICATION_JSON_VALUE)
    @RequiresRoles(value = {"USER", "ADMIN"})
    ResponseEntity authentication();

    /**
     * Find authenticated person
     * @return User
     */
    @RequestMapping(path = "/authenticated", method = GET, produces = APPLICATION_JSON_VALUE)
    @RequiresAuthentication
    ResponseEntity authenticated();

    @RequestMapping(path = "/logout", method = GET)
    @RequiresAuthentication
    void logout();

    @RequestMapping(path = "/password/update",
            method = PUT,
            consumes = APPLICATION_FORM_URLENCODED_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @RequiresAuthentication
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword, Exception ex);

    @RequestMapping(path = "/delete/{id:\\d}",
            method = PUT)
    @RequiresRoles("ADMIN")
    ResponseEntity deletePerson(@RequestParam long id);

    @RequestMapping(path = "/update",
            method = PUT)
    @RequiresAuthentication
    ResponseEntity updatePerson(@RequestBody Person person);

    @RequestMapping(path = "/admin",
            method = GET,
            produces = APPLICATION_JSON_VALUE)
    @RequiresRoles("ADMIN")
    @JsonView(View.FirstLevel.class)
    ResponseEntity getAll(@RequestParam(defaultValue = "createdDate") String property, @RequestParam(defaultValue =
            "ASC") String direction);

    @RequestMapping(path = "/authorized",
            method = GET,
            consumes = APPLICATION_JSON_VALUE)
    @RequiresAuthentication
    ResponseEntity personAuthorized(@RequestParam PersonRole role);
}
