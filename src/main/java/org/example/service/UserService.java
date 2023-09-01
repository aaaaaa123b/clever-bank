package org.example.service;

import org.example.model.User;

public interface UserService {

    User findById(Long id);

    User save(User user);

    User addUser(String firstName, String lastName, String patronymic, String login);

    boolean exists(Long id);
}
