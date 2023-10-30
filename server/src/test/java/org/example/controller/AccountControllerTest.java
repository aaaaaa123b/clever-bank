package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.BalanceRequestDto;
import org.example.dto.DepositRequestDto;
import org.example.dto.TransferRequestDto;
import org.example.enumeration.TransactionType;
import org.example.model.Account;
import org.example.model.Transaction;
import org.example.service.AccountService;
import org.example.service.CheckService;
import org.example.service.TransactionService;
import org.example.service.impl.CheckServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Function;

import static org.mockito.Mockito.*;

public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private TransactionService transactionService;

    @Mock
    private CheckService checkService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDoBalance() throws Exception {

        BalanceRequestDto balanceRequestDto = new BalanceRequestDto();
        balanceRequestDto.setAccountId(1L);

        Function<Long, Account> createAccount = (id) ->
        {
            Account account = new Account();
            account.setId(1L);
            account.setBalance(new BigDecimal("150.00"));
            account.setCurrency("EUR");
            account.setNumber("9876543210");
            account.setUserId(1);
            account.setBankId(1);
            account.setCreatedDate(Date.valueOf("2023-10-29"));
            return account;
        };

        StringWriter responseWriter = new StringWriter();
        when(request.getRequestURI()).thenReturn(AccountController.BALANCE);

        String inputJsonData = "{\"accountId\": 1}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(inputJsonData)));

        when(objectMapper.readValue(inputJsonData, BalanceRequestDto.class)).thenReturn(balanceRequestDto);
        when(accountService.findById(1)).thenReturn(createAccount.apply(1L));


        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        accountController.doPost(request, response);

        String expectedResponseBody = """
                {
                  "id" : 1,
                  "userId" : 1,
                  "bankId" : 1,
                  "balance" : 1.5E+2,
                  "currency" : "EUR",
                  "number" : "9876543210",
                  "createdDate" : 1698526800000
                }""";

        Assertions.assertEquals(expectedResponseBody, responseWriter.toString().replaceAll("\r", ""));
    }

    @Test
    public void testDoDeposite() throws Exception {
        DepositRequestDto depositRequestDto = new DepositRequestDto();
        depositRequestDto.setAccountId(1L);
        depositRequestDto.setCash(new BigDecimal("100.00"));

        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("150.00"));
        account.setCurrency("EUR");
        account.setNumber("9876543210");
        account.setUserId(1);
        account.setBankId(1);
        account.setCreatedDate(Date.valueOf("2023-10-29"));

        Account accountWithAddBalance = new Account();
        accountWithAddBalance.setId(1L);
        accountWithAddBalance.setBalance(new BigDecimal("250.00"));
        accountWithAddBalance.setCurrency("EUR");
        accountWithAddBalance.setNumber("9876543210");
        accountWithAddBalance.setUserId(1);
        accountWithAddBalance.setBankId(1);
        accountWithAddBalance.setCreatedDate(Date.valueOf("2023-10-29"));

        Transaction transaction = new Transaction();
        transaction.setSenderAccount(account);
        transaction.setAmount(depositRequestDto.getCash());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setTime(Time.valueOf(LocalTime.now()));
        transaction.setDate(Date.valueOf(LocalDate.now()));
        when(request.getRequestURI()).thenReturn(AccountController.DEPOSIT);

        String inputJsonData = "{\"accountId\": 1, \"cash\": 100.00}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(inputJsonData)));

        when(objectMapper.readValue(inputJsonData, DepositRequestDto.class)).thenReturn(depositRequestDto);

        when(accountService.findById(1)).thenReturn(account);

        when(accountService.addCash(account, BigDecimal.valueOf(100.00).setScale(2, RoundingMode.CEILING))).thenReturn(accountWithAddBalance);

        when(transactionService.create(transaction)).thenReturn(transaction);

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        accountController.doPost(request, response);

        String expectedResponseBody = """
            {
              "id" : 1,
              "userId" : 1,
              "bankId" : 1,
              "balance" : 2.5E+2,
              "currency" : "EUR",
              "number" : "9876543210",
              "createdDate" : 1698526800000
            }""";

        Assertions.assertEquals(expectedResponseBody, responseWriter.toString().replaceAll("\r", ""));
    }

    @Test
    public void testDoWithdraw() throws Exception {
        DepositRequestDto depositRequestDto = new DepositRequestDto();
        depositRequestDto.setAccountId(1L);
        depositRequestDto.setCash(new BigDecimal("100.00"));

        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("150.00"));
        account.setCurrency("EUR");
        account.setNumber("9876543210");
        account.setUserId(1);
        account.setBankId(1);
        account.setCreatedDate(Date.valueOf("2023-10-29"));

        Account accountWithAddBalance = new Account();
        accountWithAddBalance.setId(1L);
        accountWithAddBalance.setBalance(new BigDecimal("50.00"));
        accountWithAddBalance.setCurrency("EUR");
        accountWithAddBalance.setNumber("9876543210");
        accountWithAddBalance.setUserId(1);
        accountWithAddBalance.setBankId(1);
        accountWithAddBalance.setCreatedDate(Date.valueOf("2023-10-29"));

        Transaction transaction = new Transaction();
        transaction.setSenderAccount(account);
        transaction.setAmount(depositRequestDto.getCash());
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setTime(Time.valueOf(LocalTime.now()));
        transaction.setDate(Date.valueOf(LocalDate.now()));
        when(request.getRequestURI()).thenReturn(AccountController.WITHDRAW);

        String inputJsonData = "{\"accountId\": 1, \"cash\": 100.00}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(inputJsonData)));

        when(objectMapper.readValue(inputJsonData, DepositRequestDto.class)).thenReturn(depositRequestDto);

        when(accountService.findById(1)).thenReturn(account);

        when(accountService.withdrawCash(account, BigDecimal.valueOf(100.00).setScale(2, RoundingMode.CEILING))).thenReturn(accountWithAddBalance);

        when(transactionService.create(transaction)).thenReturn(transaction);

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        accountController.doPost(request, response);

        String expectedResponseBody = """
            {
              "id" : 1,
              "userId" : 1,
              "bankId" : 1,
              "balance" : 5E+1,
              "currency" : "EUR",
              "number" : "9876543210",
              "createdDate" : 1698526800000
            }""";

        Assertions.assertEquals(expectedResponseBody, responseWriter.toString().replaceAll("\r", ""));
    }

    @Test
    public void testDoTransfer() throws Exception {
        TransferRequestDto transferRequestDto = new TransferRequestDto();
        transferRequestDto.setNumber("9876543210");
        transferRequestDto.setNumberRecipient("1076543210");
        transferRequestDto.setCash(new BigDecimal("50.00"));

        when(request.getRequestURI()).thenReturn(AccountController.TRANSFER);
        String inputJsonData = "{\"number\": \"9876543210\", \"numberRecipient\": \"1076543210\", \"cash\": 50.00}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(inputJsonData)));

        when(objectMapper.readValue(inputJsonData, TransferRequestDto.class)).thenReturn(transferRequestDto);

        Account senderAccount = new Account();
        senderAccount.setId(1L);
        senderAccount.setBalance(new BigDecimal("100.00"));
        senderAccount.setCurrency("EUR");
        senderAccount.setNumber("9876543210");
        senderAccount.setUserId(1);
        senderAccount.setBankId(1);
        senderAccount.setCreatedDate(Date.valueOf("2023-10-29"));
        when(accountService.findByNumber("9876543210")).thenReturn(senderAccount);

        Account recipientAccount = new Account();
        recipientAccount.setId(2L);
        recipientAccount.setBalance(new BigDecimal("150.00"));
        recipientAccount.setCurrency("EUR");
        recipientAccount.setNumber("1076543210");
        recipientAccount.setUserId(1);
        recipientAccount.setBankId(1);
        recipientAccount.setCreatedDate(Date.valueOf("2023-10-29"));
        when(accountService.findByNumber("1076543210")).thenReturn(recipientAccount);

        Transaction transaction = new Transaction();
        transaction.setSenderAccount(senderAccount);
        transaction.setRecipientAccount(recipientAccount);
        transaction.setAmount(transferRequestDto.getCash());
        transaction.setType(TransactionType.TRANSFER);
        transaction.setTime(Time.valueOf(LocalTime.now()));
        transaction.setDate(Date.valueOf(LocalDate.now()));
        when(transactionService.create(Mockito.any())).thenReturn(transaction);
        when(checkService.createCheck(Mockito.any())).thenReturn("check");

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        accountController.doPost(request, response);

        String expectedResponseBody = "check";
        Assertions.assertEquals(expectedResponseBody, responseWriter.toString().replaceAll("\r", ""));

    }

}
















