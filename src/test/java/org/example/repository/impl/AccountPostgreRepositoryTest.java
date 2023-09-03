package org.example.repository.impl;

import org.example.model.Account;
import org.example.util.ConnectionManager;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AccountPostgreRepositoryTest {

    @Mock
    private ConnectionManager connectionManager;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    private AccountPostgreRepository repository;

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        repository = new AccountPostgreRepository(connectionManager);

        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
    }


    @org.junit.jupiter.api.Test
    void findById() throws SQLException {
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


    @org.junit.jupiter.api.Test
    void findByNumber()  throws SQLException {
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
}

//    @org.junit.jupiter.api.Test
//    void findById() {
//    }
//
//    @org.junit.jupiter.api.Test
//    void findByNumber() {
//    }
//
//    @org.junit.jupiter.api.Test
//    void update() {
//    }
//
//    @org.junit.jupiter.api.Test
//    void create() {
//    }
