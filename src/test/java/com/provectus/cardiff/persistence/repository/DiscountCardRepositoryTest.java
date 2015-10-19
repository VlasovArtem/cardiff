package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import org.hamcrest.Matchers;
import org.junit.Assert;
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
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by artemvlasov on 16/10/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DevelopmentDataSourceConfig.class, AppConfig.class, RootContextConfig.class})
@ActiveProfiles(profiles = "development")
@SqlGroup(value = {
        @Sql("/sql-data/drop-data.sql"),
        @Sql("/sql-data/location-data.sql"),
        @Sql("/sql-data/person-data.sql"),
        @Sql("/sql-data/discount-card-data.sql"),
        @Sql("/sql-data/tag-data.sql"),
        @Sql("/sql-data/discount-card-tag-data.sql")
})
@DirtiesContext
@WebAppConfiguration
@Transactional
public class DiscountCardRepositoryTest {
    @Autowired
    private DiscountCardRepository discountCardRepository;

    @Test
    public void findByIdTest() {
        assertNotNull(discountCardRepository.findById(1l).get());
    }

    @Test
    public void findByIdAndPickedFalseTest() {
        assertNotNull(discountCardRepository.findByIdAndPickedFalse(1l).get());
    }

    @Test(expected = NoSuchElementException.class)
    public void findByIdAndPickedFalseWithPickedTrueTest() {
        discountCardRepository.findByIdAndPickedFalse(2l).get();
    }

    @Test
    public void findByCardNumberTest() {
        assertNotNull(discountCardRepository.findByCardNumber(1));
    }

    @Test(expected = NoSuchElementException.class)
    public void findByCardNumberWithNotExistsCardNumberTest() {
        discountCardRepository.findByCardNumber(9656).get();
    }

    @Test
    public void findByPickedFalseTest() {
        Pageable pageable = new PageRequest(0, 15, Sort.Direction.DESC, "createdDate");
        assertThat(discountCardRepository.findByPickedFalse(pageable).getTotalElements(), is(3l));
    }

    @Test
    public void findByCompanyNameWithFullMatchTest() {
        assertThat(discountCardRepository.findByCompanyName("Test2".toLowerCase()).size(), is(1));
    }

    @Test
    public void findByCompanyNameWithPartialMatchTest() {
        assertThat(discountCardRepository.findByCompanyName("Test".toLowerCase()).size(), is(4));
    }

    @Test
    public void findByOwnerIdTest() {
        Pageable pageable = new PageRequest(0, 15, Sort.Direction.DESC, "createdDate");
        assertThat(discountCardRepository.findByOwnerId(2l, pageable).getTotalElements(), is(2l));
    }

    @Test
    public void findByOwnerIdStreamTest() {
        assertThat(discountCardRepository.findByOwnerId(2l).count(), is(2l));
    }

    @Test
    public void findByTagsTest() {
        assertThat(discountCardRepository.findByTags(Collections.singleton("test")).get().size(), is(1));
    }

    @Test
    public void existsByNumberAndCompanyNameTest() {
        assertTrue(discountCardRepository.existsByNumberAndCompanyName(2, "Test"));
    }

    @Test
    public void existsByNumberAndCompanyNameWithNotMatchesDataTest() {
        assertFalse(discountCardRepository.existsByNumberAndCompanyName(2, "Test1"));
    }

    @Test
    public void isPickedTest() {
        assertTrue(discountCardRepository.isPicked(2));
    }

    @Test
    public void isPickedWithNotPickedDiscountCardTest() {
        assertFalse(discountCardRepository.isPicked(1));
    }

    @Test
    public void personDiscountCardTest() {
        assertTrue(discountCardRepository.personDiscountCard(2, 1));
    }

    @Test
    public void personDiscountCardWithNotMatchesTest() {
        assertFalse(discountCardRepository.personDiscountCard(1, 2));
    }
}
