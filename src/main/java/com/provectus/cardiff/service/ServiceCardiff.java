package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.User;

/**
 * Created by artemvlasov on 20/08/15.
 */
public interface ServiceCardiff {
    User getUserById(long id);
    User getUserByLogin(String login);
    User getUserByEmail(String email);
    User loginUser(String email, String login, String password);
    void deleteUserById(long id);
    void addUser(User user);
    void userRegistration(User user);
}
