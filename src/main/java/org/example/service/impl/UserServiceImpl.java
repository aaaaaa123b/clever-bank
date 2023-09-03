package org.example.service.impl;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;

import java.util.ArrayList;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the user's ID
     * @return the user object, or null if not found
     */
    @Override
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Creates a new user.
     *
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param patronymic the user's patronymic name
     * @param login the user's login
     * @return the user object.
     */
    @Override
    public User addUser(String firstName, String lastName, String patronymic, String login) {
        return userRepository.addUser(firstName, lastName, patronymic, login);
    }


    @Override
    public boolean exists(Long id) {
        return findById(id) != null;
    }
}
