package org.example.repository;

import org.example.model.Account;

import java.sql.Connection;

public interface AccountRepository {

    Account findById(Long id);

    Account findByNumber(String number);

    Account update(Connection connection,Account account);

    Account create(Account account);
}
