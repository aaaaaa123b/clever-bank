package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Account;
import org.example.model.Bank;
import org.example.model.User;
import org.example.repository.BankRepository;
import org.example.repository.TransactionRepository;
import org.example.service.AccountService;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CrudDeleteControllerTest {
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
    void shouldSuccessDeleteUser() throws Exception {
        Long userId = 100L;
        User user = new User();
        user.setId(userId);
        user.setFirstName("A");
        user.setLastName("B");
        user.setPatronymic("C");
        user.setLogin("abc");

        when(request.getRequestURI()).thenReturn("/api/v1/crud/users/100");
        crudController.doDelete(request, response);

        verify(request).getRequestURI();
        verify(userService).deleteById(userId);
    }

    @Test
    void shouldSuccessDeleteAccount() throws Exception {
        int id = 100;
        Account account = new Account();
        account.setId(id);
        account.setBalance(new BigDecimal("150.00"));
        account.setCurrency("EUR");
        account.setNumber("9876543210");
        account.setUserId(1);
        account.setBankId(1);
        account.setCreatedDate(Date.valueOf("2023-10-29"));

        when(request.getRequestURI()).thenReturn("/api/v1/crud/accounts/100");
        crudController.doDelete(request, response);

        verify(request).getRequestURI();
        verify(accountService).deleteById((long) id);
    }

    @Test
    void shouldSuccessDeleteBank() throws Exception {
        int id = 1;
        Bank bank = new Bank();
        bank.setName("Bank");
        bank.setId(id);

        when(request.getRequestURI()).thenReturn("/api/v1/crud/banks/1");
        crudController.doDelete(request, response);

        verify(request).getRequestURI();
        verify(bankRepository).deleteById((long) id);
    }
}
