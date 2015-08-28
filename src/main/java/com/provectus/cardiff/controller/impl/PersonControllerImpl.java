package com.provectus.cardiff.controller.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.provectus.cardiff.controller.PersonController;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.HttpStatus.*;

/**
 * Created by artemvlasov on 27/08/15.
 */
@Component
public class PersonControllerImpl implements PersonController {
    @Autowired
    private PersonService service;
    private final static Logger LOGGER = LogManager.getLogger(PersonController.class);

    @Override
    public ResponseEntity login(String loginData, String password, boolean
            rememberMe) {
        try {
            return ResponseEntity.ok(service.loginPerson(loginData, password, rememberMe));
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity
                    .status(FORBIDDEN)
                    .body(JsonNodeFactory.instance.objectNode().put("error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity registration(@RequestBody Person person) {
        try {
            service.personRegistration(person);
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity
                    .status(FORBIDDEN)
                    .body(JsonNodeFactory.instance.objectNode().put("error",e.getMessage()));
        }
        return ResponseEntity
                .status(OK)
                .body(JsonNodeFactory.instance.objectNode().put("success", "Person successfully registered"));
    }

    @Override
    public ResponseEntity authentication() {
        try {
            service.authentication();
            return ResponseEntity.ok().build();
        } catch(AuthenticationException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(UNAUTHORIZED).body(JsonNodeFactory.instance.objectNode().put("error", e
                    .getMessage()));
        }
    }

    @Override
    public ResponseEntity authenticated() {
        try {
            return ResponseEntity.ok(service.authenticatedPerson());
        } catch (AuthenticationException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }
    }

    @Override
    public void logout() {
        service.logout();
    }

    @Override
    public ResponseEntity changePassword(String oldPassword,
                                         String newPassword) {
        try {
            service.changePassword(oldPassword, newPassword);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(CONFLICT).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity deletePerson(long id) {
        try {
            service.deletePersonById(id);
            return ResponseEntity.ok().build();
        } catch (AuthorizationServiceException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(UNAUTHORIZED).build();
        }
    }

    @Override
    public ResponseEntity updatePerson(@RequestBody Person person) {
        service.update(person);
        return ResponseEntity.ok().build();
    }
}
