package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.DependencyProvider;
import org.example.service.UserService;

import java.io.IOException;
import java.io.PrintWriter;

public class AccountController extends HttpServlet {

    public static final String API_PREFIX = "/api/v1";
    public static final String ACCOUNTS_PREFIX = API_PREFIX + "/users/(\\d+)/accounts/(\\d+)";

    public static final String BALANCE = ACCOUNTS_PREFIX + "/balance/";
    public static final String DEPOSIT = ACCOUNTS_PREFIX + "/deposit/";
    public static final String WITHDRAW = ACCOUNTS_PREFIX + "/withdraw/";
    public static final String TRANSFER = ACCOUNTS_PREFIX + "/transfer/";
    public static final String EXTRACT = ACCOUNTS_PREFIX + "/extract/";

    public static final String AUTH_PREFIX = API_PREFIX + "/auth";
    public static final String LOGIN_PREFIX = AUTH_PREFIX + "/login/";
    public static final String SIGNUP_PREFIX = AUTH_PREFIX + "/signup/";

    private ObjectMapper objectMapper;
    private UserService userService;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String path = req.getRequestURI();

//        resp.getWriter().print("""
//                {
//                    "id": 1,
//                    "balance": 100,
//                    "currency": "BYN",
//                    "number": "AA1234567",
//                    "userId": 1,
//                    "bankId": 1,
//                    "createdDate": "31.08.2023"
//                }
//                """);
        resp.getWriter().println(path);
    }

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

