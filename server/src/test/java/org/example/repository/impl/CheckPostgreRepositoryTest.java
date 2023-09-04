package org.example.repository.impl;

import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.example.repository.CheckRepository;
import org.example.repository.TransactionRepository;
import org.example.util.ConnectionManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class CheckPostgreRepositoryTest {

        @Mock
        private ConnectionManager connectionManager;
        @Mock
        private Connection connection;
        @Mock
        private PreparedStatement preparedStatement;
        @Mock
        private ResultSet resultSet;

        private TransactionRepository transactionRepository;
        private AccountRepository accountRepository;
        private CheckRepository checkRepository;

        @BeforeEach
        public void setUp() throws SQLException {
            MockitoAnnotations.openMocks(this);
            accountRepository = new AccountPostgreRepository(connectionManager);
            transactionRepository = new TransactionPostgreRepository(connectionManager,accountRepository);
            checkRepository = new CheckPostgreRepository(connectionManager);

            when(connectionManager.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        }

        @Test
        void findTransactions() throws SQLException {
            LocalDate startDate = LocalDate.of(2023, 9, 1);
            LocalDate endDate = LocalDate.of(2023, 9, 4);
            Account account = new Account();
            account.setId(1L);

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false); // Один результат
            when(resultSet.getInt("id")).thenReturn(1);

            List<Long> transactionIds = checkRepository.findTransactions(startDate, endDate, account);

            assertNotNull(transactionIds);
            assertEquals(1, transactionIds.size());
            assertEquals(1, transactionIds.get(0));
        }

        @Test
        void testFindTransactions_NoTransactions() throws SQLException {
            LocalDate startDate = LocalDate.of(2023, 9, 1);
            LocalDate endDate = LocalDate.of(2023, 9, 4);
            Account account = new Account();
            account.setId(1L);

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            List<Long> transactionIds = checkRepository.findTransactions(startDate, endDate, account);

            assertNotNull(transactionIds);
            assertEquals(0, transactionIds.size());
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
    }

