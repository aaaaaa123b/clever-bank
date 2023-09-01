package org.example.repository;

import org.example.model.User;

public interface UserRepository {

    User findById(Long id);

    User save(User user);

    User addUser(String firstName, String lastName, String patronymic, String login);
}
