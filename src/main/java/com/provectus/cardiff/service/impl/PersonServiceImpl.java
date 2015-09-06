package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import com.provectus.cardiff.persistence.repository.BookCardRepository;
import com.provectus.cardiff.persistence.repository.DiscountCardCommentRepository;
import com.provectus.cardiff.persistence.repository.DiscountCardHistoryRepository;
import com.provectus.cardiff.persistence.repository.DiscountCardRepository;
import com.provectus.cardiff.persistence.repository.PersonRepository;
import com.provectus.cardiff.persistence.repository.TagRepository;
import com.provectus.cardiff.service.PersonService;
import com.provectus.cardiff.utils.EntityUpdater;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import com.provectus.cardiff.utils.exception.PersonLoginException;
import com.provectus.cardiff.utils.exception.PersonRegistrationException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.provectus.cardiff.utils.validator.PersonValidator.PersonValidationInfo;
import static com.provectus.cardiff.utils.validator.PersonValidator.validate;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Service
@Transactional
public class PersonServiceImpl implements PersonService {
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
    public void login(String loginData, String password, boolean rememberMe) {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            if (!currentUser.isAuthenticated()) {
                UsernamePasswordToken token = new UsernamePasswordToken(loginData, password);
                token.setRememberMe(rememberMe);
                currentUser.login(token);
            }
        } catch (AuthenticationException ae) {
            throw new PersonLoginException(ae.getMessage());
        }
    }

    /**
     * Check current subject is authenticated, remembered and person that authenticated is exists in database
     * otherwise throw Authentication exception
     */
    @Override
    public void authentication() {
        if (!SecurityUtils.getSubject().isRemembered() && !SecurityUtils.getSubject().isAuthenticated() || !personRepository.exists((long) SecurityUtils.getSubject().getPrincipal())) {
            throw new AuthenticationException("Person is no authenticated");
        }
    }

    @Override
    public void logout() {
        SecurityUtils.getSubject().logout();
    }

    /**
     * Check if current subject is successfully login and person with authenticated credentials (id) is exists in
     * database
     * @return Person that successfully authenticated in application.
     */
    @Override
    public Person authenticated() {
        if (SecurityUtils.getSubject().getPrincipal() == null && !personRepository.exists((long) SecurityUtils
                .getSubject()
                .getPrincipal())) {
            throw new AuthenticationException("Person is not authenticated");
        }
        return personRepository.findById((long) SecurityUtils.getSubject().getPrincipal());
    }

    @Override
    public void delete(long id) {
        if (!personRepository.existsByIdAndRole((long) SecurityUtils.getSubject().getPrincipal(), PersonRole.ADMIN)) {
            throw new AuthorizationException("Person has no permission");
        }
        Person person = personRepository.findById(id);
        person.setDeleted(true);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        if(validate(newPassword, PersonValidationInfo.PASSWORD.getPattern())) {
            throw new EntityValidationException(PersonValidationInfo.PASSWORD.getError());
        }
        Person user = personRepository.findById((long) SecurityUtils.getSubject().getPrincipal());
        if (BCrypt.checkpw(oldPassword, user.getPassword())) {
            user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        } else {
            throw new IllegalArgumentException("Old password is not match");
        }
    }

    @Override
    public void registration(Person person) {
        if (validate(Optional.ofNullable(person))) {
            throw new PersonRegistrationException("Person cannot be null");
        }
        if (personRepository.existsByLoginOrEmail(person.getLogin(), person.getEmail())) {
            throw new PersonRegistrationException("Person with this email is already registered");
        }
        person.setPassword(BCrypt.hashpw(person.getPassword(), BCrypt.gensalt()));
        personRepository.save(person);
    }

    @Override
    public void update(Person src) {
        Person trg;
        if (src.getId() == (long) SecurityUtils.getSubject().getPrincipal()) {
            trg = personRepository.findById((long) SecurityUtils.getSubject().getPrincipal());
        } else if (SecurityUtils.getSubject().hasRole(PersonRole.ADMIN.name())) {
            trg = personRepository.findById(src.getId());
        } else {
            throw new AuthenticationException("Person has no permission");
        }
        validate(Optional.ofNullable(src));
        EntityUpdater.update(Optional.ofNullable(src), Optional.ofNullable(trg));
    }

    @Override
    public Page<Person> getAll(Pageable pageable) {
        SecurityUtils.getSubject().checkRole("ADMIN");
        return personRepository.findAll(pageable);
    }

    /**
     * Check that authenticated person has authorization permission by role.
     * @param role The role for which the {@code Person} should be allowed
     */
    @Override
    public void authorized(PersonRole role) {
        SecurityUtils.getSubject().checkRole(role.name());
    }

    /**
     * Restore person in database set deleted field to false
     * @param id {@code Person} id that need to be restored
     */
    @Override
    public void restore(long id) {
        Person person = personRepository.findById(id);
        if (person.isDeleted()) {
            person.setDeleted(false);
        }
    }

    @Override
    public void changeRole(long id) {
        Person person = personRepository.findById(id);
        person.setRole(PersonRole.USER == person.getRole() ? PersonRole.ADMIN : PersonRole.USER);
    }
}
