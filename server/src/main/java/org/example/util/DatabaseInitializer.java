package org.example.util;

import org.example.service.AccountService;
import org.example.service.UserService;

public class DatabaseInitializer {

    private final UserService userService;
    private final AccountService accountService;

    public DatabaseInitializer(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    public void initialize() {

    }
}
