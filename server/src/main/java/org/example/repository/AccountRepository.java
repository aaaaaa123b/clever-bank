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
     * @param account    the account object
     */
    Account update(Connection connection, Account account);

    /**
     * Create an account in the database.
     *
     * @param account account object
     * @return account object.
     */
    Account create(Account account);

    /**
     * Delete an account from the database.
     *
     * @param id the account ID
     */
    void delete(Long id);

    /**
     * Update an account in the database.
     *
     * @param id the account ID
     * @param account new account object
     * @return the account object.
     */
    Account update(Long id, Account account);
}
