package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.CardBooking;
import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.persistence.repository.CardBookingRepository;
import com.provectus.cardiff.persistence.repository.DiscountCardRepository;
import com.provectus.cardiff.persistence.repository.PersonRepository;
import com.provectus.cardiff.service.CardBookingService;
import com.provectus.cardiff.utils.exception.card_booking.CardBookingException;
import com.provectus.cardiff.utils.security.AuthenticatedPersonPrincipalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by artemvlasov on 25/09/15.
 */
@Service
@Transactional
public class CardBookingServiceImpl implements CardBookingService {
    @Autowired
    private CardBookingRepository cardBookingRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private DiscountCardRepository discountCardRepository;

    @Override
    public void book(long discountCardId, LocalDateTime bookingStartDate) {
        CardBooking cardBooking = new CardBooking();
        cardBooking.setCreatedDate(bookingStartDate == null ? LocalDateTime.now() : bookingStartDate);
        if(!discountCardRepository.exists(discountCardId)) {
            throw new CardBookingException("Booked Discount card is not exists");
        }
        DiscountCard discountCard = discountCardRepository.findById(discountCardId).get();
        if(bookingStartDate == null || bookingStartDate.toLocalDate().isEqual(LocalDate.now())) {
            discountCard.setAvailable(false);
        }
        Person person = new Person();
        person.setId(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get().getId());
        cardBooking.setDiscountCard(discountCard);
        cardBooking.setPerson(person);
        cardBookingRepository.save(cardBooking);
    }
}
