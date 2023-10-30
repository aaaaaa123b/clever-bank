package org.example.repository.impl;

import org.example.exception.EntityNotFoundException;
import org.example.model.Account;
import org.example.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountPostgreRepositoryTest {

    @Mock
    private ConnectionManager connectionManager;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    @InjectMocks
    private AccountPostgreRepository repository;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
    }

    @Test
    void shouldSuccessFindById() throws SQLException {
        long accountId = 1L;
        BigDecimal balance = BigDecimal.valueOf(1000);
        String currency = "USD";
        String number = "123456789";

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(accountId);
        when(resultSet.getBigDecimal("balance")).thenReturn(balance);
        when(resultSet.getString("currency")).thenReturn(currency);
        when(resultSet.getString("number")).thenReturn(number);

        Account account = repository.findById(accountId);

        assertNotNull(account);
        assertEquals(accountId, account.getId());
        assertEquals(balance, account.getBalance());
        assertEquals(currency, account.getCurrency());
        assertEquals(number, account.getNumber());
    }

    @Test
    void shouldEntityNotFoundExceptionFindAccountById() throws SQLException {
        long accountId = 1L;

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            repository.findById(accountId);
        });

        verify(preparedStatement, times(1)).setLong(1, accountId);
        verify(preparedStatement, times(1)).executeQuery();
        verify(resultSet, times(1)).next();

        assertEquals("Счёт с id 1 не найден.", exception.getMessage());
    }

    @Test
    void shouldSQLExceptionWhenFindAccountById() throws SQLException {
        long accountId = 1L;

        when(connection.prepareStatement(any(String.class))).thenThrow(new SQLException("Database error"));


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            repository.findById(accountId);
        });

        assertEquals("Ошибка при обработке SQL-запроса", exception.getMessage());
        verify(preparedStatement, times(0)).setLong(1, accountId);
    }


    @Test
    void shouldSuccessfindByNumber() throws SQLException {
        String accountNumber = "123456789";
        BigDecimal balance = BigDecimal.valueOf(1000);
        String currency = "USD";
        long accountId = 1L;

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("number")).thenReturn(accountNumber);
        when(resultSet.getLong("id")).thenReturn(accountId);
        when(resultSet.getBigDecimal("balance")).thenReturn(balance);
        when(resultSet.getString("currency")).thenReturn(currency);

        Account account = repository.findByNumber(accountNumber);

        assertNotNull(account);
        assertEquals(accountNumber, account.getNumber());
        assertEquals(balance, account.getBalance());
        assertEquals(currency, account.getCurrency());
        assertEquals(accountId, account.getId());
    }

    @Test
    void shouldEntityNotFoundExceptionFindAccountByNumber() throws SQLException {
        String accountNumber = "9876543210";

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            repository.findByNumber(accountNumber);
        });

        verify(preparedStatement, times(1)).setString(1, accountNumber);
        verify(preparedStatement, times(1)).executeQuery();
        verify(resultSet, times(1)).next();

        assertEquals("Счёт с number 9876543210 не найден.", exception.getMessage());
    }

    @Test
    void shouldSQLExceptionFindAccountByNumber() throws SQLException {
        String accountNumber = "9876543210";

        when(connection.prepareStatement(any(String.class))).thenThrow(new SQLException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            repository.findByNumber(accountNumber);
        });

        assertEquals("Ошибка при обработке SQL-запроса", exception.getMessage());
        verify(preparedStatement, times(0)).setString(1, accountNumber);
    }


    @Test
    void shouldSuccessUpdateAccount() throws SQLException {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("150.00"));
        account.setCurrency("EUR");
        account.setNumber("9876543210");
        account.setUserId(1);
        account.setBankId(1);
        account.setCreatedDate(Date.valueOf("2023-10-29"));

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        Account updatedAccount = repository.update(connection, account);

        verify(preparedStatement, times(1)).setBigDecimal(1, account.getBalance());
        verify(preparedStatement, times(1)).setString(2, account.getCurrency());
        verify(preparedStatement, times(1)).setString(3, account.getNumber());
        verify(preparedStatement, times(1)).setInt(4, account.getUserId());
        verify(preparedStatement, times(1)).setInt(5, account.getBankId());
        verify(preparedStatement, times(1)).setDate(6, (Date) account.getCreatedDate());
        verify(preparedStatement, times(1)).setLong(7, account.getId());

        assertEquals(account, updatedAccount);
    }

    @Test
    void shouldNotFoundUpdateAccount() throws SQLException {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("150.00"));
        account.setCurrency("EUR");
        account.setNumber("9876543210");
        account.setUserId(1);
        account.setBankId(1);
        account.setCreatedDate(Date.valueOf("2023-10-29"));

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            repository.update(connection, account);
        });

        assertEquals("Пользователь с id 1 не найден.", exception.getMessage());
    }

    @Test
    void shouldSQLExceptionUpdateAccount() throws SQLException {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("150.00"));
        account.setCurrency("EUR");
        account.setNumber("9876543210");
        account.setUserId(1);
        account.setBankId(1);
        account.setCreatedDate(Date.valueOf("2023-10-29"));
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("SQL Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            repository.update(connection, account);
        });

        assertEquals("Ошибка при обработке SQL-запроса", exception.getMessage());
    }

    @Test
    void shouldSuccessCreate() throws SQLException {
        Account account = new Account();
        account.setBalance(new BigDecimal("150.00"));
        account.setCurrency("EUR");
        account.setNumber("9876543210");
        account.setUserId(1);
        account.setBankId(1);
        account.setCreatedDate(Date.valueOf("2023-10-29"));

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(1L);

        Account createAccount = repository.create(account);

        verify(preparedStatement, times(1)).setBigDecimal(1, account.getBalance());
        verify(preparedStatement, times(1)).setString(2, account.getCurrency());
        verify(preparedStatement, times(1)).setString(3, account.getNumber());
        verify(preparedStatement, times(1)).setLong(4, account.getUserId());
        verify(preparedStatement, times(1)).setLong(5, account.getBankId());
        verify(preparedStatement, times(1)).setDate(6, (Date) account.getCreatedDate());
        verify(preparedStatement, times(1)).executeQuery();
        verify(resultSet, times(1)).next();

        assertEquals(1L, createAccount.getId());
        assertEquals(account, createAccount);
    }

    @Test
    void shouldSQLExceptionCreateAccount() throws SQLException {
        Account account = new Account();
        account.setBalance(new BigDecimal("150.00"));
        account.setCurrency("EUR");
        account.setNumber("9876543210");
        account.setUserId(1);
        account.setBankId(1);
        account.setCreatedDate(Date.valueOf("2023-10-29"));

        when(connection.prepareStatement(any(String.class))).thenThrow(new SQLException("SQL Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            repository.create(account);
        });

        assertEquals("Ошибка при выполнении SQL-запроса", exception.getMessage());
        verifyNoMoreInteractions(preparedStatement);
    }

    @Test
    void shouldNoResultCreateAccount() throws SQLException {
        Account account = new Account();
        account.setBalance(new BigDecimal("150.00"));
        account.setCurrency("EUR");
        account.setNumber("9876543210");
        account.setUserId(1);
        account.setBankId(1);
        account.setCreatedDate(Date.valueOf("2023-10-29"));

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Account createdAccount = repository.create(account);

        verify(preparedStatement, times(1)).executeQuery();
        verify(resultSet, times(1)).next();

        assertNull(createdAccount);
    }

    @Test
    void shouldSuccessDeleteAccount() throws SQLException {
        long accountId = 1L;

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenReturn(true);

        repository.delete(accountId);

        verify(preparedStatement, times(1)).setLong(1, accountId);
        verify(preparedStatement, times(1)).execute();
    }

    @Test
    void testDeleteAccountSQLException() throws SQLException {
        long accountId = 1L;

        when(connection.prepareStatement(any(String.class))).thenThrow(new SQLException("Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            repository.delete(accountId);
        });

        assertEquals("Ошибка при выполнении SQL-запроса", exception.getMessage());
        verify(preparedStatement, times(0)).setLong(1, accountId);
    }

    @Test
    void shouldSuccessUpdateAccountWithId() throws SQLException {
        long accountId = 1L;
        Account updatedAccount = new Account();
        updatedAccount.setId(accountId);
        updatedAccount.setBalance(new BigDecimal("200.00"));
        updatedAccount.setCurrency("USD");
        updatedAccount.setNumber("1234567890");
        updatedAccount.setUserId(2);
        updatedAccount.setBankId(2);
        updatedAccount.setCreatedDate(Date.valueOf("2023-10-30"));

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        Account result = repository.update(accountId, updatedAccount);

        verify(preparedStatement, times(1)).setBigDecimal(1, updatedAccount.getBalance());
        verify(preparedStatement, times(1)).setString(2, updatedAccount.getCurrency());
        verify(preparedStatement, times(1)).setString(3, updatedAccount.getNumber());
        verify(preparedStatement, times(1)).setInt(4, updatedAccount.getUserId());
        verify(preparedStatement, times(1)).setInt(5, updatedAccount.getBankId());
        verify(preparedStatement, times(1)).setDate(6, (Date) updatedAccount.getCreatedDate());
        verify(preparedStatement, times(1)).setLong(7, accountId);
        verify(preparedStatement, times(1)).executeUpdate();

        assertEquals(updatedAccount, result);
    }


    @Test
    void shouldEntityNotFoundExceptionUpdateAccount() throws SQLException {
        long accountId = 1L;
        Account updatedAccount = new Account();
        updatedAccount.setId(accountId);
        updatedAccount.setBalance(new BigDecimal("200.00"));
        updatedAccount.setCurrency("USD");
        updatedAccount.setNumber("1234567890");
        updatedAccount.setUserId(2);
        updatedAccount.setBankId(2);
        updatedAccount.setCreatedDate(Date.valueOf("2023-10-30"));

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            repository.update(accountId, updatedAccount);
        });

        assertEquals("Пользователь с id 1 не найден.", exception.getMessage());
    }

    @Test
    void shouldSQLExceptionUpdateAccountWithId() throws SQLException {
        long accountId = 1L;
        Account updatedAccount = new Account();
        updatedAccount.setId(accountId);
        updatedAccount.setBalance(new BigDecimal("200.00"));
        updatedAccount.setCurrency("USD");
        updatedAccount.setNumber("1234567890");
        updatedAccount.setUserId(2);
        updatedAccount.setBankId(2);
        updatedAccount.setCreatedDate(Date.valueOf("2023-10-30"));

        when(connection.prepareStatement(any(String.class))).thenThrow(new SQLException("Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            repository.update(accountId, updatedAccount);
        });

        assertEquals("Ошибка при обработке SQL-запроса", exception.getMessage());
        verify(preparedStatement, times(0)).setBigDecimal(1, updatedAccount.getBalance());
    }
}


