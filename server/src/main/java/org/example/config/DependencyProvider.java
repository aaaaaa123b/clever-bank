package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.repository.*;
import org.example.repository.impl.*;
import org.example.service.*;
import org.example.service.impl.*;
import org.example.util.ConnectionManager;

import java.util.HashMap;

public class DependencyProvider {

    private static DependencyProvider instance = null;
    private static final HashMap<Class<?>, Object> DEPENDENCIES = new HashMap<>();

    private DependencyProvider() {
        final ConnectionManager connectionManager = new ConnectionManager();
        DEPENDENCIES.put(ConnectionManager.class, connectionManager);

        final UserRepository userRepository = new UserPostgreRepository(connectionManager);
        DEPENDENCIES.put(UserRepository.class, userRepository);

        final AccountRepository accountRepository = new AccountPostgreRepository(connectionManager);
        DEPENDENCIES.put(AccountRepository.class, accountRepository);

        final UserService userService = new UserServiceImpl(userRepository);
        DEPENDENCIES.put(UserService.class, userService);

        final TransactionRepository transactionRepository = new TransactionPostgreRepository(connectionManager, accountRepository);
        DEPENDENCIES.put(TransactionRepository.class, transactionRepository);

        final AccountService accountService = new AccountServiceImpl(accountRepository, connectionManager);
        DEPENDENCIES.put(AccountService.class, accountService);

        final InterestCalculationServiceImpl interestCalculationService = new InterestCalculationServiceImpl(accountRepository, connectionManager);
        DEPENDENCIES.put(InterestCalculationServiceImpl.class, interestCalculationService);

        interestCalculationService.scheduleInterestCalculation();

        final CheckRepository checkRepository = new CheckPostgreRepository(connectionManager);
        DEPENDENCIES.put(CheckPostgreRepository.class, checkRepository);

        final BankRepository bankRepository = new BankPostgreRepository(connectionManager);
        DEPENDENCIES.put(BankRepository.class, bankRepository);

        final CheckServiceImpl checkService = new CheckServiceImpl(bankRepository, checkRepository);
        DEPENDENCIES.put(CheckService.class, checkService);

        final TransactionService transactionService = new TransactionServiceImpl(transactionRepository);
        DEPENDENCIES.put(TransactionService.class, transactionService);

        final AccountStatementService accountStatementService = new AccountStatementServiceImpl(bankRepository, userService, transactionRepository);
        DEPENDENCIES.put(AccountStatementService.class, accountStatementService);

        final MoneyStatementService moneyStatementService = new MoneyStatementServiceServiceImpl(bankRepository,userService,transactionRepository);
        DEPENDENCIES.put(MoneyStatementService.class, moneyStatementService);

        final ObjectMapper objectMapper = new ObjectMapper();
        DEPENDENCIES.put(ObjectMapper.class, objectMapper);
    }

    public static DependencyProvider get() {
        if (instance == null) {
            instance = new DependencyProvider();
        }

        return instance;
    }

    public Object forClass(Class<?> clazz) {
        return DEPENDENCIES.get(clazz);
    }
}
