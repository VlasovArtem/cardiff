package com.provectus.cardiff.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.CardiffAppInitializer;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import com.provectus.cardiff.config.security.SecurityConfig;
import com.provectus.cardiff.entities.CustomTag;
import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.entities.Tag;
import com.provectus.cardiff.service.CustomTagService;
import com.provectus.cardiff.service.DiscountCardService;
import com.provectus.cardiff.utils.SearchEngine;
import com.provectus.cardiff.utils.exception.DataUniqueException;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.hamcrest.Matchers;
import org.junit.Assert;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by artemvlasov on 20/10/15.
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
public class DiscountCardControllerTest {
    @Rule
    public EasyMockRule mocks = new EasyMockRule(this);
    @Autowired
    @TestSubject
    private DiscountCardController controller;
    @Mock
    private DiscountCardService service;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private DiscountCard discountCard;
    private ObjectNode node;

    @Before
    public void setUp() {
        node = JsonNodeFactory.instance.objectNode();
        node.put("cardNumber", 999);
        node.put("description", "test description");
        node.put("companyName", "test company");
        node.put("amountOfDiscount", 5);
        node.put("deleted", false);
        node.put("picked", false);
        discountCard = new DiscountCard();
        discountCard.setCardNumber(999);
        discountCard.setCompanyName("test company");
        discountCard.setDescription("test description");
        discountCard.setAmountOfDiscount(5);
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void addTest() throws Exception {
        service.add(discountCard);
        expectLastCall().andVoid();
        mockMvc.perform(post("/rest/card/add").contentType(APPLICATION_JSON).content(node.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void addWithExistsDataTest() throws Exception {
        service.add(discountCard);
        expectLastCall().andThrow(new EntityValidationException());
        replay(service);
        mockMvc.perform(post("/rest/card/add").contentType(APPLICATION_JSON).content(node.toString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getByCardNumberTest() throws Exception {
        expect(service.searchByCardNumber(999)).andReturn(Optional.of(discountCard));
        replay(service);
        mockMvc.perform(get("/rest/card/get/by/number").param("number", "999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value((int) discountCard.getCardNumber()));
    }

    @Test
    public void getByCardNumberWithoutExistsCardTest() throws Exception {
        expect(service.searchByCardNumber(999)).andReturn(Optional.ofNullable(null));
        replay(service);
        mockMvc.perform(get("/rest/card/get/by/number").param("number", "999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateTest() {
        // TODO Add update DiscountCard implementation
    }

    @Test
    public void deleteTest() throws Exception {
        service.delete(1);
        expectLastCall().andVoid();
        mockMvc.perform(delete("/rest/card/delete").param("cardId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllTest() throws Exception {
        Pageable pageable = new PageRequest(0, 15, new Sort(Sort.Direction.DESC, "createdDate"));
        Page<DiscountCard> discountCards = new PageImpl<>(Collections.singletonList(discountCard));
        expect(service.getAll(pageable)).andReturn(discountCards);
        replay(service);
        mockMvc.perform(get("/rest/card/get/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(is(1)));
    }

    @Test
    public void findAvailableTest() throws Exception {
        expect(service.getCard(1)).andReturn(Optional.of(discountCard));
        replay(service);
        mockMvc.perform(get("/rest/card/get/{cardId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value(is("test company")));
    }

    @Test
    public void findAvailableWithoutExistsDiscountCardTest() throws Exception {
        expect(service.getCard(1)).andReturn(Optional.ofNullable(null));
        replay(service);
        mockMvc.perform(get("/rest/card/get/{cardId}", 1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getAuthenticatedPersonDiscountCardsTest() throws Exception {
        Pageable pageable = new PageRequest(0, 15, new Sort(Sort.Direction.DESC, "createdDate"));
        Page<DiscountCard> discountCards = new PageImpl<>(Collections.singletonList(discountCard));
        expect(service.getAuthenticatedPersonDiscountCards(pageable)).andReturn(discountCards);
        replay(service);
        mockMvc.perform(get("/rest/card/owner/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(is(1)));
    }

    @Test
    public void checkDiscountCardTest() throws Exception {
        expect(service.checkDiscountCardIsUnique(discountCard.getCardNumber(), discountCard.getCompanyName()))
                .andReturn(true);
        replay(service);
        mockMvc.perform(get("/rest/card/check")
                .param("cardNumber", String.valueOf(discountCard.getCardNumber()))
                .param("companyName", discountCard.getCompanyName()))
                .andExpect(status().isOk());
    }

    @Test
    public void checkDiscountCardWithExceptionTest() throws Exception {
        String errorText = "Discount card with entered number and company companyName is already exists";
        expect(service.checkDiscountCardIsUnique(discountCard.getCardNumber(), discountCard.getCompanyName()))
                .andThrow(new DataUniqueException(errorText));
        replay(service);
        mockMvc.perform(get("/rest/card/check")
                .param("cardNumber", String.valueOf(discountCard.getCardNumber()))
                .param("companyName", discountCard.getCompanyName()))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value(is(errorText)));
    }

    @Test
    public void authPersonDiscountCardTest() throws Exception {
        expect(service.authPersonDiscountCard(1)).andReturn(true);
        replay(service);
        mockMvc.perform(get("/rest/card/auth/{cardId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authPersonCard").value(is(true)));
    }
}
