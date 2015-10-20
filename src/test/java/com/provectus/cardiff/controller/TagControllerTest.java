package com.provectus.cardiff.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.CardiffAppInitializer;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import com.provectus.cardiff.config.security.SecurityConfig;
import com.provectus.cardiff.entities.Tag;
import com.provectus.cardiff.service.TagService;
import com.provectus.cardiff.utils.exception.DataUniqueException;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.NoSuchElementException;

import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
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
public class TagControllerTest {
    @Rule
    public EasyMockRule mocks = new EasyMockRule(this);
    @Autowired
    @TestSubject
    private TagController controller;
    @Mock
    private TagService service;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getTagTest() throws Exception {
        Tag tag = new Tag("Test tag");
        expect(service.getTag(1)).andReturn(tag);
        replay(service);
        mockMvc.perform(get("/rest/tag/get/{tagId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tag").value(is(tag.getTag())));

    }

    @Test
    public void getTagWithoutExistsTagTest() throws Exception {
        expect(service.getTag(1)).andThrow(new NoSuchElementException("Tag is not found"));
        replay(service);
        mockMvc.perform(get("/rest/tag/get/{tagId}", 1))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value(is("Tag is not found")));

    }

    @Test
    public void getAllTest() throws Exception {
        Tag tag = new Tag("Test tag");
        expect(service.findAll()).andReturn(Collections.singletonList(tag));
        replay(service);
        mockMvc.perform(get("/rest/tag/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]['tag']").value(tag.getTag()));
    }
}
