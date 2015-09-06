package com.provectus.cardiff.persistence.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.RootContextConfig;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by artemvlasov on 03/09/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, RootContextConfig.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("/META-INF/dbtest/person-data.xml")
public class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    @Test
    public void findByIdWithExistingIdTest() {
        Person person = personRepository.findById(1l);
        assertNotNull(person);
        assertThat(person, hasProperty("email", is("vadimguliaev@gmail.com")));
    }

    @Test
    public void findByIdWIthNotExistingIdTest() {
        Person person = personRepository.findById(4l);
        assertNull(person);
    }

    @Test
    public void findByLoginWithExistingLoginTest() {
        Person person = personRepository.findByLogin("alexandrmahnov");
        assertNotNull(person);
        assertThat(person, hasProperty("id", is(3l)));
    }

    @Test
    public void findByEmailWithExistingEmailTest() {
        Person person = personRepository.findByEmail("dmitriyvalnov@gmail.com");
        assertNotNull(person);
        assertThat(person, hasProperty("phoneNumber", is(632563263l)));
    }

    @Test
    public void existsByLoginWithExistingLoginTest() {
        boolean exists = personRepository.existsByLogin("vadimguliaev");
        assertTrue(exists);
    }

    @Test
    public void existsByEmailWithExistingEmailTest() {
        boolean exists = personRepository.existsByEmail("alexandrmahnov@gmail.com");
        assertTrue(exists);
    }

    @Test
    public void existsByIdAndRoleWithExistingIdAndRoleTest() {
        boolean exists = personRepository.existsByIdAndRole(2l, PersonRole.ADMIN);
        assertTrue(exists);
    }

    @Test
    public void existsByIdAndRoleWithExistingIdAndNotAvailableRoleTest() {
        boolean exists = personRepository.existsByIdAndRole(1l, PersonRole.ADMIN);
        assertFalse(exists);
    }

    @Test
    public void existsByLoginOrEmailAndDeletedWithExistingLoginAndDeletedFalseTest() {
        boolean exists = personRepository.existsByLoginOrEmailAndDeleted("vadimguliaev", null, false);
        assertTrue(exists);
    }

    @Test
    public void existsByLoginOrEmailAndDeletedWithExistingEmailAndDeletedFalseTest() {
        boolean exists = personRepository.existsByLoginOrEmailAndDeleted(null, "dmitriyvalnov@gmail.com", false);
        assertTrue(exists);
    }

    @Test
    public void existsByLoginOrEmailAndDeletedExistingEmailAndDeletedTrueTest() {
        boolean exists = personRepository.existsByLoginOrEmailAndDeleted(null, "alexandrmahnov@gmail.com", true);
        assertTrue(exists);
    }
    @Test
    public void existsByLoginOrEmailAndDeletedWithExistingLoginAndDeletedFalseShouldReturnFalseTest() {
        boolean exists = personRepository.existsByLoginOrEmailAndDeleted("alexandrmahnov", null, false);
        assertFalse(exists);
    }

    @Test
    public void existsByLoginOrEmailWithExistingLoginTest() {
        boolean exists = personRepository.existsByLoginOrEmail("alexandrmahnov", null);
        assertTrue(exists);
    }

    @Test
    public void existsByLoginOrEmailWithExistingEmailTest() {
        boolean exists = personRepository.existsByLoginOrEmail(null, "dmitriyvalnov@gmail.com");
        assertTrue(exists);
    }

    @Test
    public void existsByLoginOrEmailWithNotExistingEmailTest() {
        boolean exists = personRepository.existsByLoginOrEmail(null, "notavailableemail@gmail.com");
        assertFalse(exists);
    }

    @Test
    public void findByEmailOrLoginWithExistingEmailTest() {
        Person person = personRepository.findByEmailOrLogin("vadimguliaev@gmail.com", null);
        assertNotNull(person);
        assertThat(person, hasProperty("login", is("vadimguliaev")));
    }

    @Test
    public void findByEmailOrLogin_ExistingLogin_ShouldReturnPersonTest() {
        Person person = personRepository.findByEmailOrLogin(null, "dmitriyvalnov");
        assertNotNull(person);
        assertThat(person, hasProperty("email", is("dmitriyvalnov@gmail.com")));
    }

    @Test
    public void findByEmailOrLoginWithNotExistingLoginTest() {
        Person person = personRepository.findByEmailOrLogin(null, "testnotavailablelogin");
        assertNull(person);
    }

    @Test
    public void findByEmailOrLoginAndDeletedWithExistingLoginAndDeletedFalseTest() {
        Person person = personRepository.findByEmailOrLoginAndDeleted(null, "dmitriyvalnov", false);
        assertNotNull(person);
        assertThat(person, hasProperty("email", is("dmitriyvalnov@gmail.com")));
    }

    @Test
    public void findByEmailOrLoginAndDeletedWithExistingEmailAndDeletedTrueTest() {
        Person person = personRepository.findByEmailOrLoginAndDeleted("alexandrmahnov@gmail.com", null, true);
        assertNotNull(person);
        assertThat(person, hasProperty("login", is("alexandrmahnov")));
    }

    @Test
    public void findByEmailOrLoginAndDeletedWithExistingEmailAndDeletedTrueReturnNullTest() {
        Person person = personRepository.findByEmailOrLoginAndDeleted("alexandrmahnov@gmail.com", null, false);
        assertNull(person);
    }

    @Test
    public void findAllTest() {
        List<Person> persons = personRepository.findAll();
        assertThat(persons.size(), is(3));
    }

    @Test
    public void saveTest() {
        Person person = new Person();
        person.setPhoneNumber(563256963);
        person.setId(10l);
        person.setName("testname");
        person.setEmail("testemail@email.com");
        person.setLogin("testlogin");
        person.setPassword("testpassword");
        personRepository.save(person);
        long personCount = personRepository.count();
        assertThat(personCount, is(4l));
    }

    @Test
    public void deleteByIdTest() {
        personRepository.delete(1l);
        Person person = personRepository.findById(1l);
        assertNull(person);
    }

    @Test
    public void deleteAllTest() {
        personRepository.deleteAll();
        List<Person> persons = personRepository.findAll();
        assertThat(persons.size(), is(0));
    }

    @Test
    public void deleteEntityTest() {
        Person person = personRepository.findById(2l);
        personRepository.delete(person);
        long count = personRepository.count();
        assertThat(count, is(2l));
    }

    @Test
    public void countTest() {
        long count = personRepository.count();
        assertThat(count, is(3l));
    }
}
