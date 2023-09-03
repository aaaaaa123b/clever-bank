package org.example.service.impl;

import org.example.exception.NotEnoughMoneyException;
import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.example.repository.impl.BankPostgreRepository;
import org.example.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceImplTest {

        @Mock
        private  AccountRepository accountRepository;

        @Mock
        private  ConnectionManager connectionManager;

        @Mock
        private PreparedStatement preparedStatement;

        @Mock
        private Connection connection;

        private AccountServiceImpl accountService;


            @BeforeEach
            public void setUp() throws SQLException {
                MockitoAnnotations.openMocks(this);
                accountService = new AccountServiceImpl(accountRepository, connectionManager);

                when(connectionManager.getConnection()).thenReturn(connection);
                when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
            }

        @Test
        public void withdrawCash() throws SQLException {

            Account account = new Account();
            account.setBalance(new BigDecimal("100.00"));
            BigDecimal cashToWithdraw = new BigDecimal("50.00");

            Account resultAccount = accountService.withdrawCash(account, cashToWithdraw);
            assertEquals(new BigDecimal("50.00"), resultAccount.getBalance());
        }

        @Test
        public void withdrawCashInsufficientBalance() {

            Account account = new Account();
            account.setBalance(new BigDecimal("100.00"));
            BigDecimal cashToWithdraw = new BigDecimal("200.00");

            assertThrows(NotEnoughMoneyException.class, () -> accountService.withdrawCash(account, cashToWithdraw));
        }

    @Test
    void addCash() {
        Account account = new Account();
        account.setBalance(new BigDecimal("100.00"));
        BigDecimal cashToWithdraw = new BigDecimal("50.00");

        Account resultAccount = accountService.addCash(account, cashToWithdraw);
        assertEquals(new BigDecimal("150.00"), resultAccount.getBalance());
    }

    @Test
    void transfer() {
        Account sourceAccount = new Account();
        sourceAccount.setBalance(new BigDecimal("100.00"));
        Account targetAccount = new Account();
        targetAccount.setBalance(new BigDecimal("50.00"));
        BigDecimal transferAmount = new BigDecimal("30.00");

        Account resultAccount = accountService.transfer(sourceAccount, targetAccount, transferAmount);

        assertEquals(new BigDecimal("70.00"), sourceAccount.getBalance());

        assertEquals(new BigDecimal("80.00"), targetAccount.getBalance());

        assertEquals(sourceAccount, resultAccount);
    }


    @Test
    void findById() {
        long accountId = 1L;
        Account expectedAccount = new Account();
        when(accountRepository.findById(accountId)).thenReturn(expectedAccount);

        Account foundAccount = accountService.findById(accountId);

        assertNotNull(foundAccount);
        assertSame(expectedAccount, foundAccount);

    }

    @Test
    void findByNumber() {
        String accountNumber = "12345";
        Account expectedAccount = new Account();
        when(accountRepository.findByNumber(accountNumber)).thenReturn(expectedAccount);

        Account foundAccount = accountService.findByNumber(accountNumber);

        assertNotNull(foundAccount);
        assertSame(expectedAccount, foundAccount);
    }

    @Test
    void save() {
    }

}