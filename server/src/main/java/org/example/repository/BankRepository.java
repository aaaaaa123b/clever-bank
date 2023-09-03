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

    void deleteById(Long id);

    Bank create(Bank bank);

    Bank update(int id, Bank bank);
}
