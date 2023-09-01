package org.example.service.impl;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User addUser(String firstName, String lastName, String patronymic, String login) {
        return userRepository.addUser(firstName, lastName, patronymic, login);
    }

    @Override
    public boolean exists(Long id) {
        return findById(id) != null;
    }
}
