package org.example.repository.impl;

import org.example.exception.EntityNotFoundException;
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
        void findById() throws SQLException {
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
        void findByIdUserNotFound() throws SQLException {
            long userId = 25L;

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false); // Симулируем отсутствие записи

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
                userRepository.findById(userId);
            });

            assertEquals("Пользователь с id 25 не найден.", exception.getMessage());
        }

        @Test
        void findByIdSQLException() throws SQLException {
            long userId = 1L;

            when(preparedStatement.executeQuery()).thenThrow(new SQLException("SQL Error"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                userRepository.findById(userId);
            });

            assertEquals("Ошибка при обработке SQL-запроса", exception.getMessage());
            assertTrue(exception.getCause() instanceof SQLException);
        }

        @Test
        void addUser() throws SQLException {
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
        void addUserSQLException() throws SQLException {
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
    void save() {
    }

}