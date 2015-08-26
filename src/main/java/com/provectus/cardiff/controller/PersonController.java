package com.provectus.cardiff.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.service.ServiceCardiff;
import com.provectus.cardiff.utils.View;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by artemvlasov on 20/08/15.
 */
@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private ServiceCardiff service;

    @RequestMapping(path = "/login",
            method = POST,
            consumes = APPLICATION_FORM_URLENCODED_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @JsonView(View.FirstLevel.class)
    public ResponseEntity login(
            @RequestParam(name = "login_data") String loginData,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "remember_me") boolean rememberMe) {
        try {
            return ResponseEntity.ok(service.loginPerson(loginData, password, rememberMe));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(FORBIDDEN)
                    .body(JsonNodeFactory.instance.objectNode().put("error", e.getMessage()));
        }
    }

    @RequestMapping(path = "/registration", method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity registration(@RequestBody Person person) {
        try {
            service.personRegistration(person);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(FORBIDDEN)
                    .body(JsonNodeFactory.instance.objectNode().put("error",e.getMessage()));
        }
        return ResponseEntity
                .status(OK)
                .body(JsonNodeFactory.instance.objectNode().put("success", "Person successfully registered"));
    }

    /**
     * Check if use is already authenticated
     * @return ResponseEntity with status ok or unauthorized
     */
    @RequestMapping(path = "/authentication", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity authentication() {
        try {
            service.authentication();
            return ResponseEntity.ok().build();
        } catch(AuthenticationException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(JsonNodeFactory.instance.objectNode().put("error", e
                    .getMessage()));
        }
    }

    /**
     * Find authenticated person
     * @return User
     */
    @RequestMapping(path = "/authenticated", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity authenticated() {
        try {
            return ResponseEntity.ok(service.authenticatedPerson());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }
    }

    @RequestMapping(path = "/logout", method = GET)
    public void logout() {
        service.logout();
    }

    @RequestMapping(path = "/password/update",
            method = POST,
            consumes = APPLICATION_FORM_URLENCODED_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity changePassword(
            @RequestParam(name = "old_password", required = false) String oldPassword,
            @RequestParam(name = "new_password", required = false) String newPassword) {
        try {
            service.changePassword(oldPassword, newPassword);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(CONFLICT).body(e.getMessage());
        }
    }
}
