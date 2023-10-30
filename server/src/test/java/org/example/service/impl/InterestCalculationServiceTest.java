package org.example.service.impl;

import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.example.util.ConnectionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.*;
import java.util.concurrent.ScheduledExecutorService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class InterestCalculationServiceTest {

    @InjectMocks
    private InterestCalculationServiceImpl interestCalculationService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @Mock
    private ConnectionManager connectionManager;

    @Mock
    Connection connection;

    @Mock
    private ScheduledExecutorService executorService;

    @BeforeEach
    void setup() throws SQLException {
        MockitoAnnotations.openMocks(this);
       // when(interestCalculationService.configuration()).thenReturn(1);
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }



    @Test
    void shouldSuccessCalculateInterest() throws SQLException {

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        Account account = new Account();
        long accountId = 1;
        BigDecimal initialBalance = new BigDecimal("1000");
        account.setId(1L);
        account.setBalance(initialBalance);
        account.setCurrency("EUR");
        account.setNumber("9876543210");
        account.setUserId(1);
        account.setBankId(1);
        account.setCreatedDate(Date.valueOf("2023-10-01"));

        when(resultSet.getLong("id")).thenReturn(accountId);
        when(accountRepository.findById(accountId)).thenReturn(account);
        when(resultSet.getBigDecimal("balance")).thenReturn(initialBalance);
        interestCalculationService.calculateInterest();

        BigDecimal expectedBalance = BigDecimal.valueOf(1000);
        Assertions.assertEquals(expectedBalance, account.getBalance());
    }

}
