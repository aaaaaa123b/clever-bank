package org.example.repository;

import org.example.model.Account;

import java.sql.Connection;

public interface AccountRepository {

    /**
     * Finds an account by its ID in the database.
     *
     * @param id the account ID
     * @return the transaction object.
     */
    Account findById(Long id);

    /**
     * Finds an account by its number in the database.
     *
     * @param number the account number
     * @return the account object found by its number.
     */
    Account findByNumber(String number);

    /**
     * Updates account data in the database.
     *
     * @param connection the database connection
     * @param account the account object
     */
    void update(Connection connection, Account account);

    Account create(Account account);
}
