package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.DependencyProvider;
import org.example.dto.LoginRequestDto;
import org.example.dto.SignupRequestDto;
import org.example.model.User;
import org.example.service.UserService;

import java.io.IOException;
import java.io.PrintWriter;

public class AuthController extends HttpServlet {

    public static final String API_PREFIX_PATTERN = ".*/api/v1";
    public static final String LOGIN_PATTERN = API_PREFIX_PATTERN + "/auth/login/";

    public static final String SIGNUP_PATTERN = API_PREFIX_PATTERN + "/auth/signup/";

    private ObjectMapper objectMapper;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        objectMapper = (ObjectMapper) DependencyProvider.get().forClass(ObjectMapper.class);
        userService = (UserService) DependencyProvider.get().forClass(UserService.class);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String path = request.getRequestURI();
        final String message = request.getReader().lines()
                .reduce("", (accumulator, actual) -> accumulator + actual);

        if (path.matches(LOGIN_PATTERN)) {
            doLogin(message, response);
            return;
        }

        if (path.matches(SIGNUP_PATTERN)) {
            doSignup(message, response);
            return;
        }
    }

    private void doLogin(String message, HttpServletResponse response) {
        final LoginRequestDto dto;
        try {
            dto = objectMapper.readValue(message, LoginRequestDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        final User user = userService.findById(dto.getUserId());

        final String body = """
                {
                    "id": "%s",
                    "firstName": "%s",
                    "lastName": "%s",
                    "patronymic": "%s",
                    "login": "%s"
                }
                """.formatted(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPatronymic(),
                user.getLogin()
        );
        send(body,response);
    }

    private void doSignup(String message, HttpServletResponse response) throws JsonProcessingException {

        final SignupRequestDto dto = objectMapper.readValue(message, SignupRequestDto.class);

        final User user = userService.addUser(dto.getFirstName(), dto.getLastName(), dto.getPatronymic(), dto.getLogin());

        final String body = """
                {
                    "id": "%s",
                    "firstName": "%s",
                    "lastName": "%s",
                    "patronymic": "%s",
                    "login": "%s"
                }
                """.formatted(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPatronymic(),
                user.getLogin()
        );

        send(body, response);
    }

    private void send(String message, HttpServletResponse response) {
        final PrintWriter out;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        out.print(message);
        out.flush();
    }
}

