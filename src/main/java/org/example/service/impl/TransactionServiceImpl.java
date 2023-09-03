package org.example.service.impl;

import org.example.model.Account;
import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.example.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalTime;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;


    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Creates a transaction.
     *
     * @param transaction The transaction object
     * @return the transaction object with the generated ID.
     */
    @Override
    public Transaction create(Transaction transaction) {
     return transactionRepository.create(transaction);
    }


}
