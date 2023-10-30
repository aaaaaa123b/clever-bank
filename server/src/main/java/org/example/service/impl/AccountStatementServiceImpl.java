package org.example.service.impl;

import org.example.model.Account;
import org.example.model.Bank;
import org.example.model.Transaction;
import org.example.model.User;
import org.example.repository.BankRepository;
import org.example.repository.TransactionRepository;
import org.example.service.AccountStatementService;
import org.example.service.UserService;
import org.example.util.PdfUtil;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;


public class AccountStatementServiceImpl implements AccountStatementService {

    private final BankRepository bankRepository;

    private final UserService userService;

    private final TransactionRepository transactionRepository;




    public AccountStatementServiceImpl(BankRepository bankRepository, UserService userService, TransactionRepository transactionRepository) {
        this.bankRepository = bankRepository;
        this.userService = userService;
        this.transactionRepository = transactionRepository;

    }

    /**
     * Creates an account statement.
     *
     * @param account   the account
     * @param ids       a list of transaction IDs associated with the account
     * @param startDate the start date for the statement
     * @param endDate   the end date for the statement
     */
    @Override
    public StringBuilder createStringExtract(Account account, ArrayList<Long> ids, LocalDate startDate, LocalDate endDate) {
        int senderBankId = account.getBankId();
        Bank senderBank = bankRepository.findById(senderBankId);

        long userId = account.getUserId();
        User user = userService.findById(userId);

        if(startDate==null){
            Instant instant = account.getCreatedDate().toInstant();
            startDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        }

        LocalTime currentTime = LocalTime.ofSecondOfDay(LocalTime.now().getHour());
        LocalDate currentDate = LocalDate.now();



        StringBuilder extractText = new StringBuilder("""
                                                      Выписка
                                                         %s
                    Клиент                           | %s %s %s
                    Счет                             | %s
                    Валюта                           | %s
                    Дата открытия                    | %s
                    Период                           | %s - %s
                    Дата и время формирования        | %s %s
                    Остаток                          | %s
                       Дата   |    Примечание                                |   Сумма
                ----------------------------------------------------------------------------------
                    """.formatted(

                senderBank.getName(), user.getFirstName(),
                user.getLastName(), user.getPatronymic(),
                account.getNumber(), account.getCurrency(),
                account.getCreatedDate(),
                startDate, endDate, currentDate, currentTime,
                account.getBalance()

        ));
        System.out.println(extractText);

        for (Long id : ids) {
            Transaction transaction = transactionRepository.findById(id);

            final String label = switch (transaction.getType()) {
                case DEPOSIT -> "Пополнение средств";
                case WITHDRAW -> "Снятие средств";
                case TRANSFER -> getTransferMessage(transaction, account.getId());
            };

            var transactionText = """
                    %6s|             %-40s             %-10s """.formatted(
                    transaction.getDate(), label, transaction.getAmount()
            );
            System.out.println(transactionText);
            extractText.append("\n");
            extractText.append(transactionText);
        }

        return extractText;
    }

    public byte[] createExtract(StringBuilder extract){
        return PdfUtil.toPdf(extract.toString());
    }

    private String getTransferMessage(Transaction transaction, Long id) {
        if (transaction.getSenderAccount().getId() == id) {
            final Account recipientAccount = transaction.getRecipientAccount();
            final long recipientUserId = recipientAccount.getUserId();
            final User recipientUser = userService.findById(recipientUserId);
            return "Перевод к: " + recipientUser.getLastName();
        }

        final Account senderAccount = transaction.getSenderAccount();
        final long senderUserId = senderAccount.getUserId();
        final User senderUser = userService.findById(senderUserId);
        return "Перевод от: " + senderUser.getLastName();
    }



}
