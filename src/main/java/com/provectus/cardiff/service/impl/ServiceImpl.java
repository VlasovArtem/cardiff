package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.User;
import com.provectus.cardiff.persistence.repository.DiscountCardRepository;
import com.provectus.cardiff.persistence.repository.UserRepository;
import com.provectus.cardiff.service.ServiceCardiff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
        return userCrudRepository.findByEmailOrLoginAndPassword(email, login, password.getBytes());
    }

    @Override
    public void deleteUserById(long id) {
        userCrudRepository.delete(id);
    }

    @Override
    public void addUser(User user) {
        userCrudRepository.save(user);
    }
}
