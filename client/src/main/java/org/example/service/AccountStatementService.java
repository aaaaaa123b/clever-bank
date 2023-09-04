package org.example.service;

import java.time.LocalDate;

public interface AccountStatementService {

    /**
     * Create extraxt.
     *
     * @param accountNumber number of account
     * @param startDate the date on which the statement begins
     * @param endDate the date on which the statement ends
     */
    void createExtract(String accountNumber, LocalDate startDate, LocalDate endDate);
   }
