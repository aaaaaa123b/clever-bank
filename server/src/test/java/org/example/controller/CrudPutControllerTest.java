package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CrudPutControllerTest {


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
    public void testDoPutUpdateUser() throws Exception {
        Long userId = 1L;
        String updateUserRequestJson = "{\"id\": 1, \"firstName\": \"A\", \"lastName\": \"B\", \"patronymic\": \"C\", \"login\": \"abc\"}";
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setFirstName("A");
        updatedUser.setLastName("B");
        updatedUser.setPatronymic("C");
        updatedUser.setLogin("abc");

        User newUser = new User();
        newUser=updatedUser;
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
}

