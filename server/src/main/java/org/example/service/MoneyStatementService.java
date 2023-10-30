package org.example.service;

import org.example.model.Account;

import java.time.LocalDate;
import java.util.ArrayList;

public interface MoneyStatementService {


    byte[] createStatement(StringBuilder statement);

    StringBuilder createStringStatement(Account account, ArrayList<Long> ids, LocalDate start, LocalDate end);
}
