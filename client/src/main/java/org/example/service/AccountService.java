package org.example.service;

import org.example.model.Account;

import java.math.BigDecimal;

public interface AccountService {

    /**
     * Metod wich withdraw cash from account.
     *
     * @param account account object
     * @param cash amount of money
     * @return account object.
     */
    Account withdrawCash(long account, BigDecimal cash);

    /**
     * Metod wich add cash from account.
     *
     * @param account account object
     * @param cash amount of money
     * @return account object
     */
    Account addCash(long account, BigDecimal cash);

    /**
     * Metod wich transfer cash from account.
     *
     * @param source sender account object
     * @param targer recipient account object
     * @param cash amount of money
     */
    void transfer(String source, String targer, BigDecimal cash);

    /**
     * Find account.
     *
     * @param id account ID
     * @return account object.
     */
    Account findById(long id);
}
