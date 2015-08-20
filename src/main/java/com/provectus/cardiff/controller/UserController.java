package com.provectus.cardiff.controller;

import com.provectus.cardiff.service.impl.ServiceImpl;
import com.provectus.cardiff.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ServiceImpl service;

    @RequestMapping(path = "/hello", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String helloWorld(Model model) {
        model.addAttribute("message", "Hello World!");
        return "Hello world!";
    }

    @RequestMapping(path = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<User> getAll() {
        return service.getUsers();
    }
}
