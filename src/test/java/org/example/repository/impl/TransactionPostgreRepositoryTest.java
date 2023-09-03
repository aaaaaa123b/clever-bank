package org.example.repository.impl;

import org.example.enumeration.TransactionType;
import org.example.exception.EntityNotFoundException;
import org.example.model.Account;
import org.example.model.Transaction;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.example.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TransactionPostgreRepositoryTest {

        @Mock
        private ConnectionManager connectionManager;
        @Mock
        private Connection connection;
        @Mock
        private PreparedStatement preparedStatement;
        @Mock
        private ResultSet resultSet;
        @Mock
        private AccountRepository accountRepository;
        @Mock
        private TransactionRepository transactionRepository;

        @BeforeEach
        public void setUp() throws SQLException {
            MockitoAnnotations.openMocks(this);
            accountRepository=new AccountPostgreRepository(connectionManager);
            transactionRepository = new TransactionPostgreRepository(connectionManager,accountRepository);


            when(connectionManager.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        }

        @Test
        void create() throws SQLException {
            Transaction transaction = new Transaction();
            transaction.setTime(Time.valueOf("12:00:00"));
            transaction.setType(TransactionType.DEPOSIT);
            transaction.setAmount(BigDecimal.valueOf(100.0));
            transaction.setDate(java.sql.Date.valueOf("2023-09-01"));

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getLong("id")).thenReturn(1L);

            Transaction createdTransaction = transactionRepository.create(transaction);

            assertNotNull(createdTransaction);
            assertEquals(1, createdTransaction.getId());
        }


        @Test
        void findById() throws SQLException {
            int transactionId = 1;
            Time transactionTime = Time.valueOf("12:00:00");
            TransactionType transactionType = TransactionType.DEPOSIT;
            BigDecimal transactionAmount = BigDecimal.valueOf(100.0);
            Date transactionDate = Date.valueOf("2023-09-01");
            long senderAccountId = 2L;
            long recipientAccountId = 3L;

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("id")).thenReturn(transactionId);
            when(resultSet.getTime("time")).thenReturn(transactionTime);
            when(resultSet.getString("type")).thenReturn(transactionType.name());
            when(resultSet.getObject("sender_account", Long.class)).thenReturn(senderAccountId);
            when(resultSet.getObject("recipient_account", Long.class)).thenReturn(recipientAccountId);
            when(resultSet.getBigDecimal("amount")).thenReturn(transactionAmount);
            when(resultSet.getDate("date")).thenReturn(transactionDate);

            Transaction transaction = transactionRepository.findById(transactionId);

            assertNotNull(transaction);
            assertEquals(transactionId, transaction.getId());
            assertEquals(transactionTime, transaction.getTime());
            assertEquals(transactionType, transaction.getType());
            assertNotNull(transaction.getSenderAccount());
            assertNotNull(transaction.getRecipientAccount());
            assertEquals(transactionAmount, transaction.getAmount());
            assertEquals(transactionDate, transaction.getDate());
        }

        @Test
        void findByIdTransactionNotFound() throws SQLException {
            int transactionId = 2;

            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
                transactionRepository.findById(transactionId);
            });

            assertEquals("Банк с id 2 не найден.", exception.getMessage());
        }

        @Test
        void findByIdSQLException() throws SQLException {
            int transactionId = 1;

            when(preparedStatement.executeQuery()).thenThrow(new SQLException("SQL Error"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                transactionRepository.findById(transactionId);
            });

            assertEquals("Ошибка при обработке SQL-запроса", exception.getMessage());
            assertTrue(exception.getCause() instanceof SQLException);
        }

}