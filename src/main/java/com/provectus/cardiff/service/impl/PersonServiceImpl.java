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
import com.provectus.cardiff.utils.EntitiesUpdater;
import com.provectus.cardiff.utils.exceptions.PersonLoginException;
import com.provectus.cardiff.utils.exceptions.PersonRegistrationException;
import com.provectus.cardiff.utils.validators.PersonValidator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.provectus.cardiff.utils.validators.PersonValidator.*;

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
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setPassword(password.toCharArray());
        token.setUsername(loginData);
        token.setRememberMe(rememberMe);
        try {
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e) {
            throw new PersonLoginException(e.getMessage());
        }
    }

    /**
     * Check that src with current sessionId is successfully passed authentication. Throw exception only if src is
     * not authenticated or remembered.
     */
    @Override
    public void authentication() {
        if(!SecurityUtils.getSubject().isRemembered() && !SecurityUtils.getSubject().isAuthenticated() || !personRepository.exists((long) SecurityUtils.getSubject().getPrincipal())) {
            throw new AuthenticationException("Person is no authenticated");
        }
    }

    @Override
    public void logout() {
        SecurityUtils.getSubject().logout();
    }

    /**
     * Return src that is already successfully passed authentication
     * @return User
     */
    @Override
    public Person authenticated() {
        if(SecurityUtils.getSubject().getPrincipal() == null && !personRepository.exists((long) SecurityUtils
                .getSubject()
                .getPrincipal())) {
            throw new AuthenticationException("Person is not authenticated");
        }
        return personRepository.findById((long) SecurityUtils.getSubject().getPrincipal());
    }

    @Override
    public void delete(long id) {
        if(!personRepository.existsByIdAndRole((long) SecurityUtils.getSubject().getPrincipal(), PersonRole.ADMIN)) {
            throw new AuthorizationException("Person has no permission");
        }
        Person person = personRepository.findById(id);
        person.setDeleted(true);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        if(oldPassword == null || newPassword == null) {
            throw new IllegalArgumentException("Old or new password cannot be null");
        } else if(oldPassword.length() == 0 || newPassword.length() == 0) {
            throw new IllegalArgumentException("Old or new password cannot be empty");
        } else if(!passwordIsValid(newPassword)) {
            throw new IllegalArgumentException(DataType.EMAIL.getError());
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
    public void registration(Person person) {
        if(person == null) {
            throw new PersonRegistrationException("User cannot be null");
        }
        List<PersonValidator.DataType> data = isValid(person);
        if(!data.isEmpty()) {
            throw new PersonRegistrationException(data.stream().map(DataType::getError).collect(Collectors.joining(", ")));
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
        if(src.getId() == (long) SecurityUtils.getSubject().getPrincipal()) {
            trg = personRepository.findById((long) SecurityUtils.getSubject().getPrincipal());
        } else if(SecurityUtils.getSubject().hasRole(PersonRole.ADMIN.name())) {
            trg = personRepository.findById(src.getId());
        } else {
            throw new AuthenticationException("Person has no permission");
        }
        EntitiesUpdater.update(Optional.ofNullable(src), Optional.ofNullable(trg));
    }

    @Override
    public Page<Person> getAll(Pageable pageable) {
        SecurityUtils.getSubject().checkRole("ADMIN");
        return personRepository.findAll(pageable);
    }

    @Override
    public void authorized(PersonRole role) {
        SecurityUtils.getSubject().checkRole(role.name());
    }

    @Override
    public void restore(long id) {
        Person person = personRepository.findById(id);
        if(person.isDeleted()) {
            person.setDeleted(false);
        }
    }

    @Override
    public void changeRole(long id) {
        Person person = personRepository.findById(id);
        person.setRole(PersonRole.USER == person.getRole() ? PersonRole.ADMIN : PersonRole.USER);
    }
}
