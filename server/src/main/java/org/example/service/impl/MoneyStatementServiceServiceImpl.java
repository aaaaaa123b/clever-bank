package org.example.service.impl;

import org.example.model.Account;
import org.example.model.Bank;
import org.example.model.Transaction;
import org.example.model.User;
import org.example.repository.BankRepository;
import org.example.repository.TransactionRepository;
import org.example.service.MoneyStatementService;
import org.example.service.UserService;
import org.example.util.PdfUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class MoneyStatementServiceServiceImpl implements MoneyStatementService {

    private final BankRepository bankRepository;

    private final UserService userService;

    private final TransactionRepository transactionRepository;

    public MoneyStatementServiceServiceImpl(BankRepository bankRepository, UserService userService, TransactionRepository transactionRepository) {
        this.bankRepository = bankRepository;
        this.userService = userService;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Create money statement
     *
     * @param account account object
     * @param ids ids that need for statement
     * @return bytes.
     */
    public StringBuilder createStringStatement(Account account, ArrayList<Long> ids, LocalDate start, LocalDate end) {
        BigDecimal deposit = BigDecimal.ZERO;
        BigDecimal withdraw = BigDecimal.ZERO;

        int senderBankId = account.getBankId();
        Bank senderBank = bankRepository.findById(senderBankId);

        long userId = account.getUserId();
        User user = userService.findById(userId);

        LocalTime currentTime = LocalTime.ofSecondOfDay(LocalTime.now().getHour());



        for (Long id : ids) {
            Transaction transaction = transactionRepository.findById(id);

            switch (transaction.getType()) {
                case DEPOSIT -> deposit = deposit.add(transaction.getAmount());
                case WITHDRAW -> withdraw = withdraw.subtract(transaction.getAmount());
                case TRANSFER -> {
                    System.out.println(transaction);
                    if (transaction.getSenderAccount().getId() == account.getId())
                        withdraw = withdraw.subtract(transaction.getAmount());
                    else if (transaction.getRecipientAccount().getId() == account.getId())
                        deposit = deposit.add(transaction.getAmount());
                }
            }

            System.out.println("Deposit: " + deposit);
            System.out.println("Withdraw: " + withdraw);
        }

        StringBuilder extractText = new StringBuilder("""
                                            Money statement
                                                 %s
                Клиент                           | %s %s %s
                Счет                             | %s
                Валюта                           | %s
                Дата открытия                    | %s
                Период                           | %s - %s
                Дата и время формирования        | %s %s
                Остаток                          | %s
                            Приход           | Уход
                          -----------------------------------
                              %s                %s
                """.formatted(

                senderBank.getName(), user.getFirstName(),
                user.getLastName(), user.getPatronymic(),
                account.getNumber(), account.getCurrency(),
                account.getCreatedDate(), start, end,account.getCreatedDate(), currentTime,
                account.getBalance(), deposit, withdraw
        ));
        System.out.println(extractText);

        return extractText;
    }
    public byte[] createStatement(StringBuilder statement){
        return PdfUtil.toPdf(statement.toString());
    }

    private BigDecimal getOperation(Transaction transaction, Long id, BigDecimal withdraw, BigDecimal deposit) {
        if (transaction.getSenderAccount().getId() == id) {
            withdraw = withdraw.subtract(transaction.getAmount());
            return withdraw;
        } else {
            deposit = deposit.add(transaction.getAmount());
            return deposit;
        }
    }

}
