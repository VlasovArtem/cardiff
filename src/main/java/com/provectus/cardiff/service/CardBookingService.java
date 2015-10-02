package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.CardBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by artemvlasov on 25/09/15.
 */
public interface CardBookingService {
    void book (long discountCardId, LocalDate bookingStartDate);
    void cancel (long bookingId);
    void picked (long bookingId);
    void returned (long bookingId);
    Page<CardBooking> getPersonBookedDiscountCards (Pageable pageable);
    Page<CardBooking> getPersonDiscountCardBookings (Pageable pageable);
    List<CardBooking> getAll();
    void deleteBookCardById(long id);
}
