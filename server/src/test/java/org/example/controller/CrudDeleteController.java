package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CrudDeleteController {
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
    public void testDoDeleteUser() throws Exception {
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

}
