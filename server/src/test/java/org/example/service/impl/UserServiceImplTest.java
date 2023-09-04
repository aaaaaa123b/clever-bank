package org.example.service.impl;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.repository.impl.UserPostgreRepository;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void findById() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setFirstName("Diana");
        expectedUser.setLastName("Harlap");
        expectedUser.setPatronymic("Olegovna");
        expectedUser.setLogin("diana");

        when(userRepository.findById(userId)).thenReturn(expectedUser);

        User actualUser = userRepository.findById(userId);
        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
    }


    @Test
    void addUser() {

        String firstName = "Diana";
        String lastName = "Harlap";
        String patronymic = "Olegovna";
        String login = "diana";

        User newUser = new User();
        newUser.setId(10L);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setPatronymic(patronymic);
        newUser.setLogin(login);


        when(userRepository.addUser(firstName, lastName, patronymic, login)).thenReturn(newUser);

        userService= new UserServiceImpl(userRepository);
        User addedUser = userService.addUser(firstName, lastName, patronymic, login);

        assertNotNull(addedUser);
        assertEquals(newUser.getId(), addedUser.getId());
        assertEquals(firstName, addedUser.getFirstName());
        assertEquals(lastName, addedUser.getLastName());
        assertEquals(patronymic, addedUser.getPatronymic());
        assertEquals(login, addedUser.getLogin());
    }

}