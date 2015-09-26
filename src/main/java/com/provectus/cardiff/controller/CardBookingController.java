package com.provectus.cardiff.controller;

import com.provectus.cardiff.service.CardBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by artemvlasov on 25/09/15.
 */
@RestController
@RequestMapping("/rest/card/booking")
public class CardBookingController {

    @Autowired
    private CardBookingService service;

    @RequestMapping(value = "/book", method = POST, consumes = APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(OK)
    public void book(@RequestParam long discountCardId,
                     @RequestParam(required = false) String bookingStartDate) {
        service.book(discountCardId, LocalDateTime.parse(bookingStartDate));
    }
}
