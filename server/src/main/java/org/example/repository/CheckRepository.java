package org.example.repository;

import org.example.model.Account;

import java.time.LocalDate;
import java.util.ArrayList;

public interface CheckRepository {

    /**
     * Finds all transactions in which the account was involved in the database.
     *
     * @param startDate the start date to search for transactions
     * @param endDate the end date to search for transactions
     * @param account the account for which transactions should be found
     * @return a list of transaction IDs that meet the criteria.
     */
    ArrayList<Long> findTransactions(LocalDate startDate, LocalDate endDate, Account account);

    /**
     * Finds all transactions in which the account was involved in the database.
     *
     * @param account the account for which transactions should be found
     * @return a list of transaction IDs that meet the criteria.
     */
    ArrayList<Long> findAllTransactions( Account account);
}
