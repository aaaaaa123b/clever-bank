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

    /**
     * Finds a bank by its ID in the database.
     *
     * @param id the bank ID
     * @return the bank object from the database.
     */
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
                bank.setId(rs.getInt("id"));
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

    /**
     * Delete bank from the database.
     *
     * @param id bank ID
     */
    @Override
    public void deleteById(Long id) {
        Connection connection = connectionManager.getConnection();
        final String query = "DELETE FROM banks WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении SQL-запроса", e);
        }
    }

    /**
     * Creates a bank in the database.
     *
     * @param bank the bank object
     * @return the bank object with the generated ID.
     */
    @Override
    public Bank create(Bank bank) {
        Connection connection = connectionManager.getConnection();
        final String query = "INSERT INTO banks (name) VALUES (?) RETURNING id";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, bank.getName());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    System.out.println("Generated ID: " + id);
                    bank.setId((int) id);
                    return bank;
                } else {
                    System.out.println("Не удалось добавить пользователя.");
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении SQL-запроса", e);
        }
    }

    /**
     * Update a bank in the database.
     *
     * @param id the bank ID
     * @param bank new bank object
     * @return the bank object.
     */
    @Override
    public Bank update(int id, Bank bank) {
        Connection connection = connectionManager.getConnection();

        String query = "UPDATE banks SET name = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, bank.getName());
            preparedStatement.setInt(2, id);

            int result = preparedStatement.executeUpdate();

            if (result == 0) {
                String message = "Пользователь с id %d не найден.".formatted(id);
                throw new EntityNotFoundException(message);
            }

            bank.setId(id);
            return bank;

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обработке SQL-запроса", e);
        }
    }
}
