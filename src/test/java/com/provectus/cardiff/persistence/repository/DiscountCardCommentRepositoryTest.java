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

import static org.junit.Assert.assertNotNull;

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
        @Sql("/sql-data/discount-card-comment-data.sql")
})
public class DiscountCardCommentRepositoryTest {

    @Autowired
    private DiscountCardCommentRepository discountCardCommentRepository;

    @Test
    public void findByIdTest() {
        assertNotNull(discountCardCommentRepository.findById(1l));
    }

    @Test
    public void findByDiscountCardIdTest() {
        assertNotNull(discountCardCommentRepository.findByDiscountCardId(1l));
    }

    @Test
    public void findByUserIdTest() {
        assertNotNull(discountCardCommentRepository.findByUserId(1l));
    }
}
