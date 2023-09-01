package org.example.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class HelpController extends HttpServlet {

    public static final String API_PREFIX = "/api/v1";
    public static final String HELP = API_PREFIX + "/help";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String body = """
        {
            "auth": {
                "login": "POST %s",
                "signup": "POST %s"
            },
            "accounts": {
                "balance": "GET %s",
                "deposit": "POST %s",
                "withdraw": "POST %s",
                "transfer": "POST %s",
                "extract": "POST %s"
            },
            "help": {
                "endpoints": "GET %s"
            }
        }
        """.formatted(
                AuthController.LOGIN_PATTERN,
                AuthController.SIGNUP_PATTERN,
                AccountController.BALANCE,
                AccountController.DEPOSIT,
                AccountController.WITHDRAW,
                AccountController.TRANSFER,
                AccountController.EXTRACT,
                HELP
        );

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(body);
        out.flush();
    }
}

