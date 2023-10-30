package org.example.repository.impl;

import org.example.exception.EntityNotFoundException;
import org.example.model.Bank;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.util.ConnectionManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class UserPostgreRepositoryTest {

    @Mock
    private ConnectionManager connectionManager;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    private UserRepository userRepository;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        userRepository = new UserPostgreRepository(connectionManager);

        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
    }

    @Test
    void shouldSuccessFindById() throws SQLException {
        long userId = 1L;
        String firstName = "Diana";
        String lastName = "Harlap";
        String patronymic = "Olegovna";
        String login = "diana";

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(userId);
        when(resultSet.getString("first_name")).thenReturn(firstName);
        when(resultSet.getString("last_name")).thenReturn(lastName);
        when(resultSet.getString("patronymic")).thenReturn(patronymic);
        when(resultSet.getString("login")).thenReturn(login);

        User user = userRepository.findById(userId);

        assertNotNull(user);
        assertEquals(userId, user.getId());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(patronymic, user.getPatronymic());
        assertEquals(login, user.getLogin());
    }

    @Test
    void shouldEntityNotFoundExceptionFindByIdUser() throws SQLException {
        long userId = 25L;

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Симулируем отсутствие записи

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userRepository.findById(userId);
        });

        assertEquals("Пользователь с id 25 не найден.", exception.getMessage());
    }

    @Test
    void shouldSQLExceptionFindById() throws SQLException {
        long userId = 1L;

        when(preparedStatement.executeQuery()).thenThrow(new SQLException("SQL Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userRepository.findById(userId);
        });

        assertEquals("Ошибка при обработке SQL-запроса", exception.getMessage());
        assertTrue(exception.getCause() instanceof SQLException);
    }

    @Test
    void shouldSuccessAddUser() throws SQLException {
        String firstName = "Diana";
        String lastName = "Harlap";
        String patronymic = "Olegovna";
        String login = "diana";

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(3L);

        User user = userRepository.addUser(firstName, lastName, patronymic, login);

        assertNotNull(user);
        assertEquals(3L, user.getId());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(patronymic, user.getPatronymic());
        assertEquals(login, user.getLogin());
    }

    @Test
    void shouldSQLExceptionAddUser() throws SQLException {
        String firstName = "Diana";
        String lastName = "Harlap";
        String patronymic = "Olegovna";
        String login = "diana";

        when(preparedStatement.executeQuery()).thenThrow(new SQLException("SQL Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userRepository.addUser(firstName, lastName, patronymic, login);
        });

        assertEquals("Ошибка при выполнении SQL-запроса", exception.getMessage());
        assertTrue(exception.getCause() instanceof SQLException);
    }

    @Test
    void shouldSuccessTestDeleteById() throws SQLException {
        long userId = 1L;

        when(preparedStatement.execute()).thenReturn(true);
        userRepository.deleteById(userId);
        verify(connectionManager).getConnection();
        verify(connection).prepareStatement("DELETE FROM users WHERE id = ?");
        verify(preparedStatement).setLong(1, userId);
        verify(preparedStatement).execute();
    }

    @Test
    void shouldSuccessTestUpdateUser() throws SQLException {

        User user = new User();
        user.setId(1L);
        user.setFirstName("A");
        user.setLastName("B");
        user.setPatronymic("C");
        user.setLogin("abc");

        User newUser = user;
        newUser.setFirstName("ABD");

        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(user.getId());

        User result = userRepository.updateUser(user.getId(), newUser);

        verify(connectionManager).getConnection();
        verify(preparedStatement).setString(1, newUser.getFirstName());
        verify(preparedStatement).setString(2, newUser.getLastName());
        verify(preparedStatement).setString(3, newUser.getPatronymic());
        verify(preparedStatement).setString(4, newUser.getLogin());
        verify(preparedStatement).setLong(5, newUser.getId());
        verify(preparedStatement).executeUpdate();

        assertEquals(user, result);
    }

    @Test
    void shouldSQLExceptionUpdateBank() throws SQLException {
        User user = new User();
        user.setId(1L);
        user.setFirstName("A");
        user.setLastName("B");
        user.setPatronymic("C");
        user.setLogin("abc");

        when(connection.prepareStatement(any(String.class))).thenThrow(new SQLException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userRepository.updateUser(user.getId(), user);
        });

        assertEquals("Ошибка при обработке SQL-запроса", exception.getMessage());
        verify(preparedStatement, times(0)).setString(1, user.getLogin());
    }
}