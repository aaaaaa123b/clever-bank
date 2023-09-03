package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.service.*;
import org.example.service.impl.*;

public class Main {

    public static void main(String[] args) {
        final ObjectMapper objectMapper = new ObjectMapper();

        final UserService userService = new UserServiceImpl(objectMapper);
        final AccountService accountService = new AccountServiceImpl();
        final AccountStatementService accountStatementService = new AccountStatementServiceImpl();
        final MoneyStatementService moneyStatementService = new MoneyStatementServiceImpl();

        CleverBankApplication application = new CleverBankApplication(
                userService, accountService, accountStatementService,
                moneyStatementService);

        application.start();
    }
}