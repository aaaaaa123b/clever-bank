package org.example.service.impl;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void findById() {
        Long userId = 1L;
        userService.findById(userId);
        verify(userRepository).findById(userId);
    }


    @Test
    void addUserWithParameters() {
        String firstName = "Diana";
        String lastName = "Harlap";
        String patronymic = "Olegovna";
        String login = "diana";

        userService.addUser(firstName, lastName, patronymic, login);
        verify(userRepository).addUser(firstName, lastName, patronymic, login);

    }


    @Test
    public void testAddUserWithUserObject() {
        User user = new User();
        user.setFirstName("Diana");
        user.setLastName("Harlap");
        user.setPatronymic("Olegovna");
        user.setLogin("diana");

        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setFirstName("Diana");
        expectedUser.setLastName("Harlap");
        expectedUser.setPatronymic("Olegovna");
        expectedUser.setLogin("diana");

        when(userRepository.addUser("Diana", "Harlap", "Olegovna", "diana")).thenReturn(expectedUser);

        User addedUser = userService.addUser(user);

        assertEquals(expectedUser, addedUser);
    }

    @Test
    public void testDeleteUserById() {
        Long userId = 3L;

        userService.deleteById(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    public void testUpdateUser() {
        Long userId = 4L;
        User updatedUser = new User();
        updatedUser.setId(userId);

        when(userRepository.updateUser(userId, updatedUser)).thenReturn(updatedUser);

        User updated = userService.updateUser(userId, updatedUser);

        verify(userRepository).updateUser(userId, updatedUser);

        assertEquals(userId, updated.getId());
    }

}