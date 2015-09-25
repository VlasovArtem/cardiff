package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.CardBooking;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by artemvlasov on 21/08/15.
 */
public interface CardBookingRepository extends JpaRepository<CardBooking, Long> {
}
