package org.example.service;

import org.example.model.Account;

import java.math.BigDecimal;

public interface AccountService {

    /**
     * Withdraws money from an account.
     *
     * @param account the account to withdraw funds from
     * @param cash the amount of cash to withdraw
     * @return the updated account object.
     */
    Account withdrawCash(Account account, BigDecimal cash);

    /**
     * Deposits money into an account.
     *
     * @param account the account to deposit funds into
     * @param cash the amount of cash to deposit
     * @return the updated account object.
     */
    Account addCash(Account account, BigDecimal cash);

    /**
     * Transfers money from one account to another.
     *
     * @param source the sender's account
     * @param target the recipient's account
     * @param cash the amount of cash to transfer
     * @return the updated sender's account object.
     */
    Account transfer(Account source, Account target, BigDecimal cash);

    /**
     * Finds an account by its ID.
     *
     * @param id the account ID
     * @return the account object found by its ID.
     */
    Account findById(long id);

    /**
     * Finds an account by its number.
     *
     * @param number the account number
     * @return the account object found by its number.
     */
    Account findByNumber(String number);

    Account createAccount(Account account);

    /**
     * Delete account by account id.
     *
     * @param id account ID
     */
    void deleteById(Long id);

    Account updateAccount(Long id, Account account);
}
