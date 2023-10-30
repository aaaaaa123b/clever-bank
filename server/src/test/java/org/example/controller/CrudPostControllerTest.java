package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
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

import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CrudPostControllerTest {
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
    public void setUp() throws ServletException, IOException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSuccessPostBank() throws Exception {
        String createBankRequestJson = "{\"id\": 1, \"name\": \"Bank\"}";
        int id = 1;
        Bank bank = new Bank();
        bank.setName("Bank");
        bank.setId(id);

        when(request.getRequestURI()).thenReturn("/api/v1/crud/banks");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(createBankRequestJson)));
        when(objectMapper.readValue(createBankRequestJson, Bank.class)).thenReturn(bank);

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        when(bankRepository.create(bank)).thenReturn(bank);
        when(bankRepository.findById(id)).thenReturn(bank);
        crudController.doPost(request, response);

        verify(request).getRequestURI();
        verify(request).getReader();
        verify(bankRepository).create(bank);
        verify(response).getWriter();

        String expectedResponseBody = "{\"id\":1,\"name\":\"Bank\"}";
        Assertions.assertEquals(expectedResponseBody, responseWriter.toString().replaceAll("\\s", ""));
    }

    @Test
    void shouldSuccessPostAccount() throws Exception {
        String createAccountRequestJson = "{\"id\":1,\"userId\":1,\"bankId\":1,\"balance\":150.00,\"currency\":\"EUR\",\"number\":\"9876543210\",\"createdDate\":1698526800000}";

        int id = 1;
        Account account = new Account();
        account.setId(id);
        account.setBalance(new BigDecimal("150.00"));
        account.setCurrency("EUR");
        account.setNumber("9876543210");
        account.setUserId(1);
        account.setBankId(1);
        account.setCreatedDate(Date.valueOf("2023-10-29"));

        when(request.getRequestURI()).thenReturn("/api/v1/crud/accounts");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(createAccountRequestJson)));
        when(objectMapper.readValue(createAccountRequestJson, Account.class)).thenReturn(account);

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        when(accountService.createAccount(account)).thenReturn(account);
        when(accountService.findById(id)).thenReturn(account);

        crudController.doPost(request, response);

        verify(request).getRequestURI();
        verify(request).getReader();
        verify(accountService).createAccount(account);
        verify(response).getWriter();

        String expectedResponseBody = "{\"id\":1,\"userId\":1,\"bankId\":1,\"balance\":150.00,\"currency\":\"EUR\",\"number\":\"9876543210\",\"createdDate\":1698526800000}";
        Assertions.assertEquals(expectedResponseBody, responseWriter.toString().replaceAll("\\s", ""));
    }

    @Test
    void shouldSuccessPostUser() throws Exception {
        String createUserRequestJson = "{\"id\": 1, \"firstName\": \"A\", \"lastName\": \"B\", \"patronymic\": \"C\", \"login\": \"abc\"}";
        long id = 1L;
        User userToCreate = new User();
        userToCreate.setId(id);
        userToCreate.setFirstName("A");
        userToCreate.setLastName("B");
        userToCreate.setPatronymic("C");
        userToCreate.setLogin("abc");

        when(request.getRequestURI()).thenReturn("/api/v1/crud/users");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(createUserRequestJson)));
        when(objectMapper.readValue(createUserRequestJson, User.class)).thenReturn(userToCreate);

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        when(userService.addUser(userToCreate)).thenReturn(userToCreate);
        when(userService.findById(id)).thenReturn(userToCreate);
        crudController.doPost(request, response);

        verify(request).getRequestURI();
        verify(request).getReader();
        verify(userService).addUser(userToCreate);
        verify(response).getWriter();

        String expectedResponseBody = "{\"id\":1,\"firstName\":\"A\",\"lastName\":\"B\",\"patronymic\":\"C\",\"login\":\"abc\"}";
        Assertions.assertEquals(expectedResponseBody, responseWriter.toString().replaceAll("\\s", ""));
    }

    @Test
    void shouldSuccessPostTransaction() throws Exception {

        String createTransactionRequestJson = "{\"id\":1,\"senderAccount\":null,\"recipientAccount\":null,\"amount\":100.00,\"time\":\"12:30:00\",\"date\":1698526800000,\"type\":\"TRANSFER\"}";

        int id = 1;
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setSenderAccount(null);
        transaction.setRecipientAccount(null);
        transaction.setType(TransactionType.valueOf("TRANSFER"));
        transaction.setTime(Time.valueOf("12:30:00"));
        transaction.setDate(Date.valueOf("2023-10-29"));


        when(request.getRequestURI()).thenReturn("/api/v1/crud/transactions");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(createTransactionRequestJson)));
        when(objectMapper.readValue(createTransactionRequestJson, Transaction.class)).thenReturn(transaction);

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        when(transactionRepository.create(transaction)).thenReturn(transaction);
        when(transactionRepository.findById(id)).thenReturn(transaction);

        crudController.doPost(request, response);

        verify(request).getRequestURI();
        verify(request).getReader();
        verify(transactionRepository).create(transaction);
        verify(response).getWriter();

        String expectedResponseBody = "{\"id\":1,\"senderAccount\":null,\"recipientAccount\":null,\"amount\":100.00,\"time\":\"12:30:00\",\"date\":1698526800000,\"type\":\"TRANSFER\"}";
        Assertions.assertEquals(expectedResponseBody, responseWriter.toString().replaceAll("\\s", ""));
    }
}
