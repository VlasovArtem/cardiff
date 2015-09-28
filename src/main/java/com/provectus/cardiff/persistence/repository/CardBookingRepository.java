package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.CardBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

/**
 * Created by artemvlasov on 21/08/15.
 */
public interface CardBookingRepository extends JpaRepository<CardBooking, Long> {

    @Query("select count(cb) from CardBooking cb where (cb.bookingEndDate between ?1 and ?2 or cb.bookingEndDate between ?1 and ?2 or ?1 between cb.bookingStartDate and cb.bookingEndDate or ?2 between cb.bookingStartDate and cb.bookingEndDate) and cb.discountCard.id = ?3")
    long countBookedCardsBetweenDates(LocalDate bookingStartDate, LocalDate bookingEndDate, long discountCardId);

    @EntityGraph(value = "CardBooking.personBookedTable", type = EntityGraph.EntityGraphType.LOAD)
    Page<CardBooking> findByPersonId(long id, Pageable pageable);

    @EntityGraph(value = "CardBooking.personBookingsTable", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select cb from CardBooking cb, DiscountCard dc where cb.discountCard = dc and dc.owner.id = ?1")
    Page<CardBooking> personDiscountCardBookings(long personId, Pageable pageable);

    @Query("SELECT CASE WHEN (COUNT(cb) > 0) THEN true ELSE false END FROM CardBooking cb WHERE " +
            "((cb.bookingStartDate > ?1 and cb.bookingStartDate > ?2) OR (cb.bookingEndDate < ?1 and cb.bookingEndDate < ?2)) AND cb.discountCard.id = ?3")
    boolean bookingAvailableBetweenDates(LocalDate bookingStartDate, LocalDate bookingEndDate, long discountCardId);
}
