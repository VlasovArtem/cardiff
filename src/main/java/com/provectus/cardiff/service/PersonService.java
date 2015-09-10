package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by artemvlasov on 20/08/15.
 */
public interface PersonService {
    void login(String loginData, String password, boolean rememberMe);
    void authentication();
    void logout();
    Person authenticated();
    void delete(long id);
    void changePassword(String oldPassword, String newPassword);
    void registration(Person user);
    void update(Person src);
    Page<Person> getAll(Pageable pageable);
    void authorized(PersonRole role);
    void restore(long id);
    void changeRole(long id);
    void checkLogin(String login);
    void checkEmail(String email);
    void checkPhoneNumber(long phoneNumber);
    Person find (long discountCardId);
}
