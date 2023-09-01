package org.example.repository;

import org.example.model.Bank;
import org.example.model.User;

public interface BankRepository {

    Bank findById(int id);

}
