package com.provectus.cardiff.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.provectus.cardiff.WithMockCardiffPerson;
import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.CardiffAppInitializer;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import com.provectus.cardiff.config.security.SecurityConfig;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.persistence.repository.LocationRepository;
import com.provectus.cardiff.persistence.repository.PersonRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by artemvlasov on 07/09/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        DevelopmentDataSourceConfig.class,
        CardiffAppInitializer.class,
        AppConfig.class,
        RootContextConfig.class, SecurityConfig.class})
//@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
//        DirtiesContextTestExecutionListener.class,
//        TransactionalTestExecutionListener.class,
//        DbUnitTestExecutionListener.class, ServletTestExecutionListener.class})
@ActiveProfiles(profiles = "development")
@DatabaseSetup("/META-INF/dbtest/person-service-data.xml")
@Transactional
@WebAppConfiguration
public class PersonServiceImplTest {
    @Autowired
    private PersonService service;
    @Qualifier("personRepository")
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Before
    public void dataset() {
        Person person1 = personRepository.findById(1l);
        Person person2 = personRepository.findById(2l);
        Person person3 = personRepository.findById(3l);
        person1.setPassword(BCrypt.hashpw("testpassword", BCrypt.gensalt()));
        person2.setPassword(BCrypt.hashpw("testpassword2", BCrypt.gensalt()));
        person3.setPassword(BCrypt.hashpw("testpassword3", BCrypt.gensalt()));
    }

//    @Test
//    @WithMockUser
//    public void changePasswordTest() {
//        CardiffUserDetails cardiffUserDetails = EasyMock.createNiceMock(CardiffUserDetails.class);
//        EasyMock.expect(cardiffUserDetails.getId()).andReturn(1l);
//        service.changePassword("testpassword2", "testpassword4");
//        assertTrue(BCrypt.checkpw("testpassword4", personRepository.findById(1l).getPassword()));
//    }

    @Test
    @WithMockCardiffPerson(value = "vadimguliaev")
    @Ignore
    public void test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        service.changePassword("dfsdf", "dfsadfs");
    }

//    @Test
//    public void registrationTest() {
//        Person person = new Person();
//        person.setDescription("Registered user");
//        person.setLogin("newlogin");
//        person.setPhoneNumber(562354869l);
//        person.setPassword("password");
//        person.setEmail("newaemail@mail.com");
//        person.setName("new user");
//        person.setLocation(locationRepository.findByCityAndCountry("Odessa", "Ukraine"));
//        service.registration(person);
//        assertThat(personRepository.count(), is(4l));
//    }
//
//    @Test(expected = PersonRegistrationException.class)
//    public void registrationNullPersonTest() {
//        service.registration(null);
//    }
//
//    @Test(expected = PersonRegistrationException.class)
//    public void registrationWithExistingEmailTest() {
//        Person person = new Person();
//        person.setDescription("Registered user");
//        person.setLogin("newlogin");
//        person.setPhoneNumber(562354869l);
//        person.setPassword("password");
//        person.setEmail("dmitriyvalnov@gmail.com");
//        person.setLocation(locationRepository.findByCityAndCountry("Odessa", "Ukraine"));
//        person.setName("new user");
//        service.registration(person);
//    }
//
//    @Test(expected = PersonRegistrationException.class)
//    public void registrationWithExistingLoginTest() {
//        Person person = new Person();
//        person.setDescription("Registered user");
//        person.setLogin("alexandrmahnov");
//        person.setPhoneNumber(562354869l);
//        person.setPassword("password");
//        person.setEmail("newaemail@mail.com");
//        person.setLocation(locationRepository.findByCityAndCountry("Odessa", "Ukraine"));
//        person.setName("new user");
//        service.registration(person);
//    }
}
