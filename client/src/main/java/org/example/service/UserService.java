package org.example.service;

import org.example.model.User;

public interface UserService {

    User login(Long id);

    void addUser(String firstName, String lastName, String patronymic, String login);
}
