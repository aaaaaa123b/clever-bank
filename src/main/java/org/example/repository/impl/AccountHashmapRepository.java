package org.example.repository.impl;

import org.example.exception.EntityNotFoundException;
import org.example.model.Account;
import org.example.repository.AccountRepository;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class AccountHashmapRepository implements AccountRepository {

    private final Map<Long, Account> accounts = new HashMap<>();

    @Override
    public Account findById(Long id) {
        final Account account = accounts.get(id);

        if (account == null) {
            String message = "Счёт с id %d не найден.".formatted(id);
            throw new EntityNotFoundException(message);
        }

        return account;
    }

    @Override
    public Account findByNumber(String number) {
        return null;
    }

    @Override
    public Account update(Connection connection, Account account) {
        return null;
    }


    @Override
    public Account create(Account account) {
       return  accounts.put(account.getId(), account);
    }
}
