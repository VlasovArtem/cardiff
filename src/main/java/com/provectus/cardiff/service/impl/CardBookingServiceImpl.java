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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public void book(long discountCardId, LocalDate bookingStartDate) {
        LocalDate startDate = bookingStartDate == null ? LocalDate.now() : bookingStartDate;
        if(!discountCardRepository.exists(discountCardId)) {
            throw new CardBookingException("Booked Discount card is not exists or unavailable");
        } else if(discountCardRepository.personDiscountCard(discountCardId, AuthenticatedPersonPrincipalUtil
                .getAuthenticationPrincipal().get().getId())) {
            throw new CardBookingException("Booked Discount card belongs to authenticated person");
        } else if(discountCardRepository.isPicked(discountCardId)) {
            throw new CardBookingException("Booked Discount card is already picked");
        } else if(startDate.isBefore(LocalDateTime.now().toLocalDate())) {
            throw new CardBookingException("Booking start date, cannot be earlier than today date");
        } else if (cardBookingRepository.countBookedCardsBetweenDates(startDate, startDate.plusDays(7l),
                discountCardId) > 0) {
            throw new CardBookingException("Booking for this start and end date is not available");
        }
        CardBooking cardBooking = new CardBooking();
        cardBooking.setBookingStartDate(startDate);

        DiscountCard discountCard = new DiscountCard();
        discountCard.setId(discountCardId);
        Person person = new Person();
        person.setId(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get().getId());
        cardBooking.setDiscountCard(discountCard);
        cardBooking.setPerson(person);
        cardBookingRepository.save(cardBooking);
    }

    @Override
    public Page<CardBooking> getPersonBookedDiscountCards(Pageable pageable) {
        return cardBookingRepository.findByPersonId(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get
                ().getId(), pageable);
    }

    @Override
    public Page<CardBooking> getPersonDiscountCardBookings(Pageable pageable) {
        return cardBookingRepository.personDiscountCardBookings(AuthenticatedPersonPrincipalUtil
                .getAuthenticationPrincipal().get().getId(), pageable);
    }
}
