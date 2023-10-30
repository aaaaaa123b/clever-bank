package org.example.service;

import org.example.model.Account;

import java.time.LocalDate;
import java.util.ArrayList;

public interface AccountStatementService {

    /**
     * Creates an account statement.
     *
     * @param account   the account
     * @param ids       a list of transaction IDs associated with the account
     * @param startDate the start date for the statement
     * @param endDate   the end date for the statement
     */
    StringBuilder createStringExtract(Account account, ArrayList<Long> ids, LocalDate startDate, LocalDate endDate);

    byte[] createExtract(StringBuilder extract);
}
