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

    public AccountServiceImpl(AccountRepository accountRepository,ConnectionManager connectionManager) {
        this.accountRepository=accountRepository;
        this.connectionManager=connectionManager;
    }
    @Override
    public Account withdrawCash(Account account, BigDecimal cash) {
        if (account.getBalance().compareTo(cash) < 0) {
            throw new NotEnoughMoneyException();
        }
        try (Connection connection = connectionManager.getConnection()){
            connection.setAutoCommit(false);

            try {
                account.setBalance(account.getBalance().subtract(cash));
                accountRepository.update(connection,account);

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

    @Override
    public Account addCash(Account account, BigDecimal cash) {
        try (Connection connection = connectionManager.getConnection()){
            connection.setAutoCommit(false);

            try {
                account.setBalance(account.getBalance().add(cash));
                accountRepository.update(connection,account);

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

    @Override
    public Account transfer(Account source, Account targer, BigDecimal cash) {
        try (Connection connection = connectionManager.getConnection()){
            connection.setAutoCommit(false);

            try {
            source.setBalance(source.getBalance().subtract(cash));
            targer.setBalance(targer.getBalance().add(cash));

            accountRepository.update(connection, source);
            accountRepository.update(connection, targer);

            connection.commit();

            } catch (SQLException e) {
                //throw new RuntimeException("Ошибка при обработке SQL-запроса", e);
                try {
                    // STEP 3 - Roll back transaction
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

    @Override
    public Account findById(long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public Account save(Account account) {
        return null;
    }
}
