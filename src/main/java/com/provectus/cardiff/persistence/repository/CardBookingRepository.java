package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.CardBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

/**
 * Created by artemvlasov on 21/08/15.
 */
public interface CardBookingRepository extends JpaRepository<CardBooking, Long> {

    @Query("select count(cb) from CardBooking cb where (cb.createdDate between ?1 and ?2 or cb.bookingEndDate between ?1 and ?2 or ?1 between cb.createdDate and cb.bookingEndDate or ?2 between cb.createdDate and cb.bookingEndDate) and cb.id = ?3")
    long countBookedCardsBetweenDates(LocalDate bookingStartDate, LocalDate bookingEndDate, long discountCardId);
}
