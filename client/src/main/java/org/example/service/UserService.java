package org.example.service;

import org.example.model.User;

public interface UserService {

    /**
     * Login.
     *
     * @param id user id
     * @return user object.
     */
    User login(Long id);

    /**
     * Add user.
     *
     * @param firstName user firstname
     * @param lastName user lastname
     * @param patronymic user patronymic
     * @param login user login
     */
    void addUser(String firstName, String lastName, String patronymic, String login);
}
