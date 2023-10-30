package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.enumeration.TransactionType;
import org.example.model.Account;
import org.example.model.Bank;
import org.example.model.Transaction;
import org.example.model.User;
import org.example.repository.BankRepository;
import org.example.repository.TransactionRepository;
import org.example.service.AccountService;
import org.example.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CrudPutControllerTest {


    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private UserService userService;
    @Mock
    private AccountService accountService;
    @Mock
    private BankRepository bankRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter writer;

    @InjectMocks
    private CrudController crudController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSuccessPutUser() throws Exception {
        Long userId = 1L;
        String updateUserRequestJson = "{\"id\": 1, \"firstName\": \"A\", \"lastName\": \"B\", \"patronymic\": \"C\", \"login\": \"abc\"}";
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setFirstName("A");
        updatedUser.setLastName("B");
        updatedUser.setPatronymic("C");
        updatedUser.setLogin("abc");

        User newUser;
        newUser = updatedUser;
        newUser.setLastName("D");


        when(request.getRequestURI()).thenReturn("/api/v1/crud/users/1");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(updateUserRequestJson)));
        when(objectMapper.readValue(updateUserRequestJson, User.class)).thenReturn(updatedUser);

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        when(userService.updateUser(userId, updatedUser)).thenReturn(newUser);
        when(userService.addUser(updatedUser)).thenReturn(updatedUser);
        when(userService.findById(userId)).thenReturn(updatedUser);

        crudController.doPut(request, response);

        verify(request).getRequestURI();
        verify(request).getReader();
        verify(userService).updateUser(userId, updatedUser);
        verify(response).getWriter();

        String expectedResponseBody = "{\"id\":1,\"firstName\":\"A\",\"lastName\":\"D\",\"patronymic\":\"C\",\"login\":\"abc\"}";
        Assertions.assertEquals(expectedResponseBody, responseWriter.toString().replaceAll("\\s", ""));
    }

    @Test
    void shouldSuccessPutAccount() throws Exception {

        String updateAccountRequestJson = "{\"id\":1,\"userId\":1,\"bankId\":1,\"balance\":150.00,\"currency\":\"EUR\",\"number\":\"9876543210\",\"createdDate\":1698526800000}";
        int id = 1;
        Account account = new Account();
        account.setId(id);
        account.setBalance(new BigDecimal("150.00"));
        account.setCurrency("EUR");
        account.setNumber("9876543210");
        account.setUserId(1);
        account.setBankId(1);
        account.setCreatedDate(Date.valueOf("2023-10-29"));

        Account updatedAccount;
        updatedAccount = account;
        updatedAccount.setBankId(2);


        when(request.getRequestURI()).thenReturn("/api/v1/crud/accounts/1");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(updateAccountRequestJson)));
        when(objectMapper.readValue(updateAccountRequestJson, Account.class)).thenReturn(updatedAccount);

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        when(accountService.updateAccount((long) id, updatedAccount)).thenReturn(updatedAccount);
        when(accountService.createAccount(updatedAccount)).thenReturn(updatedAccount);
        when(accountService.findById(id)).thenReturn(updatedAccount);

        crudController.doPut(request, response);

        verify(request).getRequestURI();
        verify(request).getReader();
        verify(accountService).updateAccount((long) id, updatedAccount);
        verify(response).getWriter();

        String expectedResponseBody = "{\"id\":1,\"userId\":1,\"bankId\":2,\"balance\":150.00,\"currency\":\"EUR\",\"number\":\"9876543210\",\"createdDate\":1698526800000}";
        Assertions.assertEquals(expectedResponseBody, responseWriter.toString().replaceAll("\\s", ""));
    }

    @Test
    void shouldSuccessPutTransaction() throws Exception {

        String updateTransactionRequestJson = "{\"id\":1,\"senderAccount\":null,\"recipientAccount\":null,\"amount\":100.00,\"time\":\"12:30:00\",\"date\":1698526800000,\"type\":\"TRANSFER\"}";
        int id = 1;
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setSenderAccount(null);
        transaction.setRecipientAccount(null);
        transaction.setType(TransactionType.valueOf("TRANSFER"));
        transaction.setTime(Time.valueOf("12:30:00"));
        transaction.setDate(Date.valueOf("2023-10-29"));

        Transaction updatedTransaction;
        updatedTransaction = transaction;
        transaction.setAmount(new BigDecimal("20000"));

        when(request.getRequestURI()).thenReturn("/api/v1/crud/transactions/1");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(updateTransactionRequestJson)));
        when(objectMapper.readValue(updateTransactionRequestJson, Transaction.class)).thenReturn(updatedTransaction);

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        when(transactionRepository.update((long) id, updatedTransaction)).thenReturn(updatedTransaction);
        when(transactionRepository.create(updatedTransaction)).thenReturn(updatedTransaction);
        when(transactionRepository.findById(id)).thenReturn(updatedTransaction);

        crudController.doPut(request, response);

        verify(request).getRequestURI();
        verify(request).getReader();
        verify(transactionRepository).update((long) id, updatedTransaction);
        verify(response).getWriter();

        String expectedResponseBody = "{\"id\":1,\"senderAccount\":null,\"recipientAccount\":null,\"amount\":20000,\"time\":\"12:30:00\",\"date\":1698526800000,\"type\":\"TRANSFER\"}";
        Assertions.assertEquals(expectedResponseBody, responseWriter.toString().replaceAll("\\s", ""));
    }

    @Test
    void shouldSuccessPutBank() throws Exception {

        String updateBankRequestJson = "{\"id\": 1, \"name\": \"Bank\"}";
        int id = 1;
        Bank bank = new Bank();
        bank.setName("Bank");
        bank.setId(id);

        Bank updatedBank;
        updatedBank = bank;
        updatedBank.setName("CleverBank");

        when(request.getRequestURI()).thenReturn("/api/v1/crud/banks/1");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(updateBankRequestJson)));
        when(objectMapper.readValue(updateBankRequestJson, Bank.class)).thenReturn(updatedBank);

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        when(bankRepository.update(id, updatedBank)).thenReturn(updatedBank);
        when(bankRepository.create(updatedBank)).thenReturn(updatedBank);
        when(bankRepository.findById(id)).thenReturn(updatedBank);

        crudController.doPut(request, response);

        verify(request).getRequestURI();
        verify(request).getReader();
        verify(bankRepository).update(id, updatedBank);
        verify(response).getWriter();

        String expectedResponseBody = "{\"id\":1,\"name\":\"CleverBank\"}";
        ;
        Assertions.assertEquals(expectedResponseBody, responseWriter.toString().replaceAll("\\s", ""));
    }

}

