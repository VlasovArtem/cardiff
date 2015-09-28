package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.CardBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/**
 * Created by artemvlasov on 25/09/15.
 */
public interface CardBookingService {
    void book(long discountCardId, LocalDate bookingStartDate);
    Page<CardBooking> getPersonBookedDiscountCards(Pageable pageable);
    Page<CardBooking> getPersonDiscountCardBookings(Pageable pageable);
}
