package org.example.repository.impl;

import org.example.exception.EntityNotFoundException;
import org.example.model.Bank;
import org.example.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BankPostgreRepositoryTest {

        @Mock
        private ConnectionManager connectionManager;
        @Mock
        private Connection connection;
        @Mock
        private PreparedStatement preparedStatement;
        @Mock
        private ResultSet resultSet;

        @InjectMocks
        private BankPostgreRepository bankRepository;

        @BeforeEach
        public void setUp() throws SQLException {
            MockitoAnnotations.openMocks(this);

            when(connectionManager.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        }

        @Test
        void shouldSuccessFindById() throws SQLException {
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
        void shouldNotFoundFindByIdBank() throws SQLException {
            int bankId = 20;

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
                bankRepository.findById(bankId);
            });

            assertEquals("Банк с id 20 не найден.", exception.getMessage());
        }

        @Test
        void shouldSQLExceptionFindById() throws SQLException {
            int bankId = 1;

            when(preparedStatement.executeQuery()).thenThrow(new SQLException("SQL Error"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                bankRepository.findById(bankId);
            });

            assertEquals("Ошибка при обработке SQL-запроса", exception.getMessage());
            assertTrue(exception.getCause() instanceof SQLException);
        }

    @Test
    void shouldSuccessCreateBank() throws SQLException {
        Bank bank = new Bank();
        bank.setName("Bank");
        int generatedId = 1;

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn((long) generatedId);

        Bank result = bankRepository.create(bank);

        verify(preparedStatement, times(1)).setString(1, bank.getName());
        verify(preparedStatement, times(1)).executeQuery();
        verify(resultSet, times(1)).next();

        assertEquals(generatedId, result.getId());
        assertEquals(bank, result);
    }

    @Test
    void shouldSQLExceptionCreateBank() throws SQLException {
        Bank bank = new Bank();
        bank.setName("Bank");

        when(connection.prepareStatement(any(String.class))).thenThrow(new SQLException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bankRepository.create(bank);
        });

        assertEquals("Ошибка при выполнении SQL-запроса", exception.getMessage());
        verify(preparedStatement, times(0)).setString(1, bank.getName());
    }

    @Test
    void shouldNotFoundCreateBank() throws SQLException {
        Bank bank = new Bank();
        bank.setName("Bank");

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Bank result = bankRepository.create(bank);

        verify(preparedStatement, times(1)).setString(1, bank.getName());
        verify(preparedStatement, times(1)).executeQuery();
        verify(resultSet, times(1)).next();

        assertNull(result);
    }

    @Test
    void shouldSuccessDeleteBank() throws SQLException {
        long bankId = 1L;

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);

        bankRepository.deleteById(bankId);

        verify(preparedStatement, times(1)).setLong(1, bankId);
        verify(preparedStatement, times(1)).execute();
    }

    @Test
    void shouldSQLExceptionDeleteBank() throws SQLException {
        long bankId = 1L;

        when(connection.prepareStatement(any(String.class))).thenThrow(new SQLException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bankRepository.deleteById(bankId);
        });

        assertEquals("Ошибка при выполнении SQL-запроса", exception.getMessage());

        verify(preparedStatement, times(0)).setLong(1, bankId); // Мы не ожидаем вызов setLong
    }

    @Test
    void shouldSuccessUpdateBank() throws SQLException {
        int bankId = 1;
        Bank updatedBank = new Bank();
        updatedBank.setId(bankId);
        updatedBank.setName("Bank");

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        Bank result = bankRepository.update(bankId, updatedBank);

        verify(preparedStatement, times(1)).setString(1, updatedBank.getName());
        verify(preparedStatement, times(1)).setInt(2, bankId);
        verify(preparedStatement, times(1)).executeUpdate();

        assertEquals(updatedBank, result);
    }

    @Test
    void shouldEntityNotFoundExceptionUpdateBank() throws SQLException {
        int bankId = 1;
        Bank updatedBank = new Bank();
        updatedBank.setId(bankId);
        updatedBank.setName("Updated Bank Name");

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            bankRepository.update(bankId, updatedBank);
        });

        verify(preparedStatement, times(1)).setString(1, updatedBank.getName());
        verify(preparedStatement, times(1)).setInt(2, bankId);
        verify(preparedStatement, times(1)).executeUpdate();

        assertEquals("Пользователь с id 1 не найден.", exception.getMessage());
    }

    @Test
    void shouldSQLExceptionUpdateBank() throws SQLException {
        int bankId = 1;
        Bank updatedBank = new Bank();
        updatedBank.setId(bankId);
        updatedBank.setName("Updated Bank Name");

        when(connection.prepareStatement(any(String.class))).thenThrow(new SQLException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bankRepository.update(bankId, updatedBank);
        });

        assertEquals("Ошибка при обработке SQL-запроса", exception.getMessage());
        verify(preparedStatement, times(0)).setString(1, updatedBank.getName());
    }



}


