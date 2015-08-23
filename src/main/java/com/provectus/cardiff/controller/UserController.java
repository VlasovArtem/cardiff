package com.provectus.cardiff.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.provectus.cardiff.entities.User;
import com.provectus.cardiff.service.impl.ServiceImpl;
import com.provectus.cardiff.utils.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ServiceImpl service;

    @RequestMapping(path = "/hello", method = GET, produces = {APPLICATION_JSON_VALUE})
    @ResponseBody
    public String helloWorld(Model model) {
        model.addAttribute("message", "Hello World!");
        return "Hello world!";
    }

    @RequestMapping(path = "/login",
            method = POST,
            consumes = APPLICATION_FORM_URLENCODED_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @JsonView(View.FirstLevel.class)
    public @ResponseBody ResponseEntity login(
            @RequestParam(name = "login_data") String loginData,
            @RequestParam(name = "password") String password) {
        try {
            return ResponseEntity.ok(service.loginUser(loginData, loginData, password));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(JsonNodeFactory.instance.objectNode().put("error", e.getMessage()));
        }
    }

    @RequestMapping(path = "/registration", method = POST, consumes = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity registration(@RequestBody User user) {
        try {
            service.userRegistration(user);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(JsonNodeFactory.instance.objectNode().put("error",e.getMessage()));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(JsonNodeFactory.instance.objectNode().put("success", "User successfully registered"));
    }
}
