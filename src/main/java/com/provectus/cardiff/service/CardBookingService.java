package com.provectus.cardiff.service;

import java.time.LocalDateTime;

/**
 * Created by artemvlasov on 25/09/15.
 */
public interface CardBookingService {
    void book(long discountCardId, LocalDateTime bookingStartDate);
}
