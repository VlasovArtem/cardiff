package com.provectus.cardiff.service;

import com.provectus.cardiff.WithMockCardiffPerson;
import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.CardiffAppInitializer;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import com.provectus.cardiff.config.security.SecurityConfig;
import com.provectus.cardiff.entities.CardBooking;
import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.entities.DiscountCardHistory;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.persistence.repository.CardBookingRepository;
import com.provectus.cardiff.persistence.repository.DiscountCardHistoryRepository;
import com.provectus.cardiff.persistence.repository.DiscountCardRepository;
import com.provectus.cardiff.utils.exception.card_booking.CardBookingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.smartcardio.Card;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by artemvlasov on 30/09/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        DevelopmentDataSourceConfig.class,
        CardiffAppInitializer.class,
        AppConfig.class,
        RootContextConfig.class, SecurityConfig.class})
@ActiveProfiles(profiles = "development")
@Transactional
@WebAppConfiguration
@SqlGroup(value = {
        @Sql("/sql-data/drop-data.sql"),
        @Sql("/sql-data/location-data.sql"),
        @Sql("/sql-data/person-data.sql"),
        @Sql("/sql-data/discount-card-data.sql"),
        @Sql("/sql-data/card-booking-data.sql")
})
public class CardBookingServiceImplTest {
    @Autowired
    private CardBookingService service;
    @Autowired
    private CardBookingRepository cardBookingRepository;
    @Autowired
    private DiscountCardHistoryRepository discountCardHistoryRepository;
    @Autowired
    private DiscountCardRepository discountCardRepository;

    @Test
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void bookTest() {
        long bookingCount = cardBookingRepository.count();
        service.book(4l, LocalDate.of(2015, 11, 2));
        assertThat(cardBookingRepository.count(), is(bookingCount + 1));
        List<CardBooking> cardBookingList = cardBookingRepository.findAll();
        CardBooking cardBooking = cardBookingList.get(cardBookingList.size() - 1);
        assertThat(cardBooking.getDiscountCard().getId(), is(4l));
        assertThat(cardBooking.getPerson().getId(), is(1l));
    }

    @Test(expected = CardBookingException.class)
    public void bookWithNotExistsDiscountCardNumberTest() {
        service.book(6l, LocalDate.now());
    }

    @Test(expected = CardBookingException.class)
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void bookWithMatchesPersonTest() {
        service.book(1l, null);
    }

    @Test(expected = CardBookingException.class)
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void bookWithPickedDiscountCardTest() {
        service.book(3l, null);
    }

    @Test(expected = CardBookingException.class)
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void bookWithInvalidDateTest() {
        service.book(3l, LocalDate.of(2015, 9, 11));
    }

    @Test(expected = CardBookingException.class)
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void bookWithCardBookingAvailableOnTheDateTest() {
        service.book(3l, LocalDate.of(2015, 10, 26));
    }

    @Test
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void cancelTest() {
        long bookingCount = cardBookingRepository.count();
        service.cancel(2l);
        assertThat(cardBookingRepository.count(), is(bookingCount - 1));
    }

    @Test
    @WithMockCardiffPerson(value = "dmitriyvalnov")
    public void cancelWithAdminPermissionTest() {
        long bookingCount = cardBookingRepository.count();
        service.cancel(2l);
        assertThat(cardBookingRepository.count(), is(bookingCount - 1));
    }

    @Test(expected = CardBookingException.class)
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void cancelWithNotPermissionPerson() {
        service.cancel(3l);
    }

    @Test(expected = CardBookingException.class)
    @WithMockCardiffPerson(value = "dmitriyvalnov")
    public void cancelWithPickedDiscountCardTest() {
        service.cancel(1l);
    }

    @Test
    @WithMockCardiffPerson(value = "dmitriyvalnov")
    public void pickedTest() {
        service.picked(3l);
        CardBooking cardBooking = cardBookingRepository.findOne(3l);
        assertTrue(cardBooking.getDiscountCard().isPicked());
        assertThat(discountCardHistoryRepository.findAll().size(), is(1));
    }

    @Test(expected = CardBookingException.class)
    public void pickedWithPickedDiscountCardTest() {
        service.picked(1l);
    }

    @Test(expected = CardBookingException.class)
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void pickedWithPersonWithoutPermissionTest() {
        service.picked(3l);
    }

    @Test
    @WithMockCardiffPerson("dmitriyvalnov")
    public void returnedTest() {
        long cardBookingCount = cardBookingRepository.count();
        long discountCardId = cardBookingRepository.findOne(1l).getDiscountCard().getId();
        DiscountCardHistory discountCardHistory = new DiscountCardHistory();
        Person person = new Person();
        person.setId(1l);
        discountCardHistory.setPerson(person);
        DiscountCard discountCard = new DiscountCard();
        discountCard.setId(discountCardId);
        discountCardHistory.setCreatedDate(LocalDateTime.now().minusDays(1l));
        discountCardHistory.setDiscountCard(discountCard);
        discountCardHistoryRepository.save(discountCardHistory);
        service.returned(1l);
        assertTrue(cardBookingCount > cardBookingRepository.count());
        assertFalse(discountCardRepository.findById(discountCardId).get().isPicked());
    }

