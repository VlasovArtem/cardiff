package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by artemvlasov on 20/08/15.
 */
public interface PersonService {
    boolean loginPerson(String loginData, String password, boolean rememberMe);
    void authentication();
    void logout();
    Person authenticatedPerson();
    void deletePersonById(long id);
    void changePassword(String oldPassword, String newPassword);
    void personRegistration(Person user);
    void update(Person src);
    List<Person> personAdminPanel(Sort sort);
    void authorized(PersonRole role);
}
