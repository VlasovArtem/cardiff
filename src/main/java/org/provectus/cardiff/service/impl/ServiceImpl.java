package org.provectus.cardiff.service.impl;

import org.provectus.cardiff.entities.User;
import org.provectus.cardiff.persistence.repository.UserCrudRepository;
import org.provectus.cardiff.service.ServiceCardiff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Service
public class ServiceImpl implements ServiceCardiff {
    @Qualifier("userCrudRepository")
    @Autowired
    UserCrudRepository userCrudRepository;

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
    public List<User> getUsers() {
        return userCrudRepository.findAll();
    }

    @Override
    public void deleteUserById(long id) {
        userCrudRepository.delete(id);
    }

    @Override
    public void deleteUserByLogin(String login) {
        userCrudRepository.deleteByLogin(login);
    }

    @Override
    public void deleteUserByEmail(String email) {
        userCrudRepository.deleteByEmail(email);
    }

    @Override
    public void deleteUsers() {
        userCrudRepository.deleteAll();
    }

    @Override
    public void addUser(User user) {
        userCrudRepository.save(user);
    }
}
