package org.example.service;

import org.example.model.Account;
import org.example.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalTime;

public interface TransactionService {

    /**
     * Creates a transaction.
     *
     * @param transaction The transaction object
     * @return the transaction object with the generated ID.
     */
    Transaction create(Transaction transaction);

}
