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

    /**
     * Delete user by user id.
     *
     * @param id user ID
     */
    void deleteById(Long id);

    /**
     * Add new user.
     *
     * @param user object user
     * @return object user.
     */
    User addUser(User user);

    /**
     * Update user by user id
     *
     * @param id user ID
     * @param user object user
     * @return updatet object user.
     */
    User updateUser(Long id, User user);
}
