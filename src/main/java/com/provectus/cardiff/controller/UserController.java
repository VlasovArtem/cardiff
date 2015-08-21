package com.provectus.cardiff.controller;

import com.provectus.cardiff.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
}
