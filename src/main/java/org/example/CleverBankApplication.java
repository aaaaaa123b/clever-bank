package org.example;

import org.example.context.ApplicationContext;
import org.example.enumeration.ApplicationState;
import org.example.enumeration.Login;
import org.example.enumeration.Operation;
import org.example.enumeration.TransactionType;
import org.example.exception.NotEnoughMoneyException;
import org.example.model.Account;
import org.example.model.Transaction;
import org.example.model.User;
import org.example.service.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

@Deprecated
public class CleverBankApplication {

    final ApplicationContext context = new ApplicationContext();
    final Scanner scanner = new Scanner(System.in);
    final UserService userService;
    final AccountService accountService;

    final TransactionService transactionService;

    final CheckService checkService;

    final AccountStatementService accountStatementService;

    public CleverBankApplication(UserService userService, AccountService accountService, TransactionService transactionService, CheckService checkService, AccountStatementService accountStatementService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.checkService = checkService;
        this.accountStatementService = accountStatementService;
    }

    public void start() {
        while (!exitRequired()) {
            try {
                switch (context.getState()) {
                    case USER_AUTHENTICATION -> authenticateUser();
                    case OPERATION_SELECTION -> selectOperation();
                    case OPERATION_EXECUTION -> executeOperation();
                    default -> throw new RuntimeException();
                }
            } catch (RuntimeException e) {
                System.out.println(e.getClass() + ": " + e.getMessage());
                context.setState(ApplicationState.EXIT_REQUIRED);
                throw e;
            }
        }
    }

    private void authenticateUser() {

        System.out.println("Выберите операцию:");
        System.out.println("1. Войти");
        System.out.println("2. Зарегистрироваться");

        final Login enter = Login.byOrdinal(scanner.nextInt());
        
        switch (enter) {
            case ENTRANCE -> {
                System.out.println("Введите id пользователя:");
                final long id = scanner.nextLong();

                final User user = userService.findById(id);

                context.setCurrentUser(user);
                context.setState(ApplicationState.OPERATION_SELECTION);

                final String message = "Вы авторизованы как пользователь %s %s %s".formatted(
                        user.getLastName(),
                        user.getFirstName(),
                        user.getPatronymic()
                );

                System.out.println(message);
            }

       case REGISTRATION ->{

            scanner.nextLine();
            System.out.println("Введите Ваше имя:");
            final String firstName = scanner.nextLine();

            System.out.println("Введите Вашу фамилию:");
            final String lastName = scanner.nextLine();

            System.out.println("Введите Ваше отчество:");
            final String patronymic = scanner.nextLine();

            System.out.println("Введите Ваш логин:");
            final String login = scanner.nextLine();

            userService.addUser(firstName,lastName,patronymic,login);
        }
            default -> System.out.println("Операция " + context.getCurrentOperation() + " не найдена!");
        }

    }

    private void selectOperation() {
        System.out.println("Выберите операцию:");
        System.out.println("1. Проверить остаток на счёте");
        System.out.println("2. Пополнить счёт");
        System.out.println("3. Снять средства");
        System.out.println("4. Выполнить перевод");
        System.out.println("5. Получить выписку");

        final Operation operation = Operation.byOrdinal(scanner.nextInt());
        context.setCurrentOperation(operation);
        context.setState(ApplicationState.OPERATION_EXECUTION);
    }

    private void executeOperation() {
        switch (context.getCurrentOperation()) {
            case BALANCE -> {
                System.out.println("Введите id счёта: ");
                final long id = scanner.nextLong();

                final Account account = accountService.findById(id);

                System.out.println("Счёт вашего аккаунта: " + account.getBalance() + " " + account.getCurrency());
            }
            case WITHDRAW -> {
                System.out.println("Введите id счёта: ");
                final long id = scanner.nextLong();

                System.out.println("Введите сумму,которую хотите снять: ");
                final BigDecimal cash = scanner.nextBigDecimal();

                Account account = accountService.findById(id);

                account = accountService.withdrawCash(account, cash);

                Transaction transaction = new Transaction();
                transaction.setAmount(cash);
                transaction.setType(TransactionType.WITHDRAW);
                transaction.setTime(Time.valueOf(LocalTime.now()));
                transaction.setDate(Date.valueOf(LocalDate.now()));

                transaction.setRecipientAccount(account);
                transactionService.create(transaction);

                System.out.println("Счёт вашего аккаунта: " + account.getBalance() + " " + account.getCurrency());
            }
            case DEPOSIT -> {
                System.out.println("Введите id счёта: ");
                final long id = scanner.nextLong();

                System.out.println("Введите сумму,которую хотите положить на счёт: ");
                final BigDecimal cash = scanner.nextBigDecimal();

                Account account = accountService.findById(id);

                account = accountService.addCash(account, cash);

                Transaction transaction = new Transaction();
                transaction.setSenderAccount(account);
                transaction.setAmount(cash);
                transaction.setType(TransactionType.DEPOSIT);
                transaction.setTime(Time.valueOf(LocalTime.now()));
                transaction.setDate(Date.valueOf(LocalDate.now()));
                transactionService.create(transaction);

                System.out.println("Счёт вашего аккаунта: " + account.getBalance() + " " + account.getCurrency());
            }
            case TRANSFER -> {

                Transaction transaction=new Transaction();

                scanner.nextLine();

                System.out.println("Введите номер счёта: ");
                final String number = scanner.nextLine();

                System.out.println("Введите номер счёта получателя: ");
                final String numberRecipient = scanner.nextLine();

                System.out.println("Введите сумму,которую хотите положить на счёт получателя: ");
                final BigDecimal cash = scanner.nextBigDecimal();

                Account account = accountService.findByNumber(number);

                BigDecimal accountCash;
                accountCash=account.getBalance();

                if(accountCash.compareTo(cash)<0){
                    throw new NotEnoughMoneyException();
                }

                transaction.setAmount(cash);

                Account accountRecipient = accountService.findByNumber(numberRecipient);

                transaction.setSenderAccount(account);
                transaction.setRecipientAccount(accountRecipient);

                accountService.transfer(account, accountRecipient, cash);

                LocalTime currentTime = LocalTime.now();
                LocalDate currentDate = LocalDate.now();

                transaction.setType(TransactionType.TRANSFER);
                transaction.setTime(Time.valueOf(currentTime));
                transaction.setDate(Date.valueOf(currentDate));

                System.out.println("Счёт вашего аккаунта: " + account.getBalance() + " " + account.getCurrency());
                transactionService.create(transaction);

                checkService.createCheck(transaction);

            }
            case EXTRACT ->{

                scanner.nextLine();
                System.out.println("Введите номер счёта: ");
                final String number = scanner.nextLine();
                Account account = accountService.findByNumber(number);

                System.out.println("Введите дату начала в формате dd.MM.yyyy:");
                String start = scanner.nextLine();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate startDate = LocalDate.parse(start, formatter);

                System.out.println("Введите дату конца в формате dd.MM.yyyy:");
                String end = scanner.nextLine();
                LocalDate endDate = LocalDate.parse(end, formatter);

                System.out.println(startDate);
                ArrayList<Integer> transactionIds;

                transactionIds=checkService.findTransactions(startDate,endDate,account);

                System.out.println(transactionIds);
                accountStatementService.createExtract(account,transactionIds,startDate,endDate);

            }

            default -> System.out.println("Операция " + context.getCurrentOperation() + " не найдена!");
        }

        context.setState(ApplicationState.OPERATION_SELECTION);
    }

    private boolean exitRequired() {
        return context.getState() == ApplicationState.EXIT_REQUIRED;
    }
}
