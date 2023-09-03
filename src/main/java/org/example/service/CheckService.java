package org.example.service;

import org.example.model.Account;
import org.example.model.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface CheckService {

    /**
     * Creates a check after a transaction.
     *
     * @param transaction the transaction object
     * @return a string containing the generated receipt.
     */
    String createCheck(Transaction transaction);

    /**
     * Finds all transactions in which the account was involved.
     *
     * @param startDate the start date to search for transactions
     * @param endDate the end date to search for transactions
     * @param account the account for which transactions should be found
     * @return a list of transaction IDs that meet the criteria.
     */
    ArrayList<Integer> findTransactions(LocalDate startDate, LocalDate endDate, Account account);

    ArrayList<Long> findAllTransactions( Account account);
}
