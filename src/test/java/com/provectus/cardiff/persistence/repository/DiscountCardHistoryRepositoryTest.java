package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import com.provectus.cardiff.entities.DiscountCardHistory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by artemvlasov on 19/10/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DevelopmentDataSourceConfig.class, AppConfig.class, RootContextConfig.class})
@ActiveProfiles(profiles = "development")
@SqlGroup(value = {
        @Sql("/sql-data/drop-data.sql"),
        @Sql("/sql-data/location-data.sql"),
        @Sql("/sql-data/person-data.sql"),
        @Sql("/sql-data/discount-card-data.sql"),
        @Sql("/sql-data/discount-card-history-data.sql")
})
public class DiscountCardHistoryRepositoryTest {

    @Autowired
    private DiscountCardHistoryRepository repository;

    @Test
    public void findByIdTest() {
        assertNotNull(repository.findById(1l));
    }

    @Test
    public void findByDiscountCardIdTest() {
        assertNotNull(repository.findByDiscountCardId(1l));
    }

    @Test
    public void findByPersonIdTest() {
        assertNotNull(repository.findByPersonId(1l));
    }

    @Test
    public void findByPersonIdAndDiscountCardIdAndReturnDateIsNullTest() {
        DiscountCardHistory dch = repository.findByPersonIdAndDiscountCardIdAndReturnDateIsNull(1, 1);
        assertNotNull(dch);
        assertThat(dch.getId(), is(2l));
    }
}
