package org.example.service;

import org.example.model.Account;

import java.math.BigDecimal;

public interface AccountService {

    Account withdrawCash(long account, BigDecimal cash);

    Account addCash(long account, BigDecimal cash);

    void transfer(String source, String targer, BigDecimal cash);

    Account findById(long id);
}
