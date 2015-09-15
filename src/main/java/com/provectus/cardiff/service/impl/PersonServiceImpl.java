package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import com.provectus.cardiff.persistence.repository.PersonRepository;
import com.provectus.cardiff.service.LocationService;
import com.provectus.cardiff.service.PersonService;
import com.provectus.cardiff.utils.EntityUpdater;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import com.provectus.cardiff.utils.exception.PersonAuthenticationException;
import com.provectus.cardiff.utils.exception.PersonAuthorizationException;
import com.provectus.cardiff.utils.exception.PersonDataUniqueException;
import com.provectus.cardiff.utils.exception.PersonRegistrationException;
import com.provectus.cardiff.utils.security.AuthenticatedPersonPrincipalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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

    /**
     * Check current subject is authenticated, remembered and person that authenticated is exists in database
     * otherwise throw Authentication exception
     */
    @Override
    public void authentication() {}

    /**
     * Check if current subject is successfully login and person with authenticated credentials (id) is exists in
     * database
     * @return Person that successfully authenticated in application.
     */
    @Override
    public Person authenticated() {
        if(!AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().isPresent()) {
            throw new PersonAuthenticationException("Person is not authenticated");
        }
        return personRepository.findById(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get().getId());
    }

    @Override
    public boolean delete(long id, HttpServletRequest servletRequest) {
        Person person = personRepository.findById(id);
        if(person != null) {
            person.setDeleted(true);
            if(person.getId() == AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get().getId()) {
                try {
                    servletRequest.logout();
                } catch (ServletException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        if(!validate(newPassword, PersonValidationInfo.PASSWORD.getPattern())) {
            throw new EntityValidationException(PersonValidationInfo.PASSWORD.getError());
        }
        Person user = personRepository.findById(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get().getId());
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
        if (src.getId() == AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get().getId()) {
            trg = personRepository.findById(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get().getId());
        } else if (AuthenticatedPersonPrincipalUtil.containAuthorities(PersonRole.ADMIN)) {
            trg = personRepository.findById(src.getId());
        } else {
            throw new PersonAuthorizationException();
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
    public boolean authorized(PersonRole role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new
                SimpleGrantedAuthority(role.name()));
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
