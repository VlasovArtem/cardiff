package com.provectus.cardiff.service;

import com.provectus.cardiff.WithMockCardiffPerson;
import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.CardiffAppInitializer;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import com.provectus.cardiff.config.security.SecurityConfig;
import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.persistence.repository.DiscountCardRepository;
import com.provectus.cardiff.utils.exception.DataUniqueException;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import org.easymock.EasyMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

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
@Transactional
@WebAppConfiguration
@DirtiesContext
@SqlGroup(value = {
        @Sql("/sql-data/drop-data.sql"),
        @Sql("/sql-data/location-data.sql"),
        @Sql("/sql-data/person-data.sql"),
        @Sql("/sql-data/discount-card-data.sql"),
        @Sql("/sql-data/tag-data.sql"),
        @Sql("/sql-data/discount-card-tag-data.sql")
})
public class DiscountCardServiceImplTest {

    @Autowired
    private DiscountCardRepository discountCardRepository;
    @Autowired
    private DiscountCardService discountCardService;
    @Rule
    public EasyMockRule mocks = new EasyMockRule(this);
    private DiscountCard discountCard;

    @Before
    public void setup() {
        discountCard = new DiscountCard();
        discountCard.setDescription("Tested discount card description");
        discountCard.setAmountOfDiscount(10);
        discountCard.setCardNumber(999999);
        discountCard.setCompanyName("Tested company");
    }

    @Test
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void addTest() {
        discountCardService.add(discountCard);
        assertThat(discountCardRepository.findAll().size(), is(6));
    }

    @Test(expected = EntityValidationException.class)
    public void addWithExistsCompanyNameTest() {
        discountCard.setCompanyName("Cheese");
        discountCard.setCardNumber(1);
        discountCardService.add(discountCard);
    }

    @Test(expected = EntityValidationException.class)
    public void addWithInvalidDiscountCardDataTest() {
        discountCard.setCardNumber(-1);
        discountCardService.add(discountCard);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void updateTest() {
        discountCardService.update(null);
    }

    @Test
    public void deleteTest() {
        discountCardService.delete(1l);
        assertTrue(discountCardRepository.findById(1l).get().isDeleted());
    }

    @Test
    public void getCardTest() {
        assertNotNull(discountCardService.getCard(1l));
    }

    @Test
    public void searchByCardNumberTest() {
        assertTrue(discountCardService.searchByCardNumber(1).isPresent());
    }

    @Test
    public void searchByTagsTest() {
        Optional<List<DiscountCard>> searchList = discountCardService.searchByTags(Collections.singleton("test"));
        assertTrue(searchList.isPresent());
        assertThat(searchList.get().size(), is(1));
    }

    @Test
    public void searchByCompanyNameTest() {
        List<DiscountCard> searchList = discountCardService.searchByCompanyName("Cheese");
        assertNotNull(searchList);
        assertThat(searchList.size(), is(1));
    }

    @Test
    public void findAllTest() {
        assertThat(discountCardService.findAll().size(), is(5));
    }

    @Test
    public void getAllTest() {
        Pageable pageable = new PageRequest(0, 3, new Sort(Sort.Direction.DESC, "createdDate"));
        assertThat(discountCardService.getAll(pageable).getTotalElements(), is(5l));
    }

    @Test
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void getAuthenticatedPersonDiscountCardsTest() {
        Pageable pageable = new PageRequest(0, 15, new Sort(Sort.Direction.DESC, "createdDate"));
        assertThat(discountCardService.getAuthenticatedPersonDiscountCards(pageable).getTotalElements(), is(3l));
    }

    @Test
    public void removeOwnerCardsTest() {
        discountCardService.removeOwnerCards(2l);
        assertTrue(discountCardRepository.findById(4l).get().isDeleted());
        assertFalse(discountCardRepository.findById(3l).get().isDeleted());
    }

    @Test
    public void restoreOwnerCardTest() {
        discountCardService.restoreOwnerCard(1l);
        assertFalse(discountCardRepository.findById(5l).get().isDeleted());
    }

    @Test
    public void checkDiscountCardIsUniqueTest() {
        assertTrue(discountCardService.checkDiscountCardIsUnique(450349, "unique"));
    }

    @Test(expected = DataUniqueException.class)
    public void checkDiscountCardIsUniqueWithExistDataTest() {
        discountCardService.checkDiscountCardIsUnique(1, "Cheese");
    }

    @Test
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void authPersonDiscountCardTest() {
        assertTrue(discountCardService.authPersonDiscountCard(1));
    }
}
