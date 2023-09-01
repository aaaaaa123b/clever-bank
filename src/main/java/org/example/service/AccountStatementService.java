package org.example.service;

import org.example.model.Account;
import org.example.model.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;

public interface AccountStatementService {
     void createExtract(Account account, ArrayList<Integer> ids, LocalDate startDate, LocalDate endDate);
   }
