package org.example.service;

import org.example.model.User;

public interface UserService {

    /**
     * Finds a user by their ID.
     *
     * @param id the user's ID
     * @return the user object, or null if not found
     */
    User findById(Long id);

    User save(User user);

    /**
     * Creates a new user.
     *
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param patronymic the user's patronymic name
     * @param login the user's login
     * @return the user object.
     */
    User addUser(String firstName, String lastName, String patronymic, String login);

    boolean exists(Long id);
}
