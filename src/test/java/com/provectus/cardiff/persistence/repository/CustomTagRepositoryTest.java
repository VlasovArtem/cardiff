package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import com.provectus.cardiff.entities.CustomTag;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;

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
        @Sql("/sql-data/custom-tag-data.sql")
})
@DirtiesContext
@WebAppConfiguration
@Transactional
public class CustomTagRepositoryTest {
    @Autowired
    private CustomTagRepository customTagRepository;

    @Test
    public void findByIdTest() {
        assertNotNull(customTagRepository.findById(1l));
    }

    @Test
    public void findByIdWithoutExistsIdTest() {
        assertNull(customTagRepository.findById(2l));
    }

    @Test
    public void deleteByIdTest() {
        customTagRepository.deleteById(1l);
        assertThat(customTagRepository.count(), is(0l));
    }

    @Test
    public void existsByTagTest() {
        assertTrue(customTagRepository.existsByTag("test"));
    }

    @Test
    public void existsByTagWithNotExistsTagTest() {
        assertFalse(customTagRepository.existsByTag("hello"));
    }

    @Test
    public void countByAuthorIdTest() {
        assertThat(customTagRepository.countByAuthorId(1l), is(1l));
    }

}
