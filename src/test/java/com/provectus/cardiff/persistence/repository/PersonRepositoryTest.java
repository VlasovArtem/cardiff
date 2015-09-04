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
    public void findById_ExistingIdGiven_ShouldReturnPerson() {
        Person person = personRepository.findById(1l);
        assertNotNull(person);
        assertThat(person, hasProperty("email", is("vadimguliaev@gmail.com")));
    }

    @Test
    public void findById_NotExistingIdGiven_ShouldReturnNull() {
        Person person = personRepository.findById(4l);
        assertNull(person);
    }

    @Test
    public void findByLogin_ExistingLoginGiven_ShouldReturnPerson() {
        Person person = personRepository.findByLogin("alexandrmahnov");
        assertNotNull(person);
        assertThat(person, hasProperty("id", is(3l)));
    }

    @Test
    public void findByEmail_ExistingEmailGiven_ShouldReturnPerson() {
        Person person = personRepository.findByEmail("dmitriyvalnov@gmail.com");
        assertNotNull(person);
        assertThat(person, hasProperty("phoneNumber", is(632563263l)));
    }

    @Test
    public void existsByLogin_ExistingLoginGiven_ShouldReturnTrue() {
        boolean exists = personRepository.existsByLogin("vadimguliaev");
        assertTrue(exists);
    }

    @Test
    public void existsByEmail_ExistingEmailGiven_ShouldReturnTrue() {
        boolean exists = personRepository.existsByEmail("alexandrmahnov@gmail.com");
        assertTrue(exists);
    }

    @Test
    public void existsByIdAndRole_ExistingIdAndRoleGiven_ShouldReturnTrue() {
        boolean exists = personRepository.existsByIdAndRole(2l, PersonRole.ADMIN);
        assertTrue(exists);
    }

    @Test
    public void existsByIdAndRole_ExistingIdAndNotAvailableRoleGiven_ShouldReturnFalse() {
        boolean exists = personRepository.existsByIdAndRole(1l, PersonRole.ADMIN);
        assertFalse(exists);
    }

    @Test
    public void existsByLoginOrEmailAndDeleted_ExistingLoginAndDeletedFalse_ShouldReturnTrue() {
        boolean exists = personRepository.existsByLoginOrEmailAndDeleted("vadimguliaev", null, false);
        assertTrue(exists);
    }

    @Test
    public void existsByLoginOrEmailAndDeleted_ExistingEmailAndDeletedFalse_ShouldReturnTrue() {
        boolean exists = personRepository.existsByLoginOrEmailAndDeleted(null, "dmitriyvalnov@gmail.com", false);
        assertTrue(exists);
    }

    @Test
    public void existsByLoginOrEmailAndDeleted_ExistingEmailAndDeletedTrue_ShouldReturnTrue() {
        boolean exists = personRepository.existsByLoginOrEmailAndDeleted(null, "alexandrmahnov@gmail.com", true);
        assertTrue(exists);
    }
    @Test
    public void existsByLoginOrEmailAndDeleted_ExistingLoginAndDeletedFalse_ShouldReturnFalse() {
        boolean exists = personRepository.existsByLoginOrEmailAndDeleted("alexandrmahnov", null, false);
        assertFalse(exists);
    }

    @Test
    public void existsByLoginOrEmail_ExistingLogin_ShouldReturnTrue() {
        boolean exists = personRepository.existsByLoginOrEmail("alexandrmahnov", null);
        assertTrue(exists);
    }

    @Test
    public void existsByLoginOrEmail_ExistingEmail_ShouldReturnTrue() {
        boolean exists = personRepository.existsByLoginOrEmail(null, "dmitriyvalnov@gmail.com");
        assertTrue(exists);
    }

    @Test
    public void existsByLoginOrEmail_NotExistingEmail_ShouldReturnFalse() {
        boolean exists = personRepository.existsByLoginOrEmail(null, "notavailableemail@gmail.com");
        assertFalse(exists);
    }

    @Test
    public void findByEmailOrLogin_ExistingEmail_ShouldReturnPerson() {
        Person person = personRepository.findByEmailOrLogin("vadimguliaev@gmail.com", null);
        assertNotNull(person);
        assertThat(person, hasProperty("login", is("vadimguliaev")));
    }

    @Test
    public void findByEmailOrLogin_ExistingLogin_ShouldReturnPerson() {
        Person person = personRepository.findByEmailOrLogin(null, "dmitriyvalnov");
        assertNotNull(person);
        assertThat(person, hasProperty("email", is("dmitriyvalnov@gmail.com")));
    }

    @Test
    public void findByEmailOrLogin_NotExistingLogin_ShouldReturnNull() {
        Person person = personRepository.findByEmailOrLogin(null, "testnotavailablelogin");
        assertNull(person);
    }

    @Test
    public void findByEmailOrLoginAndDeleted_ExistingLoginAndDeletedFalse_ShouldReturnPerson() {
        Person person = personRepository.findByEmailOrLoginAndDeleted(null, "dmitriyvalnov", false);
        assertNotNull(person);
        assertThat(person, hasProperty("email", is("dmitriyvalnov@gmail.com")));
    }

    @Test
    public void findByEmailOrLoginAndDeleted_ExistingEmailAndDeletedTrue_ShouldReturnPerson() {
        Person person = personRepository.findByEmailOrLoginAndDeleted("alexandrmahnov@gmail.com", null, true);
        assertNotNull(person);
        assertThat(person, hasProperty("login", is("alexandrmahnov")));
    }

    @Test
    public void findByEmailOrLoginAndDeleted_ExistingEmailAndDeletedTrue_ShouldReturnNull() {
        Person person = personRepository.findByEmailOrLoginAndDeleted("alexandrmahnov@gmail.com", null, false);
        assertNull(person);
    }

    @Test
    public void findAll_ShouldReturnList() {
        List<Person> persons = personRepository.findAll();
        assertThat(persons.size(), is(3));
    }

    @Test
    public void save_ShouldSavePerson() {
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
    public void deleteById_ShouldRemovePerson() {
        personRepository.delete(1l);
        Person person = personRepository.findById(1l);
        assertNull(person);
    }

    @Test
    public void deleteAll_ShouldRemoveAllPerson() {
        personRepository.deleteAll();
        List<Person> persons = personRepository.findAll();
        assertThat(persons.size(), is(0));
    }

    @Test
    public void deleteEntity_ShouldRemovePerson() {
        Person person = personRepository.findById(2l);
        personRepository.delete(person);
        long count = personRepository.count();
        assertThat(count, is(2l));
    }

    @Test
    public void count_ShouldReturnLong() {
        long count = personRepository.count();
        assertThat(count, is(3l));
    }
}
