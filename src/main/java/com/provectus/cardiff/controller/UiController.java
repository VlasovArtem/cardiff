package com.provectus.cardiff.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by artemvlasov on 24/08/15.
 */
@Controller
public class UiController {
    @RequestMapping(value = {"/signin", "/signup", "/account"})
    public String redirect() {
        System.out.println("Forward");
        return "forward:/";
    }
}
