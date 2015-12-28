package com.provectus.cardiff.service;

import com.provectus.cardiff.WithMockCardiffPerson;
import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.CardiffAppInitializer;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import com.provectus.cardiff.config.security.SecurityConfig;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import com.provectus.cardiff.persistence.repository.LocationRepository;
import com.provectus.cardiff.persistence.repository.PersonRepository;
import com.provectus.cardiff.utils.exception.DataUniqueException;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import com.provectus.cardiff.utils.exception.person.PersonAuthorizationException;
import com.provectus.cardiff.utils.exception.person.PersonRegistrationException;
import com.provectus.cardiff.utils.exception.person.PersonUpdateException;
import com.provectus.cardiff.utils.security.AuthenticatedPersonPrincipalUtil;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by artemvlasov on 07/09/15.
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
@SqlGroup(value = {
        @Sql("/sql-data/drop-data.sql"),
        @Sql("/sql-data/location-data.sql"),
        @Sql("/sql-data/person-data.sql"),
        @Sql("/sql-data/discount-card-data.sql"),
})
public class PersonServiceImplTest {
    @Autowired
    private PersonService service;
    @Qualifier("personRepository")
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Rule
    public EasyMockRule mocks = new EasyMockRule(this);

    @Test
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void authenticatedTest() {
        Person person = service.authenticated();
        assertNotNull(person);
        assertThat(person.getEmail(), is("vadimguliaev@gmail.com"));
    }

    @Test
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void deleteTest() {
        assertTrue(service.delete(2l, null));
        Person person = personRepository.findById(2l);
        assertTrue(person.isDeleted());
    }

    @Test
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void deleteWithLogoutTest() throws ServletException {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        request.logout();
        EasyMock.expectLastCall().andVoid();
        assertTrue(service.delete(1l, request));
        Person person = personRepository.findById(1l);
        assertTrue(person.isDeleted());
    }

    @Test
    public void deleteWithNotExistsIdTest() {
        assertFalse(service.delete(5l, null));
    }

    @Test
    @WithMockCardiffPerson(value = "dmitriyvalnov@gmail.com")
    public void changePasswordTest() {
        service.changePassword("testpassword2", "testpassword3");
        Person person = personRepository.findById(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get()
                .getId());
        assertTrue(BCrypt.checkpw("testpassword3", person.getPassword()));
    }

    @Test(expected = EntityValidationException.class)
    public void changePasswordWithInvalidNewPasswordTest() {
        service.changePassword(null, "te");
    }

    @Test(expected = PersonUpdateException.class)
    @WithMockCardiffPerson(value = "dmitriyvalnov@gmail.com")
    public void changePasswordWithNotMatchedOldPasswordTest() {
        service.changePassword("notcorrectpassword", "testpassword3");
    }

    @Test
    public void registrationTest() {
        Person person = createPerson("Registered user", "newlogin", 562354869L, "regMethdoTestSkype", "password",
                "newaemail@mail.com", "new user");
        service.registration(person);
        assertThat(personRepository.count(), is(4l));
    }

    @Test(expected = PersonRegistrationException.class)
    public void registrationNullPersonTest() {
        service.registration(null);
    }

    @Test(expected = PersonRegistrationException.class)
    public void registrationWithExistingEmailTest() {
        Person person = createPerson("Registered user", "newlogin", 562354869L, "regMethdoTestSkype", "password",
                "dmitriyvalnov@gmail.com", "new user");
        service.registration(person);
    }

    @Test(expected = PersonRegistrationException.class)
    public void registrationWithExistingLoginTest() {
        Person person = createPerson("Registered user", "alexandrmahnov", 562354869L, "regMethdoTestSkype", "password",
                "dmitriyvalnov@gmail.com", "new user");
        service.registration(person);
    }

    @Test(expected = PersonRegistrationException.class)
    public void registrationWithExistingSkypeTest() {
        Person person = createPerson("Registered user", "alexandrmahnov", 562354869L, "testskype1", "password",
                "dmitriyvalnov@gmail.com", "new user");
        service.registration(person);
    }

