package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import com.provectus.cardiff.persistence.repository.PersonRepository;
import com.provectus.cardiff.service.LocationService;
import com.provectus.cardiff.service.PersonService;
import com.provectus.cardiff.utils.EntityUpdater;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import com.provectus.cardiff.utils.exception.PersonDataUniqueException;
import com.provectus.cardiff.utils.exception.PersonLoginException;
import com.provectus.cardiff.utils.exception.PersonRegistrationException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LocationService locationService;


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
            throw new AuthenticationException("Person is not authenticated");
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
        return personRepository.findById((long) SecurityUtils.getSubject().getPrincipal());
    }

    @Override
    public void delete(long id) {
        Person person = personRepository.findById(id);
        if(person != null) {
            person.setDeleted(true);
            SecurityUtils.getSubject().logout();
        }
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
        validate(Optional.ofNullable(person).orElseThrow(() -> new PersonRegistrationException("Person cannot be " +
                "null")), false);
        if (personRepository.existsByLoginOrEmailOrPhoneNumber(person.getLogin(), person.getEmail(), person.getPhoneNumber())) {
            throw new PersonRegistrationException("Person with this email, login or phone number is already " +
                    "registered");
        }
        if (!locationService.exists(person.getLocation().getCity(), person.getLocation().getCountry())) {
            throw new PersonRegistrationException("Person location is invalid");
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
        validate(src, true);
        EntityUpdater.update(Optional.ofNullable(src), Optional.ofNullable(trg));
    }

    @Override
    public Page<Person> getAll(Pageable pageable) {
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

    @Override
    public void checkLogin(String login) {
        if(personRepository.existsByLogin(login)) {
            throw new PersonDataUniqueException("Person with this login is already exists");
        }
    }

    @Override
    public void checkEmail(String email) {
        if(personRepository.existsByEmail(email)) {
            throw new PersonDataUniqueException("Person with this email is already exists");
        }
    }

    @Override
    public void checkPhoneNumber(long phoneNumber) {
        if(personRepository.existsByPhoneNumber(phoneNumber)) {
            throw new PersonDataUniqueException("Person with this phone number is already exists");
        }
    }

    @Override
    public Person find(long discountCardId) {
        return personRepository.findByDiscountCardId(discountCardId);
    }
}
