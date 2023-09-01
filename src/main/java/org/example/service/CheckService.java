package org.example.service;

import org.example.model.Account;
import org.example.model.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface CheckService {

    void createCheck(Transaction transaction);

    ArrayList<Integer> findTransactions(LocalDate startDate, LocalDate endDate, Account account);

}
