package com.provectus.cardiff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.CardiffAppInitializer;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import com.provectus.cardiff.config.security.SecurityConfig;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import com.provectus.cardiff.service.PersonService;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import com.provectus.cardiff.utils.exception.PersonAuthenticationException;
import com.provectus.cardiff.utils.exception.PersonAuthorizationException;
import com.provectus.cardiff.utils.exception.DataUniqueException;
import com.provectus.cardiff.utils.exception.PersonRegistrationException;
import com.provectus.cardiff.utils.validator.PersonValidator;
import com.provectus.cardiff.utils.view.PersonView;
import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by artemvlasov on 20/09/15.
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
public class PersonControllerTest {

    @Rule
    public EasyMockRule mocks = new EasyMockRule(this);

    private ObjectMapper objectMapper;
    private ObjectNode personNode;
    private Person person;

    @Mock
    private PersonService personService;

    @Autowired
    @TestSubject
    private PersonController controller;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy
                .CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        personNode = JsonNodeFactory.instance.objectNode();
        personNode.put("name", "test name");
        personNode.put("login", "testlogin");
        personNode.put("email", "testemail@mail.com");
        personNode.put("phone_number", 562356369);
        personNode.put("password", "testpassword");
        personNode.put("description", "test person description");
        personNode.put("role", "ADMIN");
        personNode.put("deleted", false);
        person = new Person("test name", "testlogin", "testpassword");
        person.setDeleted(false);
        person.setDescription("test person description");
        person.setPhoneNumber(562356369);
        person.setEmail("testemail@mail.com");
        person.setRole(PersonRole.ADMIN);
    }

    @Test
    public void personAuthorizedTest() throws Exception {
        expect(personService.authorized(PersonRole.ADMIN)).andReturn(true);
        replay(personService);
        this.mockMvc.perform(get("/rest/person/authorized").param("hasRole", "ADMIN"))
                .andExpect(status().isOk());
    }

    @Test
    public void personAuthorizedUnexpectedRoleTest() throws Exception {
        expect(personService.authorized(PersonRole.ADMIN)).andReturn(false);
        replay(personService);
        this.mockMvc.perform(get("/rest/person/authorized").param("hasRole", "ADMIN"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void registrationTest() throws Exception {
        personService.registration(person);
        expectLastCall().andVoid();
        this.mockMvc.perform(post("/rest/person/registration").contentType(MediaType.APPLICATION_JSON).content(personNode
                .toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void registrationWillInvalidPersonDataTest() throws Exception {
        personService.registration(person);
        expectLastCall().andThrow(new PersonRegistrationException());
        replay(personService);
        this.mockMvc.perform(post("/rest/person/registration").contentType(MediaType.APPLICATION_JSON).content(personNode.toString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void authenticatedTest() throws Exception {
        personService.authenticated();
        expectLastCall().andReturn(person);
        replay(personService);
        this.mockMvc.perform(get("/rest/person/authenticated"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(person)));
    }

    @Test
    public void authenticatedThrowExceptionTest() throws Exception {
        personService.authenticated();
        expectLastCall().andThrow(new PersonAuthenticationException("Person is not authenticated"));
        replay(personService);
        this.mockMvc.perform(get("/rest/person/authenticated"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(JsonNodeFactory.instance.objectNode().put
                        ("error", "Person is not authenticated").toString()));
    }

    @Test
    public void changePasswordTest() throws Exception {
        personService.changePassword("oldPassword", "newPassword");
        expectLastCall().andVoid();
        this.mockMvc.perform(put("/rest/person/password/update").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("oldPassword", "oldPassword")
                .param("newPassword", "newPassword"))
                .andExpect(status().isOk());
    }

    @Test
    public void changePasswordThrownEntityValidationExceptionTest() throws Exception {
        personService.changePassword("oldPassword", "newPassword");
        expectLastCall().andThrow(new EntityValidationException(PersonValidator.PersonValidationInfo.PASSWORD.getError()));
        replay(personService);
        this.mockMvc.perform(put("/rest/person/password/update").contentType(MediaType
                .APPLICATION_FORM_URLENCODED)
                .param("oldPassword", "oldPassword")
                .param("newPassword", "newPassword"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(JsonNodeFactory.instance.objectNode().put("error",
                        PersonValidator
                                .PersonValidationInfo.PASSWORD.getError()).toString()));
    }

    @Test
    @Ignore
    public void changePasswordThrowIllegalArgumentException() throws Exception {
        personService.changePassword("oldPassword", "newPassword");
        expectLastCall().andThrow(new IllegalArgumentException("Old password is not match"));
        replay(personService);
        MvcResult result = this.mockMvc.perform(put("/rest/person/password/update").contentType(MediaType
                .APPLICATION_FORM_URLENCODED)
                .param("oldPassword", "oldPassword")
                .param("newPassword", "newPassword"))
                .andExpect(status().is4xxClientError())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().equals(JsonNodeFactory.instance.objectNode().put("error",
                "Old password is not match").toString()));
    }

    /* Problem with mock MockHttpServletRequest */
    @Test
    @Ignore
    public void deleteTest() throws Exception {
        expect(personService.delete(1, new MockHttpServletRequest())).andReturn(true);
        replay(personService);
        this.mockMvc.perform(delete("/rest/person/admin/delete/{id}", 1))
                .andExpect(status().isOk());
    }

    /* Problem with mock MockHttpServletRequest */
    @Test
    @Ignore
    public void deleteWithInvalidDataTest() throws Exception {
        expect(personService.delete(1, null)).andReturn(false);
        replay(personService);
        this.mockMvc.perform(delete("/rest/person/admin/delete/{id}", 1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateTest() throws Exception {
        personService.update(person);
        expectLastCall().andVoid();
        this.mockMvc.perform(put("/rest/person/update").contentType(MediaType.APPLICATION_JSON).content(personNode.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void updateThrowPersonAuthorizationExceptionTest() throws Exception {
        personService.update(person);
        expectLastCall().andThrow(new PersonAuthorizationException());
        replay(personService);
        this.mockMvc.perform(put("/rest/person/update").content(personNode.toString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getAllDefaultDataTest() throws Exception {
        Pageable pageable = new PageRequest(0, 15, new Sort(Sort.Direction.DESC, "createdDate"));
        Page<Person> page = new PageImpl<>(Collections.singletonList(person));
        expect(personService.getAll(pageable))
                .andReturn(page);
        replay(personService);
        this.mockMvc.perform(get("/rest/person/admin/get/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
    }

    @Test
    public void restoreTest() throws Exception {
        personService.restore(1);
        expectLastCall().andVoid();
        this.mockMvc.perform(put("/rest/person/admin/restore/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void changeRoleTest() throws Exception {
        personService.changeRole(1);
        expectLastCall().andVoid();
        this.mockMvc.perform(put("/rest/person/admin/update/role/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void checkEmailTest() throws Exception {
        personService.checkEmail("testmail@mail.com");
        expectLastCall().andVoid();
        this.mockMvc.perform(post("/rest/person/check/email").param("email", "testmail@mail.com"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkEmailThrowExceptionTest() throws Exception {
        personService.checkEmail("testmail@mail.com");
        expectLastCall().andThrow(new DataUniqueException("Person with this email is already exists"));
        replay(personService);
        this.mockMvc.perform(post("/rest/person/check/email").param("email", "testmail@mail.com"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void checkLoginTest() throws Exception {
        personService.checkLogin("testlogin");
        expectLastCall().andVoid();
        this.mockMvc.perform(post("/rest/person/check/login").param("login", "testlogin"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkLoginThrowExceptionTest() throws Exception {
        personService.checkLogin("testlogin");
        expectLastCall().andThrow(new DataUniqueException("Person with this login is already exists"));
        replay(personService);
        this.mockMvc.perform(post("/rest/person/check/login").param("login", "testlogin"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void checkPhoneNumberTest() throws Exception {
        personService.checkPhoneNumber(523265669);
        expectLastCall().andVoid();
        this.mockMvc.perform(post("/rest/person/check/phone").param("phone", "523265669"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkPhoneNumberThrowExceptionTest() throws Exception {
        personService.checkPhoneNumber(523265669);
        expectLastCall().andThrow(new DataUniqueException("Person with this phone number is already exists"));
        replay(personService);
        this.mockMvc.perform(post("/rest/person/check/phone").param("phone", "523265669"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void findTest() throws Exception {
        expect(personService.find(1)).andReturn(person);
        replay(personService);
        this.mockMvc.perform(get("/rest/person/get/{cardId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writerWithView(PersonView.BasicLevel.class).writeValueAsString
                        (person)));
    }

    @Test
    public void findWithNotExistsIdTest() throws Exception {
        expect(personService.find(1)).andReturn(null);
        replay(personService);
        this.mockMvc.perform(get("/rest/person/get/{cardId}", 1))
                .andExpect(status().is4xxClientError());
    }

    @AfterClass
    @Sql("/sql-data/drop-data.sql")
    public static void teardown() {}
}
