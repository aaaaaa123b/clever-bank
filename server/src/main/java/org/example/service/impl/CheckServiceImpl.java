package org.example.service.impl;

import org.example.model.Account;
import org.example.model.Bank;
import org.example.model.Transaction;
import org.example.repository.BankRepository;
import org.example.repository.CheckRepository;
import org.example.service.CheckService;

import java.time.LocalDate;
import java.util.ArrayList;

public class CheckServiceImpl implements CheckService {
    private final BankRepository bankRepository;

    public final CheckRepository checkRepository;


    public CheckServiceImpl(BankRepository bankRepository, CheckRepository checkRepository) {
        this.bankRepository = bankRepository;
        this.checkRepository = checkRepository;
    }

    /**
     * Creates a check after a transaction.
     *
     * @param transaction the transaction object
     * @return a string containing the generated receipt.
     */
    @Override
    public String createCheck(Transaction transaction) {
        int senderBankId = transaction.getSenderAccount().getBankId();
        Bank senderBank = bankRepository.findById(senderBankId);
        String senderBankName = senderBank.getName();

        int recipientBankId = transaction.getRecipientAccount().getBankId();
        Bank recipientBank = bankRepository.findById(recipientBankId);
        String recipientBankName = recipientBank.getName();

        var checkText = """
                -----------------------------------------
                |            Банковский чек             |
                | Чек:                       %11s |
                | %-26s %10s |
                | Тип транзакции:            %11s |
                | Банк отправителя:          %11s |
                | Банк получателя:           %11s |
                | Счёт отправителя:          %11s |
                | Счёт получателя:           %11s |
                | Сумма:                  %11s%s |
                -----------------------------------------
                """.formatted(
                transaction.getId(),
                transaction.getDate(), transaction.getTime(),
                transaction.getType(),
                senderBankName,
                recipientBankName,
                transaction.getSenderAccount().getNumber(),
                transaction.getRecipientAccount().getNumber(),
                transaction.getAmount(), transaction.getSenderAccount().getCurrency()
        );

        System.out.println(checkText);

        return checkText;
    }

    /**
     * Finds all transactions in which the account was involved.
     *
     * @param startDate the start date to search for transactions
     * @param endDate the end date to search for transactions
     * @param account the account for which transactions should be found
     * @return a list of transaction IDs that meet the criteria.
     */
    @Override
    public ArrayList<Long> findTransactions(LocalDate startDate, LocalDate endDate, Account account) {
        return checkRepository.findTransactions(startDate, endDate, account);
    }

    /**
     * Finds all transactions in which the account was involved.
     *
     * @param account the account for which transactions should be found
     * @return a list of transaction IDs that meet the criteria.
     */
    @Override
    public ArrayList<Long> findAllTransactions( Account account) {
        return checkRepository.findAllTransactions(account);
    }
}

