package com.provectus.cardiff.service;

import com.provectus.cardiff.WithMockCardiffPerson;
import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.CardiffAppInitializer;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import com.provectus.cardiff.config.security.SecurityConfig;
import com.provectus.cardiff.entities.CustomTag;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.persistence.repository.CustomTagRepository;
import com.provectus.cardiff.persistence.repository.TagRepository;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.hamcrest.Matchers;
import org.junit.Assert;
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

import java.util.Arrays;
import java.util.Collections;

import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by artemvlasov on 16/10/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        DevelopmentDataSourceConfig.class,
        CardiffAppInitializer.class,
        AppConfig.class,
        RootContextConfig.class, SecurityConfig.class})
@ActiveProfiles(profiles = "development")
@WebAppConfiguration
@DirtiesContext
@SqlGroup(value = {
        @Sql("/sql-data/drop-data.sql"),
        @Sql("/sql-data/location-data.sql"),
        @Sql("/sql-data/person-data.sql"),
        @Sql("/sql-data/discount-card-data.sql"),
        @Sql("/sql-data/tag-data.sql"),
        @Sql("/sql-data/custom-tag-data.sql")
})
@Transactional
public class CustomTagServiceImplTest {

    @Autowired
    private CustomTagService customTagService;
    @Autowired
    private CustomTagRepository customTagRepository;
    @Autowired
    private TagRepository tagRepository;

    @Test
    public void findAllTest() {
        assertThat(customTagService.findAll().size(), is(1));
    }

    @Test
    @WithMockCardiffPerson(value = "vadimguliaev")
    @Ignore //Issue with author add to custom tag during test
    public void addTagTest() {
        CustomTag customTag = new CustomTag("new tag");
        customTag.setId(2);
        customTagService.addTag(customTag);
        assertThat(customTagRepository.findAll().size(), is(2));
    }

    @Test(expected = IllegalArgumentException.class)
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void addTagWithExistsCustomTagTest() {
        CustomTag customTag = new CustomTag("test");
        customTagService.addTag(customTag);
    }

    @Test(expected = RuntimeException.class)
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void addTagWithExceedCustomTagLimitTest() {
        Person person = new Person();
        person.setId(1);
        for (int i = 0; i < 5; i++) {
            CustomTag customTag = new CustomTag("test" + i);
            customTag.setAuthor(person);
            customTagRepository.save(customTag);
        }
        CustomTag customTag = new CustomTag("new tag");
        customTagService.addTag(customTag);
    }

    @Test(expected = IllegalArgumentException.class)
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void addTagWithExistsTagTest() {
        CustomTag customTag = new CustomTag("exist tag");
        customTagService.addTag(customTag);
    }

    @Test
    public void countUnacceptedTagsTest() {
        assertThat(customTagService.countUnacceptedTags(), is(1l));
    }

    @Test
    public void getAllTest () {
        Pageable pageable = new PageRequest(0, 15, Sort.Direction.DESC, "createdDate");
        assertThat(customTagService.getAll(pageable).getTotalElements(), is(1l));
    }

    @Test
    public void acceptTagTest() {
        customTagService.acceptTag(1);
        assertThat(customTagRepository.count(), is(0l));
        assertThat(tagRepository.count(), is(3l));
    }

    @Test(expected = NullPointerException.class)
    public void acceptTagWithNotExistsIdTest() {
        customTagService.acceptTag(99);
    }

    @Test
    public void deleteTest() {
        customTagService.delete(1l);
        assertThat(customTagRepository.count(), is(0l));
    }
}
