package org.example.repository;

import org.example.model.Transaction;

public interface TransactionRepository {

    /**
     * Creates a transaction in the database.
     *
     * @param transaction the transaction object
     * @return the transaction object with the generated ID.
     */
    Transaction create(Transaction transaction);

    /**
     * Finds a transaction by its ID in the database.
     *
     * @param id the transaction ID
     * @return the transaction object.
     */
    Transaction findById(long id);

    /**
     * Update a transaction in the database.
     *
     * @param id          the transaction ID
     * @param transaction new transaction object
     * @return the transaction object.
     */
    Transaction update(Long id, Transaction transaction);
}
