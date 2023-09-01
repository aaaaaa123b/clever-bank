package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.DependencyProvider;
import org.example.dto.*;
import org.example.enumeration.TransactionType;
import org.example.exception.NotEnoughMoneyException;
import org.example.model.Account;
import org.example.model.Transaction;
import org.example.service.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class AccountController extends HttpServlet {

    public static final String API_PREFIX = ".*/api/v1";
    public static final String ACCOUNTS_PREFIX = API_PREFIX + "/users/accounts";

    public static final String BALANCE = ACCOUNTS_PREFIX + "/balance/";
    public static final String DEPOSIT = ACCOUNTS_PREFIX + "/deposit/";
    public static final String WITHDRAW = ACCOUNTS_PREFIX + "/withdraw/";
    public static final String TRANSFER = ACCOUNTS_PREFIX + "/transfer/";
    public static final String EXTRACT = ACCOUNTS_PREFIX + "/extract/";


    private ObjectMapper objectMapper;
    private AccountService accountService;
    private TransactionService transactionService;
    private CheckService checkService;
    private AccountStatementService accountStatementService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        objectMapper = (ObjectMapper) DependencyProvider.get().forClass(ObjectMapper.class);
        accountService = (AccountService) DependencyProvider.get().forClass(AccountService.class);
        transactionService = (TransactionService) DependencyProvider.get().forClass(TransactionService.class);
        checkService = (CheckService) DependencyProvider.get().forClass(CheckService.class);
        accountStatementService = (AccountStatementService) DependencyProvider.get().forClass(AccountStatementService.class);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String path = request.getRequestURI();
        final String message = request.getReader().lines()
                .reduce("", (accumulator, actual) -> accumulator + actual);

        if (path.matches(BALANCE)) {
            doBalance(message, response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String path = request.getRequestURI();
        final String message = request.getReader().lines()
                .reduce("", (accumulator, actual) -> accumulator + actual);

        if (path.matches(DEPOSIT)) {
            doDeposite(message, response);
            return;
        }

        if (path.matches(WITHDRAW)) {
            doWithdraw(message, response);
            return;
        }

        if (path.matches(TRANSFER)) {
            doTransfer(message, response);
            return;
        }
        if (path.matches(EXTRACT)) {
            doExtract(message, response);
        }
    }


    private void doBalance(String message, HttpServletResponse response) {
        final BalanceRequestDto dto;
        try {
            dto = objectMapper.readValue(message, BalanceRequestDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        final Account account = accountService.findById(dto.getAccountId());

        final String body = """
                {
                    "balance": "%s",
                    "id": "%s",
                    "number": "%s",
                    
                }
                """.formatted(
                account.getBalance(),
                account.getId(),
                account.getNumber()
        );
        send(body, response);
    }

    private void doDeposite(String message, HttpServletResponse response) {
        final DepositRequestDto dto;
        try {
            dto = objectMapper.readValue(message, DepositRequestDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Account account = accountService.findById(dto.getAccountId());
        account = accountService.addCash(account, dto.getCash());

        Transaction transaction = new Transaction();
        transaction.setSenderAccount(account);
        transaction.setAmount(dto.getCash());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setTime(Time.valueOf(LocalTime.now()));
        transaction.setDate(Date.valueOf(LocalDate.now()));

        transactionService.create(transaction);

        final String body = """
                {
                    "balance": "%s",
                    "id": "%s",
                    "number": "%s",
                    
                }
                """.formatted(
                account.getBalance(),
                account.getId(),
                account.getNumber()
        );
        send(body, response);
    }

    private void doWithdraw(String message, HttpServletResponse response) {
        final WithdrawRequestDto dto;
        try {
            dto = objectMapper.readValue(message, WithdrawRequestDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Account account = accountService.findById(dto.getAccountId());

        account = accountService.withdrawCash(account, dto.getCash());

        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getCash());
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setTime(Time.valueOf(LocalTime.now()));
        transaction.setDate(Date.valueOf(LocalDate.now()));

        transaction.setRecipientAccount(account);
        transactionService.create(transaction);

        final String body = """
                {
                    "balance": "%s",
                    "id": "%s",
                    "number": "%s",
                    
                }
                """.formatted(
                account.getBalance(),
                account.getId(),
                account.getNumber()
        );
        send(body, response);
    }


    private void doTransfer(String message, HttpServletResponse response) {
        final TransferRequestDto dto;
        try {
            dto = objectMapper.readValue(message, TransferRequestDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Transaction transaction = new Transaction();

        Account account = accountService.findByNumber(dto.getNumber());

        BigDecimal accountCash;
        accountCash = account.getBalance();

        if (accountCash.compareTo(dto.getCash()) < 0) {
            throw new NotEnoughMoneyException();
        }

        transaction.setAmount(dto.getCash());

        Account accountRecipient = accountService.findByNumber(dto.getNumberRecipient());
        transaction.setSenderAccount(account);
        transaction.setRecipientAccount(accountRecipient);

        accountService.transfer(account, accountRecipient, dto.getCash());

        LocalTime currentTime = LocalTime.now();
        LocalDate currentDate = LocalDate.now();
        transaction.setType(TransactionType.TRANSFER);
        transaction.setTime(Time.valueOf(currentTime));
        transaction.setDate(Date.valueOf(currentDate));

        transactionService.create(transaction);

        checkService.createCheck(transaction);

        final String body = """
                {
                   
                   
                }
                """.formatted(
        );
        send(body, response);
    }

    private void doExtract(String message, HttpServletResponse response) {
        final ExctractRequestDto dto;
        try {
            dto = objectMapper.readValue(message, ExctractRequestDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Account account = accountService.findByNumber(dto.getNumber());

        ArrayList<Integer> transactionIds;

        transactionIds = checkService.findTransactions(dto.getStartDate(), dto.getEndDate(), account);

        System.out.println(transactionIds);
        accountStatementService.createExtract(account, transactionIds, dto.getStartDate(), dto.getEndDate());

        final String body = """
                {
                   
                   
                }
                """.formatted(
        );
        send(body, response);
    }


    private void send(String message, HttpServletResponse response) {
        final PrintWriter out;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        out.print(message);
        out.flush();
    }


}

