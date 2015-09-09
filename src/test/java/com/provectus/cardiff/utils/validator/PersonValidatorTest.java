package com.provectus.cardiff.utils.validator;

import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static com.provectus.cardiff.utils.validator.PersonValidator.validate;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by artemvlasov on 04/09/15.
 */
public class PersonValidatorTest {
    private Person person;
    @Before
    public void createPerson() {
        person = new Person();
        person.setDeleted(false);
        person.setName("Test Name");
        person.setDescription("Default description");
        person.setPassword("testPassword");
        person.setEmail("testemail@gmail.com");
        person.setLogin("testlogin");
        person.setPhoneNumber(568569636);
        person.setRole(PersonRole.ADMIN);
    }

    @Test
    public void validateTest() {
        assertTrue(validate(person, false));
    }

    @Test
    public void validateWithNullTest() {
        assertFalse(validate(null, false));
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidLoginTest() {
        person.setLogin("h");
        validate(person, false);
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidEmailTest() {
        person.setEmail("fdsafdsfs@dsadfsd");
        validate(person, false);
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidPasswordTest() {
        person.setPassword("gd12");
        validate(person, false);
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidPhoneNumberTest() {
        person.setPhoneNumber(32l);
        validate(person, false);
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidNameTest() {
        person.setName("fsdfds3434");
        validate(person, false);
    }

    @Test(expected = EntityValidationException.class)
    public void validateWithInvalidDescriptionTest() {
        Random r = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 600; i++) {
            builder.append((char)(r.nextInt(26) + 'a'));
        }
        person.setDescription(builder.toString());
        validate(person, false);
    }

}
