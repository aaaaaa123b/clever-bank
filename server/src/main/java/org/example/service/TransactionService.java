package org.example.service;

import org.example.model.Transaction;

public interface TransactionService {

    /**
     * Creates a transaction.
     *
     * @param transaction The transaction object
     * @return the transaction object with the generated ID.
     */
    Transaction create(Transaction transaction);

}
