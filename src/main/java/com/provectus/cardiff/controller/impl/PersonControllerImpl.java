package com.provectus.cardiff.controller.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.provectus.cardiff.controller.PersonController;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import com.provectus.cardiff.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static com.provectus.cardiff.utils.ResponseEntityExceptionCreator.create;
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
        service.loginPerson(loginData, password, rememberMe);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity registration(@RequestBody Person person) {
        service.personRegistration(person);
        return ResponseEntity
                .status(OK)
                .body(JsonNodeFactory.instance.objectNode().put("success", "Person successfully registered"));
    }

    @Override
    public ResponseEntity authentication() {
        service.authentication();
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity authenticated() {
        try {
            return ResponseEntity.ok(service.authenticatedPerson());
        } catch (AuthenticationException e) {
            return create(NOT_FOUND, "Person is not authenticated");
        }
    }

    @Override
    public void logout() {
        service.logout();
    }

    @Override
    public ResponseEntity changePassword(String oldPassword,
                                         String newPassword, Exception ex) {
        if(ex != null) {
            return create(FORBIDDEN, ex.getMessage());
        }
        service.changePassword(oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity deletePerson(long id) {
        service.deletePerson(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity updatePerson(@RequestBody Person person) {
        service.update(person);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity getAll(@RequestParam(defaultValue = "createdDate", required = false) String property,
                                 @RequestParam(defaultValue = "ASC", required = false) String direction) {
        Sort sort = new Sort(Sort.Direction.valueOf(direction), property);
        return ResponseEntity.ok(service.personAdminPanel(sort));
    }

    @Override
    public ResponseEntity personAuthorized(@RequestParam(required = false) String role) {
        service.authorized(PersonRole.valueOf(role));
        return ResponseEntity.ok().build();
    }
}
