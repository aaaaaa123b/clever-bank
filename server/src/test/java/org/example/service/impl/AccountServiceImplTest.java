package org.example.service.impl;

import org.example.exception.NotEnoughMoneyException;
import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.example.util.ConnectionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ConnectionManager connectionManager;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Connection connection;

    @InjectMocks
    private AccountServiceImpl accountService;


    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
    }

    @Test
    void shouldSuccessWithdrawCash() {

        Account account = new Account();
        account.setBalance(new BigDecimal("100.00"));
        BigDecimal cashToWithdraw = new BigDecimal("50.00");

        Account resultAccount = accountService.withdrawCash(account, cashToWithdraw);
        assertEquals(new BigDecimal("50.00"), resultAccount.getBalance());
    }

    @Test
    void shouldNotEnoughMoneyExceptionWithdrawCash() {

        Account account = new Account();
        account.setBalance(new BigDecimal("100.00"));
        BigDecimal cashToWithdraw = new BigDecimal("200.00");

        assertThrows(NotEnoughMoneyException.class, () -> accountService.withdrawCash(account, cashToWithdraw));
    }

    @Test
    void shouldSuccessAddCash() {
        Account account = new Account();
        account.setBalance(new BigDecimal("100.00"));
        BigDecimal cashToAdd = new BigDecimal("50.00");

        Account resultAccount = accountService.addCash(account, cashToAdd);
        assertEquals(new BigDecimal("150.00"), resultAccount.getBalance());
    }


    @Test
    void shouldSuccessTransfer() {
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
    void shouldSuccessFindById() {
        long accountId = 1L;
        Account expectedAccount = mock(Account.class);
        when(accountRepository.findById(accountId)).thenReturn(expectedAccount);

        Account foundAccount = accountService.findById(accountId);

        assertNotNull(foundAccount);
        assertSame(expectedAccount, foundAccount);

    }

    @Test
    void shouldSuccessFindByNumber() {
        String accountNumber = "12345";
        Account expectedAccount = mock(Account.class);
        when(accountRepository.findByNumber(accountNumber)).thenReturn(expectedAccount);

        Account foundAccount = accountService.findByNumber(accountNumber);

        assertNotNull(foundAccount);
        assertSame(expectedAccount, foundAccount);
    }

    @Test
    void shouldSuccessCreateAccount() {
        Account accountToCreate = mock(Account.class);

        when(accountRepository.create(accountToCreate)).thenReturn(accountToCreate);

        Account createdAccount = accountService.createAccount(accountToCreate);

        assertNotNull(createdAccount);
        assertSame(accountToCreate, createdAccount);
        verify(accountRepository, times(1)).create(accountToCreate);
    }

    @Test
    void shouldSuccessUpdateAccount() {
        long accountId = 1L;
        Account accountToUpdate = mock(Account.class);
        when(accountRepository.update(accountId, accountToUpdate)).thenReturn(accountToUpdate);

        Account updatedAccount = accountService.updateAccount(accountId, accountToUpdate);

        assertNotNull(updatedAccount);
        assertSame(accountToUpdate, updatedAccount);
        verify(accountRepository, times(1)).update(accountId, accountToUpdate);
    }

    @Test
    void shouldSuccessDeleteById() {
        long accountId = 1L;
        doNothing().when(accountRepository).delete(accountId);

        assertDoesNotThrow(() -> accountService.deleteById(accountId));
        verify(accountRepository, times(1)).delete(accountId);
    }


}