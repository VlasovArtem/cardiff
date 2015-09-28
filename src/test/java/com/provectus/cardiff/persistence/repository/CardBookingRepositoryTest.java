package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * Created by artemvlasov on 26/09/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DevelopmentDataSourceConfig.class, AppConfig.class, RootContextConfig.class})
@SqlGroup(value = {
        @Sql("/sql-data/drop-data.sql"),
        @Sql("/sql-data/card-booking-data.sql")
})
@ActiveProfiles(profiles = "development")
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
}
