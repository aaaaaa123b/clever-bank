package org.example.service.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.example.model.Account;
import org.example.model.Bank;
import org.example.model.Transaction;
import org.example.repository.BankRepository;
import org.example.repository.CheckRepository;
import org.example.service.CheckService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

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
                | Чек:                       %10s |
                | %-26s %10s |
                | Тип транзакции:            %10s |
                | Банк отправителя:          %10s |
                | Банк получателя:           %10s |
                | Счёт отправителя:          %10s |
                | Счёт получателя:           %10s |
                | Сумма:                  %10s%s |
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
    public ArrayList<Integer> findTransactions(LocalDate startDate, LocalDate endDate, Account account) {
        return checkRepository.findTransactions(startDate, endDate, account);
    }

    @Override
    public ArrayList<Long> findAllTransactions( Account account) {
        return checkRepository.findAllTransactions(account);
    }
}

