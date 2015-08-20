package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.User;

import java.util.List;

/**
 * Created by artemvlasov on 20/08/15.
 */
public interface ServiceCardiff {
    User getUserById(long id);
    User getUserByLogin(String login);
    User getUserByEmail(String email);
    List<User> getUsers();
    void deleteUserById(long id);
    void deleteUserByLogin(String login);
    void deleteUserByEmail(String email);
    void deleteUsers();
    void addUser(User user);
}
