package org.example.service.impl;

import org.example.enumeration.TransactionType;
import org.example.model.Account;
import org.example.model.Bank;
import org.example.model.Transaction;
import org.example.repository.BankRepository;
import org.example.repository.CheckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CheckServiceImplTest {
    @Mock
    private BankRepository bankRepository;

    @Mock
    private CheckRepository checkRepository;

    @InjectMocks
    private CheckServiceImpl checkService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCheck() {
        Bank senderBank = mock(Bank.class);
        when(senderBank.getName()).thenReturn("SenderBank");

        Bank recipientBank = mock(Bank.class);
        when(recipientBank.getName()).thenReturn("RecipientBank");

        Account senderAccount = mock(Account.class);
        when(senderAccount.getBankId()).thenReturn(1);
        when(senderAccount.getNumber()).thenReturn("123456");
        when(senderAccount.getCurrency()).thenReturn("BYN");

        Account recipientAccount = mock(Account.class);
        when(recipientAccount.getBankId()).thenReturn(2);
        when(recipientAccount.getNumber()).thenReturn("654321");

        Transaction transaction = mock(Transaction.class);
        when(transaction.getId()).thenReturn(1L);
        when(transaction.getDate()).thenReturn(Date.valueOf("2023-10-29"));
        when(transaction.getTime()).thenReturn(Time.valueOf("10:30:45"));
        when(transaction.getType()).thenReturn(TransactionType.valueOf("TRANSFER"));
        when(transaction.getAmount()).thenReturn(BigDecimal.valueOf(100));
        when(transaction.getSenderAccount()).thenReturn(senderAccount);
        when(transaction.getRecipientAccount()).thenReturn(recipientAccount);

        when(bankRepository.findById(1)).thenReturn(senderBank);
        when(bankRepository.findById(2)).thenReturn(recipientBank);

        String checkText = checkService.createCheck(transaction);


        String expectedCheckText = """
                -----------------------------------------
                |            Банковский чек             |
                | Чек:                                 1 |
                | 2023-10-29                   10:30:45 |
                | Тип транзакции:               TRANSFER |
                | Банк отправителя:           SenderBank |
                | Банк получателя:           RecipientBank |
                | Счёт отправителя:               123456 |
                | Счёт получателя:                654321 |
                | Сумма:                          100BYN |
                -----------------------------------------
                """;

        assertEquals(expectedCheckText, checkText);
    }

    @Test
    void testFindTransactions() {

        Account account = mock(Account.class);
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        ArrayList<Long> expectedTransactions = new ArrayList<>();
        expectedTransactions.add(1L);
        expectedTransactions.add(2L);
        expectedTransactions.add(3L);

        when(checkRepository.findTransactions(startDate, endDate, account)).thenReturn(expectedTransactions);

        ArrayList<Long> result = checkService.findTransactions(startDate, endDate, account);

        assertEquals(expectedTransactions, result);
        verify(checkRepository).findTransactions(startDate, endDate, account);
    }

    @Test
    void findAllTransactions() {

        Account account = mock(Account.class);

        ArrayList<Long> expectedTransactions = new ArrayList<>();
        expectedTransactions.add(1L);
        expectedTransactions.add(2L);
        expectedTransactions.add(3L);

        when(checkRepository.findAllTransactions(account)).thenReturn(expectedTransactions);

        ArrayList<Long> result = checkService.findAllTransactions(account);

        assertEquals(expectedTransactions, result);

        verify(checkRepository).findAllTransactions(account);
    }
}




