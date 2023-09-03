package org.example.repository.impl;

import org.example.exception.EntityNotFoundException;
import org.example.model.Bank;
import org.example.repository.BankRepository;
import org.example.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BankPostgreRepositoryTest {

        @Mock
        private ConnectionManager connectionManager;
        @Mock
        private Connection connection;
        @Mock
        private PreparedStatement preparedStatement;
        @Mock
        private ResultSet resultSet;

        private BankRepository bankRepository;

        @BeforeEach
        public void setUp() throws SQLException {
            MockitoAnnotations.openMocks(this);
            bankRepository = new BankPostgreRepository(connectionManager);

            when(connectionManager.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        }

        @Test
        void findById() throws SQLException {
            int bankId = 100;
            String bankName = "TestBank";

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("id")).thenReturn(bankId);
            when(resultSet.getString("name")).thenReturn(bankName);

            Bank bank = bankRepository.findById(bankId);

            assertNotNull(bank);
            assertEquals(bankId, bank.getId());
            assertEquals(bankName, bank.getName());
        }

        @Test
        void FindById_BankNotFound() throws SQLException {
            int bankId = 20;

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
                bankRepository.findById(bankId);
            });

            assertEquals("Банк с id 20 не найден.", exception.getMessage());
        }

        @Test
        void FindById_SQLException() throws SQLException {
            int bankId = 1;

            when(preparedStatement.executeQuery()).thenThrow(new SQLException("SQL Error"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                bankRepository.findById(bankId);
            });

            assertEquals("Ошибка при обработке SQL-запроса", exception.getMessage());
            assertTrue(exception.getCause() instanceof SQLException);
        }
    }


