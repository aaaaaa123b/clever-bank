package org.example.service;

import org.example.model.Account;

import java.util.ArrayList;

public interface MoneyStatementService {
    byte[] createStatement(Account account, ArrayList<Long> ids);
}
