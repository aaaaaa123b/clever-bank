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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CrudPostControllerTest {
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
    public void testDoPostCreateUser() throws Exception {
        String createUserRequestJson = "{\"id\": 1, \"firstName\": \"A\", \"lastName\": \"B\", \"patronymic\": \"C\", \"login\": \"abc\"}";
        long id =1L;
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
}
