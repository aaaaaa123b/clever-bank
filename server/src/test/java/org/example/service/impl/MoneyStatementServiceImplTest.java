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

class MoneyStatementServiceImplTest {

    @InjectMocks
    private MoneyStatementServiceServiceImpl moneyStatementService;

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
    void shouldSuccessCreateStatement() {
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

        LocalDate startDate = LocalDate.parse("2023-10-29");
        LocalDate endDate = LocalDate.parse("2023-10-29");

        Transaction transaction = mock(Transaction.class);
        when(transaction.getSenderAccount()).thenReturn(account);
        when(transaction.getDate()).thenReturn(Date.valueOf("2023-10-29"));
        when(transaction.getType()).thenReturn(TransactionType.DEPOSIT);
        when(transaction.getAmount()).thenReturn(BigDecimal.valueOf(100));

        when(bankRepository.findById(1)).thenReturn(senderBank);
        when(userService.findById(1L)).thenReturn(user);
        when(transactionRepository.findById(1L)).thenReturn(transaction);

        LocalTime currentTime = LocalTime.ofSecondOfDay(LocalTime.now().getHour());

        String statement = String.valueOf(moneyStatementService.createStringStatement(account, ids, startDate, endDate));

        String expectedText = """
                                            Money statement
                                                 SenderBank
                Клиент                           | A B C
                Счет                             | 123456
                Валюта                           | USD
                Дата открытия                    | 2023-06-29
                Период                           | 2023-10-29 - 2023-10-29
                Дата и время формирования        | 2023-06-29 %s
                Остаток                          | 1000.00
                            Приход           | Уход
                          -----------------------------------
                              100                0
                """.formatted(currentTime);


        Assertions.assertEquals(expectedText, statement);

        Assertions.assertEquals(moneyStatementService.createStatement(new StringBuilder(expectedText)).length, moneyStatementService.createStatement(new StringBuilder(statement)).length);
    }
}


