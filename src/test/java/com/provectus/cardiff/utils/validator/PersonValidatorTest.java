package com.provectus.cardiff.utils.validator;

import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.provectus.cardiff.utils.validator.PersonValidator.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

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
        List<DataType> dataTypes = validate(person);
        assertThat(dataTypes.size(), is(0));
    }

    @Test(expected = RuntimeException.class)
    public void validateWithNullTest() {
        Person person = null;
        validate(person);
    }

    @Test(expected = RuntimeException.class)
    public void validateWithNullLoginTest() {
        person.setLogin(null);
        validate(person);
    }

    @Test
    public void validateWithInvalidLoginTest() {
        person.setLogin("h");
        List<DataType> dataTypes = validate(person);
        assertThat(dataTypes.size(), is(1));
        assertEquals(dataTypes.get(0), DataType.LOGIN);
    }

    @Test
    public void validateWithInvalidEmailTest() {
        person.setEmail("fdsafdsfs@dsadfsd");
        List<DataType> dataTypes = validate(person);
        assertThat(dataTypes.size(), is(1));
        assertEquals(dataTypes.get(0), DataType.EMAIL);
    }

    @Test
    public void validateWithInvalidPasswordTest() {
        person.setPassword("gd12");
        List<DataType> dataTypes = validate(person);
        assertThat(dataTypes.size(), is(1));
        assertEquals(dataTypes.get(0), DataType.PASSWORD);
    }

    @Test
    public void validateWithInvalidPhoneNumberTest() {
        person.setPhoneNumber(32l);
        List<DataType> dataTypes = validate(person);
        assertThat(dataTypes.size(), is(1));
        assertEquals(dataTypes.get(0), DataType.PHONE_NUMBER);
    }

    @Test
    public void validateWithInvalidNameTest() {
        person.setName("fsdfds3434");
        List<DataType> dataTypes = validate(person);
        assertThat(dataTypes.size(), is(1));
        assertEquals(dataTypes.get(0), DataType.NAME);
    }

    @Test
    public void validateWithInvalidDescriptionTest() {
        Random r = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 600; i++) {
            builder.append((char)(r.nextInt(26) + 'a'));
        }
        person.setDescription(builder.toString());
        List<DataType> dataTypes = validate(person);
        assertThat(dataTypes.size(), is(1));
        assertEquals(dataTypes.get(0), DataType.DESCRIPTION);
    }

    @Test
    public void validateNameWithValidNameTest() {
        assertTrue(validateName("Hellof"));
    }

    @Test
    public void validateNameWithInvalidNameTest() {
        assertFalse(validateName("sda45"));
    }

    @Test
    public void validateNameWithNullTest() {
        assertFalse(validateName(null));
    }

    @Test
    public void validateLoginWithValidLoginTest() {
        assertTrue(validateLogin("dsf345fd"));
    }

    @Test
    public void validateLoginWithInvalidLoginTest() {
        assertFalse(validateLogin("sdfsa"));
    }

    @Test
    public void validateLoginWithNullTest() {
        assertFalse(validateLogin(null));
    }

    @Test
    public void validatePasswordWithValidPasswordTest() {
        assertTrue(validatePassword("dfsadfksjdf343_9s-d"));
    }

    @Test
    public void validatePasswordWithInvalidPasswordTest() {
        assertFalse(validatePassword("dfsdfs"));
    }

    @Test
    public void validatePasswordWithNullTest() {
        assertFalse(validatePassword(null));
    }

    @Test
    public void validatePhoneNumberWithValidPhoneNumberTest() {
        assertTrue(validatePhoneNumber(String.valueOf(563569636l)));
    }

    @Test
    public void validatePhoneNumberWithInvalidPhoneNumberTest() {
        assertFalse(validatePhoneNumber(String.valueOf(5632652364l)));
    }

    @Test
    public void validatePhoneNumberWithNullTest() {
        assertFalse(validatePhoneNumber(null));
    }

    @Test
    public void validateDescriptionWithValidDescriptionTest() {
        assertTrue(validateDescription("dfsadfsdhf34307d87fsadf-0-s0dfasdnf211"));
    }

    @Test
    public void validateDescriptionWithNullTest() {
        assertFalse(validateDescription(null));
    }

    @Test
    public void validateWithValidDataTest() {
        List<Data> datas = new ArrayList<>();
        datas.add(DataFactory.loginData(person.getLogin()));
        datas.add(DataFactory.nameData(person.getName()));
        datas.add(DataFactory.descriptionData(person.getDescription()));
        datas.add(DataFactory.emailData(person.getEmail()));
        datas.add(DataFactory.passwordData(person.getPassword()));
        datas.add(DataFactory.phoneNumberData(person.getPhoneNumber()));
        assertThat(validate(datas).size(), is(0));
    }

}
