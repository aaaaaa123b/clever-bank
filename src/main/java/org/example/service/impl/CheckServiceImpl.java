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

    @Override
    public void createCheck(Transaction transaction) {
        int senderBankId = transaction.getSenderAccount().getBankId();
        Bank senderBank = bankRepository.findById(senderBankId);
        String senderBankName = senderBank.getName();

        int recipientBankId = transaction.getRecipientAccount().getBankId();
        Bank recipientBank = bankRepository.findById(recipientBankId);
        String recipientBankName = recipientBank.getName();

//        System.out.println("----------------------------------------");
//        System.out.println("|           Банковский чек             |");
//        System.out.printf("| Чек:                             %-24s|%n",transaction.getId());
//        System.out.printf("| %s                               %-24s|%n",transaction.getDate(),transaction.getTime());
//        System.out.printf("| Тип транзакции:                  %-24s|%n",transaction.getType());
//        System.out.printf("| Банк отправителя:                %-24s|%n",senderBankName);
//        System.out.printf("| Банк получателя:                 %-24s|%n",recipientBankName);
//        System.out.printf("| Счёт отправителя:        %s           |%n",transaction.getSenderAccount().getNumber());
//        System.out.printf("| Счёт получателя:         %s           |%n",transaction.getRecipientAccount().getNumber());
//        System.out.printf("| Сумма:                   %s %s        |%n",transaction.getAmount(),transaction.getSenderAccount().getCurrency());
//        System.out.println("----------------------------------------");

        var checkText = """
                -----------------------------------------
                |            Банковский чек             |
                | Чек:                               %s |
                | %s                                 %s |
                | Тип транзакции:                    %s |
                | Банк отправителя:                  %s |
                | Банк получателя:                   %s |
                | Счёт отправителя:                  %s |
                | Счёт получателя:                   %s |
                | Сумма:                          %s %s |
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
    }

    @Override
    public ArrayList<Integer> findTransactions(LocalDate startDate, LocalDate endDate, Account account) {
        return checkRepository.findTransactions(startDate, endDate, account);
    }


}

