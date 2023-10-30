package org.example.repository.impl;

import org.example.model.Account;
import org.example.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckPostgreRepositoryTest {

    @Mock
    private ConnectionManager connectionManager;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private CheckPostgreRepository checkRepository;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);

    }

    @Test
    void testFindTransactionsSuccess() throws SQLException {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        long accountId = 1L;

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getLong("id")).thenReturn(1L, 2L);

        Account account = new Account();
        account.setId(accountId);

        ArrayList<Long> result = checkRepository.findTransactions(startDate, endDate, account);

        verify(preparedStatement, times(1)).setLong(1, accountId);
        verify(preparedStatement, times(1)).setLong(2, accountId);
        verify(preparedStatement, times(1)).setObject(3, startDate);
        verify(preparedStatement, times(1)).setObject(4, endDate);
        verify(preparedStatement, times(1)).executeQuery();

        assertEquals(2, result.size());
        assertTrue(result.contains(1L));
        assertTrue(result.contains(2L));
    }


    @Test
    void testFindTransactions_SQLException() throws SQLException {
        LocalDate startDate = LocalDate.of(2023, 9, 1);
        LocalDate endDate = LocalDate.of(2023, 9, 4);
        Account account = new Account();
        account.setId(1L);

        when(preparedStatement.executeQuery()).thenThrow(new SQLException("SQL Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            checkRepository.findTransactions(startDate, endDate, account);
        });

        assertTrue(exception.getCause() instanceof SQLException);

    }

    @Test
    void testFindAllTransactionsSuccess() throws SQLException {
        long accountId = 1L;

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getLong("id")).thenReturn(1L, 2L);

        Account account = new Account();
        account.setId(accountId);

        ArrayList<Long> result = checkRepository.findAllTransactions(account);

        verify(preparedStatement, times(1)).setLong(1, accountId);
        verify(preparedStatement, times(1)).setLong(2, accountId);
        verify(preparedStatement, times(1)).executeQuery();

        assertEquals(2, result.size());
        assertTrue(result.contains(1L));
        assertTrue(result.contains(2L));
    }

    @Test
    void testFindAllTransactionsSQLException() throws SQLException {
        long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);

        when(preparedStatement.executeQuery()).thenThrow(new SQLException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            checkRepository.findAllTransactions(account);
        });

        assertTrue(exception.getCause() instanceof SQLException);
    }

}

