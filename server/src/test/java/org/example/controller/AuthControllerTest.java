package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.DependencyProvider;
import org.example.dto.LoginRequestDto;
import org.example.dto.SignupRequestDto;
import org.example.model.User;
import org.example.service.UserService;
import org.example.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.function.Function;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    @Mock
    private ServletConfig servletConfig;

    @Mock
    public UserService userService;

    @Mock
    public UserServiceImpl userServiceImpl;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter responseWriter;

    @InjectMocks
    private AuthController authController;

    @Mock
    private DependencyProvider dependencyProvider;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDoPostForLogin() throws Exception {
        String loginRequestJson = "{\"userId\": 1}";

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUserId(1L);

        Function<Long, User> createUser = (id) -> {
            User user = new User();
            user.setId(1L);
            user.setFirstName("A");
            user.setLastName("B");
            user.setPatronymic("C");
            user.setLogin("abc");
            return user;
        };

        when(userService.findById(1L)).thenReturn(createUser.apply(1L));
        when(userServiceImpl.findById(1L)).thenReturn(createUser.apply(1L));

        when(dependencyProvider.forClass(UserService.class)).thenReturn(userService);
        when(request.getRequestURI()).thenReturn("/api/v1/auth/login/");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(loginRequestJson)));
        when(objectMapper.readValue(loginRequestJson, LoginRequestDto.class)).thenReturn(loginRequestDto);
        when(response.getWriter()).thenReturn(responseWriter);

        authController.doPost(request, response);

        verify(request).getReader();
        verify(response).getWriter();
        verify(userService).findById(1L);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        String expectedResponseBody = """
                {
                    "id": "1",
                    "firstName": "A",
                    "lastName": "B",
                    "patronymic": "C",
                    "login": "abc"
                }
                """;

        verify(responseWriter).print(expectedResponseBody);
        verify(responseWriter).flush();
    }


    @Test
    public void testDoSignup() throws Exception {
        String signupRequestJson = "{\"firstName\": \"A\", \"lastName\": \"B\", \"patronymic\": \"C\", \"login\": \"abc\"}";

        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setFirstName("A");
        signupRequestDto.setLastName("B");
        signupRequestDto.setPatronymic("C");
        signupRequestDto.setLogin("abc");

        User user = new User();
        user.setId(1L);
        user.setFirstName("A");
        user.setLastName("B");
        user.setPatronymic("C");
        user.setLogin("abc");

        when(request.getRequestURI()).thenReturn("/api/v1/auth/signup/");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(signupRequestJson)));
        when(objectMapper.readValue(signupRequestJson, SignupRequestDto.class)).thenReturn(signupRequestDto);
        when(userService.addUser("A", "B", "C", "abc")).thenReturn(user);

        when(response.getWriter()).thenReturn(responseWriter);

        authController.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        String expectedResponseBody = """
                {
                    "id": "1",
                    "firstName": "A",
                    "lastName": "B",
                    "patronymic": "C",
                    "login": "abc"
                }
                """;

        verify(responseWriter).print(expectedResponseBody);
        verify(responseWriter).flush();
    }


}

