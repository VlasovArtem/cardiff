package com.provectus.cardiff.utils;

import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.entities.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by artemvlasov on 04/09/15.
 */
public class EntityUpdaterTest {
    private Person personTarget;
    private Person personSource;
    @Before
    public void createPerson() {
        personSource = new Person();
        personSource.setName("Person Source Name");
        personSource.setPhoneNumber(563236963l);
        personSource.setEmail("personsourceemail@mail.com");
        personSource.setLogin("personSourceLogin");
        personSource.setPassword("dsfdfasdf3224");
        personSource.setDescription("This is the source description");
        personTarget = new Person();
        personTarget.setName("Person target name");
        personTarget.setPhoneNumber(456236536l);
        personTarget.setPassword("dsfdfasdf3224");
        personTarget.setEmail("persontargetemail@mail.com");
        personTarget.setLogin("personTargetLogin");
        personTarget.setDescription("This is the target description");
    }

    @Test
    public void updateTest() {
        EntityUpdater.update(Optional.ofNullable(personSource), Optional.ofNullable(personTarget));
        assertTrue(personSource.equals(personTarget));
        assertThat(personSource.getEmail(), is(personTarget.getEmail()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateDifferentClassesTest() {
        EntityUpdater.update(Optional.ofNullable(personSource), Optional.ofNullable(new DiscountCard()));
    }
}
