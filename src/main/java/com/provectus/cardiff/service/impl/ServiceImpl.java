package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.persistence.repository.BookCardRepository;
import com.provectus.cardiff.persistence.repository.DiscountCardCommentRepository;
import com.provectus.cardiff.persistence.repository.DiscountCardHistoryRepository;
import com.provectus.cardiff.persistence.repository.DiscountCardRepository;
import com.provectus.cardiff.persistence.repository.PersonRepository;
import com.provectus.cardiff.persistence.repository.TagRepository;
import com.provectus.cardiff.service.ServiceCardiff;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Service
@Transactional
public class ServiceImpl implements ServiceCardiff {
    @Qualifier("personRepository")
    @Autowired
    PersonRepository personRepository;
    @Autowired
    DiscountCardRepository discountCardRepository;
    @Autowired
    DiscountCardCommentRepository discountCardCommentRepository;
    @Autowired
    DiscountCardHistoryRepository discountCardHistoryRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    BookCardRepository bookCardRepository;


    @Override
    public Person loginPerson(String loginData, String password, boolean rememberMe) {
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setPassword(password.toCharArray());
        token.setUsername(loginData);
        token.setRememberMe(rememberMe);
        try {
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e) {
            throw new AuthenticationException("There is an error in email, login or password");
        }
        return personRepository.findByEmailOrLogin(loginData, loginData);
    }

    /**
     * Check that person with current sessionId is successfully passed authentication. Throw exception only if person is
     * not authenticated or remembered.
     */
    @Override
    public void authentication() {
        if(!SecurityUtils.getSubject().isRemembered() && !SecurityUtils.getSubject().isAuthenticated() || !personRepository.exists((long) SecurityUtils.getSubject().getPrincipal())) {
            throw new AuthenticationException("User is not authenticated");
        }
    }

    @Override
    public void logout() {
        SecurityUtils.getSubject().logout();
    }

    /**
     * Return person that is already successfully passed authentication
     * @return User
     */
    @Override
    public Person authenticatedPerson() {
        if(SecurityUtils.getSubject().getPrincipal() == null) {
            throw new AuthenticationException("User is not authenticated");
        } else if(!personRepository.exists((long) SecurityUtils
                .getSubject()
                .getPrincipal())) {
            throw new AuthenticationException("User is not authenticated");
        }
        return personRepository.findById((long) SecurityUtils.getSubject().getPrincipal());
    }

    @Override
    public void deletePersonById(long id) {
        personRepository.delete(id);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        if(oldPassword == null || newPassword == null) {
            throw new IllegalArgumentException("Old or new password cannot be null");
        } else if(oldPassword.length() == 0 || newPassword.length() == 0) {
            throw new IllegalArgumentException("Old or new password cannot be empty");
        }
        authentication();
        Person user = personRepository.findById((long) SecurityUtils.getSubject().getPrincipal());
        if(BCrypt.checkpw(oldPassword, user.getPassword())) {
            user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        } else {
            throw new IllegalArgumentException("Old password is not match");
        }
    }

    @Override
    public void personRegistration(Person person) {
        checkPersonBeforeRegistration(person);
        person.setPassword(BCrypt.hashpw(person.getPassword(), BCrypt.gensalt()));
        personRepository.save(person);
    }

    /**
     * Check person data on availability of required data
     * @param person Data that received from registration form
     */
    private void checkPersonBeforeRegistration(Person person) {
        if(person == null) {
            throw new RuntimeException("User cannot be null");
        }
        List<String> requiredData = new ArrayList<>(4);
        if (person.getEmail() == null) {
            requiredData.add("email");
        }
        if (person.getLogin() == null) {
            requiredData.add("login");
        }
        if (person.getPassword() == null) {
            requiredData.add("password");
        }
        if (person.getPhoneNumber() == 0) {
            requiredData.add("phone number");
        }
        if (requiredData.size() != 0) {
            throw new RuntimeException("Next person data is required: " + requiredData.stream().collect(Collectors
                    .joining(", ")));
        }
        if (personRepository.existsByEmail(person.getEmail())) {
            throw new RuntimeException("Person with this email is already registered");
        } else if (personRepository.existsByLogin(person.getLogin())) {
            throw new RuntimeException("Person with this login is already registered");
        }
    }
}
