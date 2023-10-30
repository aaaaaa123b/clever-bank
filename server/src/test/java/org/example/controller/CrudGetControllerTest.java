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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CrudGetControllerTest {
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
    public void testDoGetUser() throws Exception {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFirstName("A");
        user.setLastName("B");
        user.setPatronymic("C");
        user.setLogin("abc");

        when(request.getRequestURI()).thenReturn("/api/v1/crud/users/1");
        when(userService.findById(userId)).thenReturn(user);

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        crudController.doGet(request, response);

        verify(request).getRequestURI();
        verify(userService).findById(userId);
        verify(response).getWriter();

        String expectedResponseBody = "{\"id\":1,\"firstName\":\"A\",\"lastName\":\"B\",\"patronymic\":\"C\",\"login\":\"abc\"}";
        Assertions.assertEquals(expectedResponseBody, responseWriter.toString().replaceAll("\\s", ""));
    }

    @Test
    public void testDoGetAccount() throws Exception {

        long id = 1L;
        Account account = new Account();
        account.setId(id);
        account.setBalance(new BigDecimal("150.00"));
        account.setCurrency("EUR");
        account.setNumber("9876543210");
        account.setUserId(1);
        account.setBankId(1);
        account.setCreatedDate(Date.valueOf("2023-10-29"));

        when(request.getRequestURI()).thenReturn("/api/v1/crud/accounts/1");
        when(accountService.findById(id)).thenReturn(account);

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        crudController.doGet(request, response);

        verify(request).getRequestURI();
        verify(accountService).findById(id);
        verify(response).getWriter();

        String expectedResponseBody = "{\"id\":1,\"userId\":1,\"bankId\":1,\"balance\":150.00,\"currency\":\"EUR\",\"number\":\"9876543210\",\"createdDate\":1698526800000}";
        Assertions.assertEquals(expectedResponseBody, responseWriter.toString().replaceAll("\\s", ""));
    }

    @Test
    public void testDoGetBank() throws Exception {

        int id = 1;
        Bank bank = new Bank();
        bank.setName("Bank");
        bank.setId(id);

        when(request.getRequestURI()).thenReturn("/api/v1/crud/banks/1");
        when(bankRepository.findById(id)).thenReturn(bank);

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        crudController.doGet(request, response);

        verify(request).getRequestURI();
        verify(bankRepository).findById(id);
        verify(response).getWriter();

        String expectedResponseBody = "{\"id\":1,\"name\":\"Bank\"}";
        Assertions.assertEquals(expectedResponseBody, responseWriter.toString().replaceAll("\\s", ""));
    }

    @Test
    public void testDoGetTransaction() throws Exception {

        Account senderAccount = new Account();
        Account recipientAccount = new Account();

        int id = 1;
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setTime(Time.valueOf("12:00:00"));
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(BigDecimal.valueOf(100.0));
        transaction.setDate(java.sql.Date.valueOf("2023-09-01"));
        transaction.setSenderAccount(senderAccount);
        transaction.setRecipientAccount(recipientAccount);

        when(request.getRequestURI()).thenReturn("/api/v1/crud/transactions/1");
        when(transactionRepository.findById(id)).thenReturn(transaction);

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        crudController.doGet(request, response);

        verify(request).getRequestURI();
        verify(transactionRepository).findById(id);
        verify(response).getWriter();

        String expectedResponseBody = "{\"id\":1,\"senderAccount\":{\"id\":0,\"userId\":0,\"bankId\":0,\"balance\":null,\"currency\":null,\"number\":null,\"createdDate\":null},\"recipientAccount\":{\"id\":0,\"userId\":0,\"bankId\":0,\"balance\":null,\"currency\":null,\"number\":null,\"createdDate\":null},\"amount\":100.0,\"time\":\"12:00:00\",\"date\":1693515600000,\"type\":\"DEPOSIT\"}";
        Assertions.assertEquals(expectedResponseBody, responseWriter.toString().replaceAll("\\s", ""));
    }



}
