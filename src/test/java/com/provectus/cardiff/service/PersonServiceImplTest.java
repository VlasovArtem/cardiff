package com.provectus.cardiff.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.CardiffAppInitializer;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import com.provectus.cardiff.config.ShiroSecurityConfig;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.persistence.repository.LocationRepository;
import com.provectus.cardiff.persistence.repository.PersonRepository;
import com.provectus.cardiff.utils.exception.PersonRegistrationException;
import org.apache.shiro.subject.Subject;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import test.AbstractShiroTest;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by artemvlasov on 07/09/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DevelopmentDataSourceConfig.class, CardiffAppInitializer.class, AppConfig.class, RootContextConfig.class,
        ShiroSecurityConfig.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class, ServletTestExecutionListener.class})
@ActiveProfiles(profiles = "development")
@DatabaseSetup("/META-INF/dbtest/person-service-data.xml")
@Transactional
public class PersonServiceImplTest extends AbstractShiroTest {
    @TestSubject
    @Autowired
    private PersonService service;
    @Qualifier("personRepository")
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Mock
    private Subject subject = createNiceMock(Subject.class);

    @Before
    public void dataset() {
        Person person1 = personRepository.findById(1l);
        Person person2 = personRepository.findById(2l);
        Person person3 = personRepository.findById(3l);
        person1.setPassword(BCrypt.hashpw("testpassword", BCrypt.gensalt()));
        person2.setPassword(BCrypt.hashpw("testpassword2", BCrypt.gensalt()));
        person3.setPassword(BCrypt.hashpw("testpassword3", BCrypt.gensalt()));
    }

    @Test
    public void testSimple() {

        expect(subject.getPrincipal()).andReturn(1l);
        setSubject(subject);
        System.out.println(subject.getPrincipal());
        clearSubject();
    }

    @Test
    public void registrationTest() {
        Person person = new Person();
        person.setDescription("Registered user");
        person.setLogin("newlogin");
        person.setPhoneNumber(562354869l);
        person.setPassword("password");
        person.setEmail("newaemail@mail.com");
        person.setName("new user");
        person.setLocation(locationRepository.findByCityAndCountry("Odessa", "Ukraine"));
        service.registration(person);
        assertThat(personRepository.count(), is(4l));
    }

    @Test(expected = PersonRegistrationException.class)
    public void registrationNullPersonTest() {
        service.registration(null);
    }

    @Test(expected = PersonRegistrationException.class)
    public void registrationWithExistingEmailTest() {
        Person person = new Person();
        person.setDescription("Registered user");
        person.setLogin("newlogin");
        person.setPhoneNumber(562354869l);
        person.setPassword("password");
        person.setEmail("dmitriyvalnov@gmail.com");
        person.setLocation(locationRepository.findByCityAndCountry("Odessa", "Ukraine"));
        person.setName("new user");
        service.registration(person);
    }

    @Test(expected = PersonRegistrationException.class)
    public void registrationWithExistingLoginTest() {
        Person person = new Person();
        person.setDescription("Registered user");
        person.setLogin("alexandrmahnov");
        person.setPhoneNumber(562354869l);
        person.setPassword("password");
        person.setEmail("newaemail@mail.com");
        person.setLocation(locationRepository.findByCityAndCountry("Odessa", "Ukraine"));
        person.setName("new user");
        service.registration(person);
    }
}
