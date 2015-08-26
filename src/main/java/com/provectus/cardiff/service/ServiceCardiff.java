package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.Person;

/**
 * Created by artemvlasov on 20/08/15.
 */
public interface ServiceCardiff {
    Person loginPerson(String loginData, String password, boolean rememberMe);
    void authentication();
    void logout();
    Person authenticatedPerson();
    void deletePersonById(long id);
    void changePassword(String oldPassword, String newPassword);
    void personRegistration(Person user);
}
