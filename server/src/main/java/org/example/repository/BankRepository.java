package org.example.repository;

import org.example.model.Bank;

public interface BankRepository {

    /**
     * Finds a bank by its ID in the database.
     *
     * @param id the bank ID
     * @return the bank object from the database.
     */
    Bank findById(int id);

    /**
     * Delete bank from the database.
     *
     * @param id bank ID
     */
    void deleteById(Long id);

    /**
     * Creates a bank in the database.
     *
     * @param bank the bank object
     * @return the bank object with the generated ID.
     */
    Bank create(Bank bank);

    /**
     * Update a bank in the database.
     *
     * @param id the bank ID
     * @param bank new bank object
     * @return the bank object.
     */
    Bank update(int id, Bank bank);
}
