package org.example.repository;

import org.example.model.Transaction;

public interface TransactionRepository {

    Transaction create(Transaction transaction);

    Transaction findById(int id);
}
