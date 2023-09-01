package org.example.service;

import org.example.model.Account;
import org.example.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalTime;

public interface TransactionService {

    Transaction create(Transaction transaction);

    Transaction findAll(Account account, LocalTime start, LocalTime date);

    BigDecimal configuration();

}
