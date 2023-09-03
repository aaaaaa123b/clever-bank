package org.example.service;

import org.example.model.Account;

import java.time.LocalDate;

public interface AccountStatementService {

    void createExtract(String accountNumber, LocalDate startDate, LocalDate endDate);
   }
