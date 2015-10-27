package com.provectus.cardiff.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.CardiffAppInitializer;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import com.provectus.cardiff.config.security.SecurityConfig;
import com.provectus.cardiff.entities.CardBooking;
import com.provectus.cardiff.service.CardBookingService;
import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Collections;

import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by artemvlasov on 19/10/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        DevelopmentDataSourceConfig.class,
        CardiffAppInitializer.class,
        AppConfig.class,
        RootContextConfig.class, SecurityConfig.class})
@ActiveProfiles(profiles = "development")
@DirtiesContext
@WebAppConfiguration
public class CardBookingControllerTest {
    @Rule
    public EasyMockRule mocks = new EasyMockRule(this);
    @Autowired
    @TestSubject
    private CardBookingController cardBookingController;
    @Mock
    private CardBookingService cardBookingService;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Pageable pageable;
    private Page<CardBooking> cardBookings;

    @Before
    public void setUp() {
        CardBooking cardBooking = new CardBooking();
        cardBooking.setId(1);
        cardBooking.setBookingStartDate(LocalDate.now());
        cardBooking.setBookingEndDate(LocalDate.now().plusDays(7));
        pageable = new PageRequest(0, 15, new Sort(Sort.Direction.DESC, "bookingStartDate"));
        cardBookings = new PageImpl<>(Collections.singletonList(cardBooking));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy
                .CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    }

    @Test
    public void bookTest() throws Exception {
        cardBookingService.book(1, LocalDate.now());
        expectLastCall().andVoid();
        this.mockMvc.perform(post("/rest/card/booking/book")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("discountCardId", "1").param("bookingStartDate", LocalDate.now().toString()));
    }

    @Test
    public void getBookedTest() throws Exception {
        expect(cardBookingService.getPersonBookedDiscountCards(pageable)).andReturn(cardBookings);
        replay(cardBookingService);
        this.mockMvc.perform(get("/rest/card/booking/booked/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value((int) cardBookings.getTotalElements()));
    }

    @Test
    public void getPersonDiscountCardBookingsTest() throws Exception {
        expect(cardBookingService.getPersonDiscountCardBookings(pageable)).andReturn(cardBookings);
        replay(cardBookingService);
        this.mockMvc.perform(get("/rest/card/booking/bookings/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value((int) cardBookings.getTotalElements()));
    }

    @Test
    public void cancelTest() throws Exception {
        cardBookingService.cancel(1);
        expectLastCall().andVoid();
        this.mockMvc.perform(put("/rest/card/booking/cancel/{bookingId}", 1)).andExpect(status().isOk());
    }

    @Test
    public void pickedTest() throws Exception {
        cardBookingService.picked(1);
        expectLastCall().andVoid();
        this.mockMvc.perform(put("/rest/card/booking/picked/{bookingId}", 1)).andExpect(status().isOk());
    }

    @Test
    public void returnedTest() throws Exception {
        cardBookingService.returned(1);
        expectLastCall().andVoid();
        this.mockMvc.perform(put("/rest/card/booking/returned/{bookingId}", 1)).andExpect(status().isOk());
    }

    @Test
    public void getAvailableBookingStartDateTest() throws Exception {
        expect(cardBookingService.getAvailableBookingDate(1)).andReturn(LocalDate.now().plusDays(7));
        replay(cardBookingService);
        byte[] returnedValue = this.mockMvc.perform(get("/rest/card/booking/get/available/start").param
                ("discountCardId", "1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        JsonNode node = objectMapper.readTree(returnedValue);
        LocalDate newDate = LocalDate.of(node.get(0).asInt(), node.get(1).asInt(), node.get(2).asInt());
        assertTrue(LocalDate.now().plusDays(7).isEqual(newDate));
    }
}
