package org.example.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Account;
import org.example.repository.AccountRepository;

import org.example.util.ConnectionManager;
import org.example.config.InterestConfig;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InterestCalculationServiceImpl {

    private final AccountRepository accountRepository;
    private final ConnectionManager connectionManager;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final LocalDate currentDate = LocalDate.now();
    private LocalDate firstDayOfNextMonth = currentDate.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());


    public InterestCalculationServiceImpl(AccountRepository accountRepository, ConnectionManager connectionManager) {
        this.accountRepository = accountRepository;
        this.connectionManager = connectionManager;
    }

    public void scheduleInterestCalculation() {
        scheduler.scheduleAtFixedRate(this::calculateInterest, 5, 5, TimeUnit.SECONDS);
    }

    public void calculateInterest() {
        int interest = configuration();

        if (currentDate.isAfter(firstDayOfNextMonth) || currentDate.isEqual(firstDayOfNextMonth)) {

            firstDayOfNextMonth = firstDayOfNextMonth.plusMonths(1);
            String query = "SELECT balance,id FROM accounts";
            Connection connection = connectionManager.getConnection();
            try {
                connection.setAutoCommit(false);

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    Account account = accountRepository.findById(id);

                    BigDecimal currentBalance = resultSet.getBigDecimal("balance");
                    BigDecimal recievedBalance = currentBalance.add(BigDecimal.valueOf(interest).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP).multiply(currentBalance));

                    System.out.println("Value: " + recievedBalance);
                    account.setBalance(recievedBalance);
                    accountRepository.update(connection, account);
                    connection.commit();
                }
            } catch (SQLException e) {
                try {

                    System.out.println("Transaction is being rolled back.");
                    try {
                        connection.rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
    }

    public int configuration() {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        File yamlFile = new File(Objects.requireNonNull(classLoader.getResource("interest-config.yaml")).getFile());
        InterestConfig interestConfig;
        try {
            interestConfig = mapper.readValue(yamlFile, InterestConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return interestConfig.getSize();
    }
}
