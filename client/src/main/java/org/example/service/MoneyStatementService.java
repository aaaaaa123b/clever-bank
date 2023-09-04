package org.example.service;

import java.time.LocalDate;

public interface MoneyStatementService {

    void createStatement(String accountNumber, LocalDate startDate,LocalDate endDate);
}
