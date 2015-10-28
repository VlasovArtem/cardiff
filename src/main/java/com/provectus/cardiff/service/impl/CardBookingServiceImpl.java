package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.CardBooking;
import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.entities.DiscountCardHistory;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import com.provectus.cardiff.persistence.repository.CardBookingRepository;
import com.provectus.cardiff.persistence.repository.DiscountCardHistoryRepository;
import com.provectus.cardiff.persistence.repository.DiscountCardRepository;
import com.provectus.cardiff.service.CardBookingService;
import com.provectus.cardiff.utils.exception.card_booking.CardBookingException;
import com.provectus.cardiff.utils.security.AuthenticatedPersonPrincipalUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

/**
 * Created by artemvlasov on 25/09/15.
 */
@Service
@Transactional
public class CardBookingServiceImpl implements CardBookingService {
    private final static Logger LOGGER = org.apache.log4j.Logger.getLogger(CardBookingServiceImpl.class);
    @Autowired
    private CardBookingRepository cardBookingRepository;
    @Autowired
    private DiscountCardRepository discountCardRepository;
    @Autowired
    private DiscountCardHistoryRepository discountCardHistoryRepository;

    @Override
    public void book (long discountCardId, LocalDate bookingStartDate) {
        LocalDate startDate = bookingStartDate == null ? LocalDate.now() : bookingStartDate;
        if(!discountCardRepository.exists(discountCardId)) {
            throw new CardBookingException("Booked Discount card is not exists or unavailable");
        } else if(discountCardRepository.personDiscountCard(discountCardId, AuthenticatedPersonPrincipalUtil
                .getAuthenticationPrincipal().get().getId())) {
            throw new CardBookingException("Booked Discount card belongs to authenticated person");
        } else if(cardBookingRepository.countByPersonIdAndDiscountCardId(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get().getId(), discountCardId) > 0) {
            throw new CardBookingException("Discount card is already booked by authenticated person");
        } else if(discountCardRepository.isPicked(discountCardId)) {
            throw new CardBookingException("Booked Discount card is already picked");
        } else if(startDate.isBefore(LocalDate.now())) {
            throw new CardBookingException("Booking start date, cannot be earlier than today date");
        } else if (cardBookingRepository.countBookedCardsBetweenDates(startDate, startDate.plusDays(6l),
                discountCardId) > 0) {
            throw new CardBookingException("Booking for this start and end date is not available");
        }
        LOGGER.info(String.format("Discount card with id - %d, successfully booked by person with id - %d", discountCardId, AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get().getId()));
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
    public void cancel (long bookingId) {
        if(!cardBookingRepository.checkPersonBookingCancel(bookingId, AuthenticatedPersonPrincipalUtil
                .getAuthenticationPrincipal().get().getId()) && !AuthenticatedPersonPrincipalUtil.containAuthorities
                (PersonRole.ADMIN)) {
            LOGGER.warn(String.format("Person with id - %d, try to remove booking with id - %d, without permission.",
                    AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get().getId(), bookingId));
            throw new CardBookingException("Person has no permission to cancel booking");
        } else if (cardBookingRepository.bookingDiscountCardIsPicked(bookingId)) {
            throw new CardBookingException("Canceled booking Discount card is already picked");
        }
        LOGGER.info(String.format("Person (id - %d) delete booking (id - %d)", AuthenticatedPersonPrincipalUtil
                .getAuthenticationPrincipal().get().getId(), bookingId));
        cardBookingRepository.delete(bookingId);
    }

    @Override
    public void picked (long bookingId) {
        if (cardBookingRepository.bookingDiscountCardIsPicked(bookingId)) {
            throw new CardBookingException("Discount card is already picked");
        } else if (!cardBookingRepository.checkPersonDiscountCardPick(bookingId, AuthenticatedPersonPrincipalUtil
                .getAuthenticationPrincipal().get().getId())) {
            throw new CardBookingException("Only owner of the card has ability to mark discount card as picked");
        }
        CardBooking cardBooking = cardBookingRepository.findOne(bookingId);
        cardBooking.getDiscountCard().setPicked(true);
        DiscountCardHistory discountCardHistory = new DiscountCardHistory();
        discountCardHistory.setDiscountCard(cardBooking.getDiscountCard());
        discountCardHistory.setPerson(cardBooking.getPerson());
        discountCardHistoryRepository.save(discountCardHistory);
    }

    @Override
    public void returned (long bookingId) {
        if (!cardBookingRepository.exists(bookingId)) {
            throw new CardBookingException("Booking with this id is not exists");
        } else if (!cardBookingRepository.bookingDiscountCardIsPicked(bookingId)) {
            throw new CardBookingException("Discount card is not picked");
        }
        CardBooking cardBooking = cardBookingRepository.findOne(bookingId);
        cardBooking.getDiscountCard().setPicked(false);
        DiscountCardHistory cardHistory = discountCardHistoryRepository
                .findByPersonIdAndDiscountCardIdAndReturnDateIsNull(cardBooking.getPerson().getId(), cardBooking
                        .getDiscountCard().getId());
        cardHistory.setReturnDate(LocalDateTime.now());
        cardBookingRepository.delete(bookingId);
    }

    /**
     * Return all {@link CardBooking} that was booked by authenticated {@link Person}.
     * @param pageable paging parameters.
     * @return {@link Page<CardBooking>}
     */
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

    @Override
    public List<CardBooking> getAll() {
        return cardBookingRepository.findAll();
    }

    @Override
    public void deleteBookCardById(long id) {
        cardBookingRepository.delete(id);
    }

    /**
     * Return available booking start date.
     * @param discountCardId Discount card id that should be booked
     * @return LocalDate available booking start date.
     */
    @Override
    public LocalDate getAvailableBookingDate(long discountCardId) {
        List<CardBooking> cardBookings = cardBookingRepository.findByDiscountCardIdOrderByBookingStartDateAsc(discountCardId);
        if(cardBookings.size() == 0 ) {
            return LocalDate.now();
        } else if(cardBookings.get(0).getBookingEndDate().isBefore(LocalDate.now())) {
            return LocalDate.now();
        } else if(cardBookings.size() == 1) {
            return cardBookings.get(0).getBookingEndDate().plusDays(1);
        }
        LocalDate availableBookingDate = null;
        int countOfCardBookings = 0;
        do {
            if(Period.between(
                    cardBookings.get(countOfCardBookings).getBookingEndDate(),
                    cardBookings.get(countOfCardBookings + 1).getBookingStartDate()).getDays() > 8) {
                availableBookingDate = cardBookings.get(countOfCardBookings).getBookingEndDate().plusDays(1l);
            }
            countOfCardBookings++;
        } while (availableBookingDate == null && countOfCardBookings != cardBookings.size() - 1);
        return availableBookingDate == null ? cardBookings.get(cardBookings.size() - 1).getBookingEndDate().plusDays(1l) : availableBookingDate;
    }
}
