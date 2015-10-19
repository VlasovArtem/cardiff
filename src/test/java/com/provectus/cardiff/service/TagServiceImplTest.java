package com.provectus.cardiff.service;

import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import com.provectus.cardiff.entities.Tag;
import com.provectus.cardiff.utils.exception.EntityValidationException;
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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by artemvlasov on 19/10/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DevelopmentDataSourceConfig.class, AppConfig.class, RootContextConfig.class})
@ActiveProfiles(profiles = "development")
@SqlGroup(value = {
        @Sql("/sql-data/drop-data.sql"),
        @Sql("/sql-data/tag-data.sql")
})
@DirtiesContext
@WebAppConfiguration
@Transactional
public class TagServiceImplTest {
    @Autowired
    private TagService tagService;

    @Test
    public void getTagTest() {
        assertNotNull(tagService.getTag(1l));
    }

    @Test
    public void findAllTest() {
        assertThat(tagService.findAll().size(), is(1));
    }

    @Test
    public void addTagTest() {
        Tag newTag = new Tag("new tag");
        tagService.addTag(newTag);
        assertThat(tagService.findAll().size(), is(2));
    }

    @Test(expected = EntityValidationException.class)
    public void addTagWithInvalidTagTest() {
        Tag invalidTag = new Tag("ta");
        tagService.addTag(invalidTag);
    }

    @Test
    public void updateTagTest() {
        tagService.updateTag(1l, "update");
        assertThat(tagService.getTag(1l).getTag(), is("update"));
    }

    @Test
    public void deleteTagTest() {
        tagService.deleteTag(1l);
        assertThat(tagService.findAll().size(), is(0));
    }
}
