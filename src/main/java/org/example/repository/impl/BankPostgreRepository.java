package org.example.repository.impl;

import org.example.exception.EntityNotFoundException;
import org.example.model.Bank;
import org.example.model.User;
import org.example.repository.BankRepository;
import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankPostgreRepository implements BankRepository {

    private final ConnectionManager connectionManager;

    public BankPostgreRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }


    @Override
    public Bank findById(int id) {
        final Connection connection = connectionManager.getConnection();
        String query = "SELECT * FROM banks WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                final Bank bank = new Bank();
                bank.setId( rs.getInt("id"));
                bank.setName(rs.getString("name"));

                return bank;
            } else {
                String message = "Банк с id %d не найден.".formatted(id);
                throw new EntityNotFoundException(message);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обработке SQL-запроса", e);
        }
    }
}
