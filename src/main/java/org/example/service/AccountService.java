package org.example.service;

import org.example.model.Account;

import java.math.BigDecimal;

public interface AccountService {

    Account withdrawCash(Account account, BigDecimal cash);

    Account addCash(Account account, BigDecimal cash);

    Account transfer(Account source, Account targer, BigDecimal cash);

    Account findById(long id);

    Account findByNumber(String number);

    Account save(Account account);

}
