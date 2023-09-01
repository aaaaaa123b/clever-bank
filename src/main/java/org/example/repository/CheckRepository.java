package org.example.repository;

import org.example.model.Account;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface CheckRepository {

    ArrayList<Integer> findTransactions(LocalDate startDate, LocalDate endDate, Account account);
}
