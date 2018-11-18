package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import com.provectus.cardiff.entities.CardBooking;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by artemvlasov on 26/09/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DevelopmentDataSourceConfig.class, AppConfig.class, RootContextConfig.class})
@ActiveProfiles(profiles = "development")
@SqlGroup(value = {
        @Sql("/sql-data/drop-data.sql"),
        @Sql("/sql-data/location-data.sql"),
        @Sql("/sql-data/person-data.sql"),
        @Sql("/sql-data/discount-card-data.sql"),
        @Sql("/sql-data/card-booking-data.sql")
})
public class CardBookingRepositoryTest {
    @Autowired
    private CardBookingRepository cardBookingRepository;

    @Test
    public void
    countBookedCardsBetweenDatesWithFirstDateLessThanStartDateAndSecondDateGreaterThanCreatedDateAndLessThanEndDateTest() {
        long availableBookings = cardBookingRepository.countBookedCardsBetweenDates(
                LocalDate.of(2015, 10, 23),
                LocalDate.of(2015, 10, 30), 1l);
        assertEquals(1, availableBookings);
    }

    @Test
    public void
            countBookedCardsBetweenDatesWithFirstDateGreaterThanStartDateAndLessThanEndDateAndSecondDateGreaterEndDateTest() {
        long availableBookings = cardBookingRepository.countBookedCardsBetweenDates(
                LocalDate.of(2015, 10, 27),
                LocalDate.of(2015, 11, 3), 1l);
        assertEquals(1, availableBookings);
    }

    @Test
    public void countBookedCardsBetweenDatesWithFirstAndSecondDateGreaterThanStartDateAndLessThanEndDate() {
        long availableBookings = cardBookingRepository.countBookedCardsBetweenDates(
                LocalDate.of(2015, 10, 26),
                LocalDate.of(2015, 10, 31), 1l);
        assertEquals(1, availableBookings);
    }

    @Test
    public void countBookedCardsBetweenDatesLessThanStartDateTest() {
        long availableBookings = cardBookingRepository.countBookedCardsBetweenDates(
                LocalDate.of(2015, 10, 17),
                LocalDate.of(2015, 10, 24), 1l);
        assertEquals(0, availableBookings);
    }

    @Test
    public void countBookedCardsBetweenDatesGreaterThanEndDateTest() {
        long availableBookings = cardBookingRepository.countBookedCardsBetweenDates(
                LocalDate.of(2015, 11, 2),
                LocalDate.of(2015, 11, 9), 1l);
        assertEquals(0, availableBookings);
    }

    @Test
    public void findByPersonIdTest() {
        Page<CardBooking> bookingPage = cardBookingRepository.findByPersonId(1l, new PageRequest(0, 15, new Sort(Sort
                .Direction.DESC, "bookingStartDate")));
        assertNotNull(bookingPage);
        assertThat(bookingPage.getTotalElements(), is(1l));
        assertThat(bookingPage.getContent().get(0).getId(), is(1l));
    }

    @Test
    public void findByPersonIdWithoutCardBookingsTest() {
        Page<CardBooking> bookingPage = cardBookingRepository.findByPersonId(4l, new PageRequest(0, 15, new Sort(Sort
                .Direction.DESC, "bookingStartDate")));
        assertThat(bookingPage.getTotalElements(), is(0l));
    }

    @Test
    public void personDiscountCardBookingsTest() {
        Page<CardBooking> bookingPage = cardBookingRepository.personDiscountCardBookings(1l, new PageRequest(0, 15, new Sort(Sort
                .Direction.DESC, "bookingStartDate")));
        assertThat(bookingPage.getTotalElements(), is(1l));
        assertThat(bookingPage.getContent().get(0).getPerson().getEmail(), is("dmitriyvalnov@gmail.com"));
    }

    @Test
    public void bookingAvailableBetweenDatesLessThanBookingStartDateTest() {
        boolean available = cardBookingRepository.bookingAvailableBetweenDates(LocalDate.of(2015, 10, 17), LocalDate
                .of(2015, 10, 24), 1l);
        assertTrue(available);
    }

    @Test
    public void bookingAvailableBetweenDateGreaterThanBookingEndDateTest() {
        boolean available = cardBookingRepository.bookingAvailableBetweenDates(LocalDate.of(2015, 11, 2), LocalDate
                .of(2015, 11, 9), 1l);
        assertTrue(available);
    }

    @Test
    public void bookingAvailableBetweenDatesInvalidDatesTest() {
        boolean available = cardBookingRepository.bookingAvailableBetweenDates(LocalDate.of(2015, 10, 19), LocalDate
                .of(2015, 10, 26), 1l);
        assertFalse(available);
    }

    @Test
    public void checkPersonBookingCancelTest() {
        assertTrue(cardBookingRepository.checkPersonBookingCancel(1l, 1l));
        assertTrue(cardBookingRepository.checkPersonBookingCancel(1l, 2l));
    }

    @Test
    public void bookingDiscountCardIsPickedTest() {
        assertTrue(cardBookingRepository.bookingDiscountCardIsPicked(1l));
    }

    @Test
    public void bookingDiscountCardIsPickedNotTest() {
        assertFalse(cardBookingRepository.bookingDiscountCardIsPicked(2l));
    }

    @Test
    public void checkPersonDiscountCardPickTest() {
        assertTrue(cardBookingRepository.checkPersonDiscountCardPick(1l, 2l));
    }

    @Test
    public void checkPersonDiscountCardPickNotMathcesDataTest() {
        assertFalse(cardBookingRepository.checkPersonDiscountCardPick(1l, 1l));
    }
}