    @Test
    @WithMockCardiffPerson("vadimguliaev@gmail.com")
    public void updateTest() {
        Person person = personRepository.findById(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal
                ().get().getId());
        person.setLogin("new_login");
        service.update(person);
        Person updatedPerson = personRepository.findById(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal
                ().get().getId());
        assertThat(updatedPerson.getLogin(), is("new_login"));
    }

    @Test
    @WithMockCardiffPerson("dmitriyvalnov")
    public void updateWithAdminTest() {
        Person person = personRepository.findById(1l);
        person.setEmail("newemail@mail.com");
        service.update(person);
        person = personRepository.findById(1l);
        assertThat(person.getEmail(), is("newemail@mail.com"));
    }

    @Test(expected = PersonAuthorizationException.class)
    @WithMockCardiffPerson("vadimguliaev@gmail.com")
    public void updateUnauthorizedTest() {
        Person person = personRepository.findById(2l);
        person.setLogin("new login");
        service.update(person);
    }

    @Test(expected = EntityValidationException.class)
    @WithMockCardiffPerson("vadimguliaev@gmail.com")
    public void updateWithInvalidDataTest() {
        Person person = personRepository.findById(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get()
                .getId());
        person.setLogin("rf");
        service.update(person);
    }

    @Test
    public void getAllTest() {
        Page<Person> allPersons = service.getAll(new PageRequest(0, 15, new Sort(Sort.Direction.DESC, "createdDate")));
        assertThat(allPersons.getTotalElements(), is(3l));
        assertThat(allPersons.getTotalPages(), is(1));
    }

    @Test
    @WithMockCardiffPerson("vadimguliaev@gmail.com")
    public void authorizedTest() {
        assertTrue(service.authorized(PersonRole.USER));
    }

    @Test
    @WithMockCardiffPerson("vadimguliaev@gmail.com")
    public void authorizedWithNotExistsRoleTest() {
        assertFalse(service.authorized(PersonRole.ADMIN));
    }

    @Test
    public void restoreTest() {
        service.restore(3l);
        Person restoredPerson = personRepository.findById(3l);
        assertFalse(restoredPerson.isDeleted());
    }

    @Test
    public void changeRoleToUserRoleTest() {
        service.changeRole(2l);
        Person person = personRepository.findById(2l);
        assertThat(person.getRole(), is(PersonRole.USER));
    }

    @Test
    public void changeRoleToAdminRoleTest() {
        service.changeRole(1l);
        Person person = personRepository.findById(1l);
        assertThat(person.getRole(), is(PersonRole.ADMIN));
    }

    @Test
    public void checkLoginTest() {
        service.checkLogin("notavailablelogin");
    }

    @Test(expected = DataUniqueException.class)
    public void checkLoginThrowExceptionTest() {
        service.checkLogin("dmitriyvalnov");
    }

    @Test
    public void checkEmailTest() {
        service.checkEmail("notavailableemail@mail.com");
    }

    @Test(expected = DataUniqueException.class)
    public void checkEmailThrowExceptionTest() {
        service.checkEmail("vadimguliaev@gmail.com");
    }

    @Test
    public void checkPhoneNumberTest() {
        service.checkPhoneNumber(5645421645l);
    }

    @Test(expected = DataUniqueException.class)
    public void checkPhoneNumberThrowExceptionTest() {
        service.checkPhoneNumber(563256989l);
    }

    @Test
    public void findTest() {
        Person person = service.find(1l);
        assertNotNull(person);
        assertThat(person.getEmail(), is("vadimguliaev@gmail.com"));
    }

    @Test
    public void findNotExistsTest() {
        assertNull(service.find(6l));
    }

    private Person createPerson(String description, String login, long phoneNumber, String skype, String password, String email, String name) {
        Person person = new Person();
        person.setDescription(description);
        person.setLogin(login);
        person.setPhoneNumber(phoneNumber);
        person.setSkype(skype);
        person.setPassword(password);
        person.setEmail(email);
        person.setLocation(locationRepository.findByCityAndCountryIgnoreCase("Odessa", "Ukraine"));
        person.setName(name);
        return person;
    }
}
