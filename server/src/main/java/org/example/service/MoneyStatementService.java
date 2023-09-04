package org.example.service;

import org.example.model.Account;

import java.util.ArrayList;

public interface MoneyStatementService {

    /**
     * Create money statement
     *
     * @param account account object
     * @param ids ids that need for statement
     * @return bytes.
     */
    byte[] createStatement(Account account, ArrayList<Long> ids);
}
