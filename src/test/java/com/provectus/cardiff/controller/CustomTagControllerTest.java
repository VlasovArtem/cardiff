package com.provectus.cardiff.controller;

import com.fasterxml.jackson.databind.JsonNode;
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
import com.provectus.cardiff.service.CustomTagService;
import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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
        RootContextConfig.class,
        SecurityConfig.class})
@ActiveProfiles(profiles = "development")
@DirtiesContext
@WebAppConfiguration
public class CustomTagControllerTest {
    @Rule
    public EasyMockRule mocks = new EasyMockRule(this);
    @Autowired
    @TestSubject
    private CustomTagController controller;
    @Mock
    private CustomTagService service;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Page<CustomTag> cardBookings;
    private CustomTag tag;
    private ObjectNode node;

    @Before
    public void setUp() {
        node = JsonNodeFactory.instance.objectNode();
        node.put("tag", "test");
        node.put("description", "Description");
        tag = new CustomTag();
        tag.setTag("test");
        tag.setDescription("Description");
        tag.setId(1);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy
                .CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    }

    @Test
    public void addTagTest() throws Exception {
        service.addTag(tag);
        expectLastCall().andVoid();
        this.mockMvc.perform(post("/rest/tag/custom/add")
                .contentType(APPLICATION_JSON)
                .content(node.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllTest() throws Exception {
        Pageable pageable = new PageRequest(0, 15, new Sort(Sort.Direction.DESC, "createdDate"));
        Page<CustomTag> customTags = new PageImpl<>(Collections.singletonList(tag));
        expect(service.getAll(pageable)).andReturn(customTags);
        replay(service);
        this.mockMvc.perform(get("/rest/tag/custom/admin/get/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value((int) customTags.getTotalElements()));
    }

    @Test
    @Ignore
    public void acceptTagTest() throws Exception {
        service.acceptTag(1);
        expectLastCall().andVoid();
        this.mockMvc.perform(put("/rest/tag/custom/admin/accept/{tagId}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void acceptTagWithoutExistsCustomTag() throws Exception {
        service.acceptTag(1);
        expectLastCall().andThrow(new NullPointerException("Custom tag is not exists with the specified id"));
        this.mockMvc.perform(put("/rest/tag/custom/admin/accept/{tagId}", 1))
                .andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();
    }

    @Test
    @Ignore
    public void deleteTest() throws Exception {
        service.delete(1);
        expectLastCall().andVoid();
        this.mockMvc.perform(put("/rest/tag/custom/admin/delete/{tagId}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void countUnAcceptedTagsTest() throws Exception {
        long unacceptedTags = 5;
        expect(service.countUnacceptedTags()).andReturn(unacceptedTags);
        replay(service);
        byte[] returnedValue = this.mockMvc.perform(get("/rest/tag/custom/admin/count"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        assertThat(objectMapper.readTree(returnedValue).findValue("count").asLong(), is(unacceptedTags));
    }
}
