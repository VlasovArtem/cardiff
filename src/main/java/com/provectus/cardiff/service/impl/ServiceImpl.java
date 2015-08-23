package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.User;
import com.provectus.cardiff.persistence.repository.DiscountCardRepository;
import com.provectus.cardiff.persistence.repository.UserRepository;
import com.provectus.cardiff.service.ServiceCardiff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Service
public class ServiceImpl implements ServiceCardiff {
    @Qualifier("userRepository")
    @Autowired
    UserRepository userCrudRepository;
    @Autowired
    DiscountCardRepository discountCardRepository;

    @Override
    public User getUserById(long id) {
        return userCrudRepository.findById(id);
    }

    @Override
    public User getUserByLogin(String login) {
        return userCrudRepository.findByLogin(login);
    }

    @Override
    public User getUserByEmail(String email) {
        return userCrudRepository.findByEmail(email);
    }

    @Override
    public User loginUser(String email, String login, String password) {
        Optional<User> user = Optional.ofNullable(userCrudRepository.findByEmailOrLogin(email, login));
        if(user.isPresent() && BCrypt.checkpw(password, new String(user.get().getPassword()))) {
            return user.get();
        } else {
            throw new RuntimeException("There is an error in email, login or password");
        }
    }

    @Override
    public void deleteUserById(long id) {
        userCrudRepository.delete(id);
    }

    @Override
    public void addUser(User user) {
        userCrudRepository.save(user);
    }

    @Override
    public void userRegistration(User user) {
        checkUserBeforeRegistration(user);
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userCrudRepository.save(user);
    }

    /**
     * Check user data on availability of required data
     * @param user Data that received from registration form
     */
    private void checkUserBeforeRegistration(User user) {
        if(user == null) {
            throw new RuntimeException("User cannot be null");
        }
        List<String> requiredData = new ArrayList<>(4);
        if (user.getEmail() == null) {
            requiredData.add("email");
        }
        if (user.getLogin() == null) {
            requiredData.add("login");
        }
        if (user.getPassword() == null) {
            requiredData.add("password");
        }
        if (user.getPhoneNumber() == 0) {
            requiredData.add("phone number");
        }
        if (requiredData.size() != 0) {
            throw new RuntimeException("Next user data is required: " + requiredData.stream().collect(Collectors
                    .joining(", ")));
        }
        if (userCrudRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with this email is already registered");
        } else if (userCrudRepository.existsByLogin(user.getLogin())) {
            throw new RuntimeException("User with this login is already registered");
        }
    }
}
