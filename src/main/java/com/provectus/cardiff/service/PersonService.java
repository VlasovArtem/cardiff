package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.security.access.vote.AuthenticatedVoter.IS_AUTHENTICATED_FULLY;

/**
 * Created by artemvlasov on 20/08/15.
 */
public interface PersonService {

    void authentication();
    Person authenticated();
    void delete(long id, HttpServletRequest request);
    void changePassword(String oldPassword, String newPassword);
    void registration(Person user);
    void update(Person src);
    Page<Person> getAll(Pageable pageable);
    boolean authorized(PersonRole role);
    void restore(long id);
    void changeRole(long id);
    void checkLogin(String login);
    void checkEmail(String email);
    void checkPhoneNumber(long phoneNumber);

    @Secured(IS_AUTHENTICATED_FULLY)
    Person find (long discountCardId);
}
