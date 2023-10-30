package org.example.service.impl;

import org.example.enumeration.TransactionType;
import org.example.model.Account;
import org.example.model.Bank;
import org.example.model.Transaction;
import org.example.model.User;
import org.example.repository.BankRepository;
import org.example.repository.TransactionRepository;
import org.example.service.UserService;
import org.example.util.PdfUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountStatementServiceImplTest {

    @InjectMocks
    private AccountStatementServiceImpl accountStatementService;

    @Mock
    private BankRepository bankRepository;

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSuccessCreateCheck() {
        Bank senderBank = mock(Bank.class);
        when(senderBank.getName()).thenReturn("SenderBank");

        User user = mock(User.class);
        when(user.getFirstName()).thenReturn("A");
        when(user.getLastName()).thenReturn("B");
        when(user.getPatronymic()).thenReturn("C");

        Account account = mock(Account.class);
        when(account.getBankId()).thenReturn(1);
        when(account.getUserId()).thenReturn(1);
        when(account.getNumber()).thenReturn("123456");
        when(account.getCurrency()).thenReturn("USD");
        when(account.getCreatedDate()).thenReturn(Date.valueOf("2023-06-29"));
        when(account.getBalance()).thenReturn(new BigDecimal("1000.00"));

        ArrayList<Long> ids = new ArrayList<>();
        ids.add(1L);

        LocalDate startDate = LocalDate.parse(("2023-10-29"));
        LocalDate endDate = LocalDate.parse(("2023-10-29"));

        Transaction transaction = mock(Transaction.class);
        when(transaction.getSenderAccount()).thenReturn(account);
        when(transaction.getDate()).thenReturn(Date.valueOf("2023-10-29"));
        when(transaction.getType()).thenReturn(TransactionType.DEPOSIT);


        when(bankRepository.findById(1)).thenReturn(senderBank);
        when(userService.findById(1L)).thenReturn(user);
        when(transactionRepository.findById(1)).thenReturn(transaction);

        LocalTime currentTime = LocalTime.ofSecondOfDay(LocalTime.now().getHour());
        LocalDate currentDate = LocalDate.now();

        String extractText = ("""
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

        final String label = "Пополнение средств";

        var transactionText = """
                %6s|             %-40s             %-10s """.formatted(
                transaction.getDate(), label, transaction.getAmount()
        );
        extractText += "\n" + transactionText;


        StringBuilder statementText = accountStatementService.createStringExtract(account, ids, startDate, endDate);
        final String statementString = statementText.toString();

        Assertions.assertEquals(extractText, statementString);
        Assertions.assertEquals(accountStatementService.createExtract(new StringBuilder(extractText)).length, accountStatementService.createExtract(new StringBuilder(statementString)).length);
        Assertions.assertEquals(PdfUtil.toPdf(extractText).length, PdfUtil.toPdf(statementString).length);

    }

}
