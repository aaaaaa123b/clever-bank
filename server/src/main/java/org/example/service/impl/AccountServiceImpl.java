package org.example.service.impl;

import org.example.exception.NotEnoughMoneyException;
import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.example.service.AccountService;
import org.example.util.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final ConnectionManager connectionManager;

    public AccountServiceImpl(AccountRepository accountRepository, ConnectionManager connectionManager) {
        this.accountRepository = accountRepository;
        this.connectionManager = connectionManager;
    }

    /**
     * Withdraws money from an account.
     *
     * @param account the account to withdraw funds from
     * @param cash    the amount of cash to withdraw
     * @return the updated account object.
     */
    @Override
    public Account withdrawCash(Account account, BigDecimal cash) {
        if (account.getBalance().compareTo(cash) < 0) {
            throw new NotEnoughMoneyException();
        }
        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);

            try {
                account.setBalance(account.getBalance().subtract(cash));
                accountRepository.update(connection, account);

                connection.commit();

            } catch (SQLException e) {
                try {

                    System.out.println("Transaction is being rolled back.");
                    connection.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return account;
    }

    /**
     * Deposits money into an account.
     *
     * @param account the account to deposit funds into
     * @param cash    the amount of cash to deposit
     * @return the updated account object.
     */
    @Override
    public Account addCash(Account account, BigDecimal cash) {
        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);

            try {
                account.setBalance(account.getBalance().add(cash));
                accountRepository.update(connection, account);

                connection.commit();

            } catch (SQLException e) {
                //throw new RuntimeException("Ошибка при обработке SQL-запроса", e);
                try {

                    System.out.println("Transaction is being rolled back.");
                    connection.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return account;
    }

    /**
     * Transfers money from one account to another.
     *
     * @param source the sender's account
     * @param target the recipient's account
     * @param cash   the amount of cash to transfer
     * @return the updated sender's account object.
     */
    @Override
    public Account transfer(Account source, Account target, BigDecimal cash) {
        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);

            try {
                source.setBalance(source.getBalance().subtract(cash));
                target.setBalance(target.getBalance().add(cash));

                accountRepository.update(connection, source);
                accountRepository.update(connection, target);

                connection.commit();

            } catch (SQLException e) {
                //throw new RuntimeException("Ошибка при обработке SQL-запроса", e);
                try {
                    System.out.println("Transaction is being rolled back.");
                    connection.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return source;
    }

    /**
     * Finds an account by its ID.
     *
     * @param id the account ID
     * @return the account object found by its ID.
     */
    @Override
    public Account findById(long id) {
        return accountRepository.findById(id);
    }

    /**
     * Finds an account by its number.
     *
     * @param number the account number
     * @return the account object found by its number.
     */
    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    /**
     * Create an account in the database.
     *
     * @param account account object
     * @return account object.
     */
    @Override
    public Account createAccount(Account account) {
        return accountRepository.create(account);
    }

    /**
     * Delete an account from the database.
     *
     * @param id the account ID
     */
    @Override
    public void deleteById(Long id) {
        accountRepository.delete(id);
    }

    /**
     * Update an account in the database.
     *
     * @param id the account ID
     * @param account new account object
     * @return the account object.
     */
    @Override
    public Account updateAccount(Long id, Account account) {
        return accountRepository.update(id, account);
    }
}