    @Test(expected = CardBookingException.class)
    public void returnedWithNotExistCardbookingIdTest() {
        service.returned(10l);
    }

    @Test(expected = CardBookingException.class)
    public void returnedWithDiscountCardIsNotPickedTest() {
        service.returned(2l);
    }

    @Test
    @WithMockCardiffPerson("dmitriyvalnov")
    public void getPersonBookedDiscountCardsTest() {
        assertThat(service.getPersonBookedDiscountCards(new PageRequest(0, 15, new Sort(Sort.Direction.DESC,
                "bookingStartDate"))).getTotalElements(), is(1l));
    }

    @Test
    @WithMockCardiffPerson("dmitriyvalnov")
    public void getPersonDiscountCardBookingsTest() {
        assertThat(service.getPersonDiscountCardBookings(new PageRequest(0, 15, new Sort(Sort.Direction.DESC,
                "bookingStartDate"))).getTotalElements(), is(2l));
    }

    @Test
    public void deleteBookCardById() {
        service.deleteBookCardById(1);
        assertThat(cardBookingRepository.count(), is(2l));
    }

    @Test
    public void getAllTest() {
        assertThat(service.getAll().size(), is((int) cardBookingRepository.count()));
    }

    @Test
    public void getAvailableBookingDateTest() {
        assertNotNull(service.getAvailableBookingDate(3));
    }

    @Test
    public void getAvailableBookingDateWithoutBookingsTest() {
        assertThat(service.getAvailableBookingDate(5), is(LocalDate.now()));
    }

    @Test
    public void getAvailableBookingDateWithBookingEndDateBeforeCurrentDateTest() {
        CardBooking cardBooking = new CardBooking();
        cardBooking.setBookingEndDate(LocalDate.now().minusDays(1));
        cardBooking.setBookingStartDate(cardBooking.getBookingEndDate().minusDays(6));
        DiscountCard discountCard = new DiscountCard();
        discountCard.setId(5);
        cardBooking.setDiscountCard(discountCard);
        Person person = new Person();
        person.setId(1);
        cardBooking.setPerson(person);
        cardBookingRepository.save(cardBooking);
        assertThat(service.getAvailableBookingDate(5), is(LocalDate.now()));
    }

    @Test
    public void getAvailableBookingDateWithSeveralBookingsTest() {
        List<CardBooking> cardBookings = cardBookingRepository.findByDiscountCardIdOrderByBookingStartDateAsc(3);
        LocalDate newBookingStartDate;
        if(Period.between(cardBookings.get(0).getBookingEndDate(), LocalDate.now()).getDays() <= 0) {
            newBookingStartDate = cardBookings.get(0).getBookingEndDate().plusDays(1l);
        } else {
            newBookingStartDate = LocalDate.now();
        }
        CardBooking newCardBooking = new CardBooking();
        newCardBooking.setPerson(cardBookings.get(0).getPerson());
        newCardBooking.setDiscountCard(cardBookings.get(0).getDiscountCard());
        newCardBooking.setBookingStartDate(newBookingStartDate);
        newCardBooking.setBookingEndDate(newBookingStartDate.plusDays(6));
        cardBookingRepository.save(newCardBooking);
        assertThat(service.getAvailableBookingDate(newCardBooking.getDiscountCard().getId()),
                is(newBookingStartDate.plusDays(7)));
    }

    @Test
    public void getAvailableBookingDateWithSeveralBookingsAndPeriodMoreThanSevenDaysTest() {
        List<CardBooking> cardBookings = cardBookingRepository.findByDiscountCardIdOrderByBookingStartDateAsc(3);
        LocalDate newBookingStartDate;
        LocalDate availableBookingStartDate;
        if(Period.between(cardBookings.get(0).getBookingEndDate(), LocalDate.now()).getDays() <= 0) {
            availableBookingStartDate = cardBookings.get(0).getBookingEndDate().plusDays(1l);
            newBookingStartDate = cardBookings.get(0).getBookingEndDate().plusDays(12l);
        } else {
            availableBookingStartDate = LocalDate.now();
            newBookingStartDate = LocalDate.now().plusDays(12l);
        }
        CardBooking newCardBooking = new CardBooking();
        newCardBooking.setPerson(cardBookings.get(0).getPerson());
        newCardBooking.setDiscountCard(cardBookings.get(0).getDiscountCard());
        newCardBooking.setBookingStartDate(newBookingStartDate);
        newCardBooking.setBookingEndDate(newBookingStartDate.plusDays(6));
        cardBookingRepository.save(newCardBooking);
        assertThat(service.getAvailableBookingDate(newCardBooking.getDiscountCard().getId()),
                is(availableBookingStartDate));
    }


}
