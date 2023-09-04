package org.example.repository;

import org.example.model.User;

public interface UserRepository {

    /**
     * Finds a user by their ID in the database.
     *
     * @param id the user's ID
     * @return the user object.
     */
    User findById(Long id);


    /**
     * Creates a new user in the database.
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
     * Update user by user id in the database
     *
     * @param id user ID
     * @param user object user
     * @return updatet object user.
     */
    User updateUser(Long id, User user);
}
