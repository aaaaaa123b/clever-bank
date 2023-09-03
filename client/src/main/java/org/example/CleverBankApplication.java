package org.example;

import org.example.context.ApplicationContext;
import org.example.enumeration.ApplicationState;
import org.example.enumeration.Login;
import org.example.enumeration.Operation;

import org.example.enumeration.TimeForExtract;
import org.example.model.Account;
import org.example.model.User;
import org.example.service.AccountService;
import org.example.service.AccountStatementService;
import org.example.service.MoneyStatementService;
import org.example.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.Scanner;

public class CleverBankApplication {

    public static final String SERVER_BASE = "http://localhost:8080/CleverBank_1_0_SNAPSHOT_war";

    final ApplicationContext context = new ApplicationContext();
    final Scanner scanner = new Scanner(System.in);
    final UserService userService;
    final AccountService accountService;
    final AccountStatementService accountStatementService;
    final MoneyStatementService moneyStatementService;

    public CleverBankApplication(UserService userService, AccountService accountService, AccountStatementService accountStatementService, MoneyStatementService moneyStatementService) {
        this.userService = userService;
        this.accountService = accountService;
        this.accountStatementService = accountStatementService;
        this.moneyStatementService = moneyStatementService;
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
                e.printStackTrace();
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

                final User user = userService.login(id);

                context.setCurrentUser(user);
                context.setState(ApplicationState.OPERATION_SELECTION);

                final String message = "Вы авторизованы как пользователь %s %s %s".formatted(
                        user.getLastName(),
                        user.getFirstName(),
                        user.getPatronymic()
                );

                System.out.println(message);
            }

            case REGISTRATION -> {

                scanner.nextLine();
                System.out.println("Введите Ваше имя:");
                final String firstName = scanner.nextLine();

                System.out.println("Введите Вашу фамилию:");
                final String lastName = scanner.nextLine();

                System.out.println("Введите Ваше отчество:");
                final String patronymic = scanner.nextLine();

                System.out.println("Введите Ваш логин:");
                final String login = scanner.nextLine();

                userService.addUser(firstName, lastName, patronymic, login);
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
        System.out.println("6. Получить Money statement");

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

                Account account = accountService.withdrawCash(id, cash);

                System.out.println("Счёт вашего аккаунта: " + account.getBalance() + " " + account.getCurrency());
            }
            case DEPOSIT -> {
                System.out.println("Введите id счёта: ");
                final long id = scanner.nextLong();

                System.out.println("Введите сумму,которую хотите положить на счёт: ");
                final BigDecimal cash = scanner.nextBigDecimal();

                Account account = accountService.addCash(id, cash);

                System.out.println("Счёт вашего аккаунта: " + account.getBalance() + " " + account.getCurrency());
            }
            case TRANSFER -> {
                scanner.nextLine();

                System.out.println("Введите номер счёта: ");
                final String number = scanner.nextLine();

                System.out.println("Введите номер счёта получателя: ");
                final String numberRecipient = scanner.nextLine();

                System.out.println("Введите сумму,которую хотите положить на счёт получателя: ");
                final BigDecimal cash = scanner.nextBigDecimal();

                accountService.transfer(number, numberRecipient, cash);


            }
            case EXTRACT -> {

                scanner.nextLine();
                System.out.println("Введите номер счёта: ");
                final String number = scanner.nextLine();

                System.out.println(" 1.За месяц:");
                System.out.println(" 2.За год:");
                System.out.println(" 3.За всё время:");
                 int date = scanner.nextInt();
                 LocalDate currentDate=LocalDate.now();

                LocalDate startDate = switch (TimeForExtract.byOrdinal(date)) {
                    case MONTH -> currentDate.minusMonths(1);
                    case YEAR -> currentDate.minusYears(1);
                    case ALL -> currentDate=null;
                };

                accountStatementService.createExtract(number, startDate, currentDate);

                System.out.println("Выписка сохранена");

            }

            case STATEMENT -> {
                scanner.nextLine();
                System.out.println("Введите номер счёта:");
                final String number = scanner.nextLine();

                moneyStatementService.createStatement(number);

                System.out.println("Money statment сохранена");

            }
            default -> System.out.println("Операция " + context.getCurrentOperation() + " не найдена!");
        }

        context.setState(ApplicationState.OPERATION_SELECTION);
    }

    private boolean exitRequired() {
        return context.getState() == ApplicationState.EXIT_REQUIRED;
    }
}
