package org.example.repository.impl;

import org.example.model.Account;
import org.example.repository.CheckRepository;
import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CheckPostgreRepository implements CheckRepository {

    private final ConnectionManager connectionManager;

    public CheckPostgreRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public ArrayList<Integer> findTransactions(LocalDate startDate, LocalDate endDate, Account account) {
        Connection connection = connectionManager.getConnection();
        ArrayList<Integer> intValues = new ArrayList<>();
        long accountId=account.getId();

        String query = "SELECT * FROM transactions WHERE (sender_account = ? OR recipient_account = ?) AND date BETWEEN ? AND ? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1,accountId);
            preparedStatement.setLong(2,accountId);
            preparedStatement.setObject(3, startDate);
            preparedStatement.setObject(4, endDate);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int intValue = resultSet.getInt("id");
                intValues.add(intValue);
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return intValues;
    }
    }

