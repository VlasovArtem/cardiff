package com.provectus.cardiff.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.provectus.cardiff.entities.CardBooking;
import com.provectus.cardiff.service.CardBookingService;
import com.provectus.cardiff.utils.view.CardBookingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

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
        service.book(discountCardId, LocalDate.parse(bookingStartDate));
    }

    @RequestMapping(value = "/booked", method = GET, produces = APPLICATION_JSON_VALUE)
    @JsonView(CardBookingView.BasicLevel.class)
    public Page<CardBooking> getBooked(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "15", required = false) int size,
            @RequestParam(defaultValue = "DESC", required = false) String direction,
            @RequestParam(defaultValue = "bookingStartDate", required = false) String property) {
        return service.getPersonBookedDiscountCards(new PageRequest(page, size, new Sort(Sort.Direction.valueOf
                (direction), property)));
    }

    @RequestMapping(value = "/bookings", method = GET, produces = APPLICATION_JSON_VALUE)
    @JsonView(CardBookingView.BookingsLevel.class)
    public Page<CardBooking> getPersonDiscountCardBookings(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "15", required = false) int size,
            @RequestParam(defaultValue = "DESC", required = false) String direction,
            @RequestParam(defaultValue = "bookingStartDate", required = false) String property) {
        return service.getPersonDiscountCardBookings(new PageRequest(page, size, new Sort(Sort.Direction.valueOf
                (direction), property)));
    }

    @RequestMapping(value = "/cancel/{bookingId}", method = PUT)
    @ResponseStatus(OK)
    public void cancel(@PathVariable long bookingId) {
        service.cancel(bookingId);
    }

    @RequestMapping(value = "/picked/{bookingId}", method = PUT)
    @ResponseStatus(OK)
    public void picked(@PathVariable long bookingId) {
        service.picked(bookingId);
    }

    @RequestMapping(value = "/returned/{bookingId}", method = PUT)
    @ResponseStatus(OK)
    public void returned(@PathVariable long bookingId) {
        service.returned(bookingId);
    }
}
