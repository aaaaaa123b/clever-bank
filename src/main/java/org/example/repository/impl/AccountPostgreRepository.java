package org.example.repository.impl;

import org.example.exception.EntityNotFoundException;
import org.example.model.Account;
import org.example.model.User;
import org.example.repository.AccountRepository;
import org.example.util.ConnectionManager;

import java.sql.*;

public class AccountPostgreRepository implements AccountRepository {
    private final ConnectionManager connectionManager;

    public AccountPostgreRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Account findById(Long id) {
        final Connection connection = connectionManager.getConnection();
        String query = "SELECT * FROM accounts WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                final Account account = new Account();
                account.setId(rs.getLong("id"));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setCurrency(rs.getString("currency"));
                account.setNumber(rs.getString("number"));
                account.setUserId(rs.getInt("user_id"));
                account.setBankId(rs.getInt("bank_id"));
                account.setCreatedDate(rs.getDate("created_date"));

                return account;
            } else {
                String message = "Счёт с id %d не найден.".formatted(id);
                throw new EntityNotFoundException(message);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обработке SQL-запроса", e);
        }
    }

    @Override
    public Account findByNumber(String number) {
        final Connection connection = connectionManager.getConnection();
        String query = "SELECT * FROM accounts WHERE number = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, number);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                final Account account = new Account();
                account.setId(rs.getLong("id"));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setCurrency(rs.getString("currency"));
                account.setNumber(rs.getString("number"));
                account.setUserId(rs.getInt("user_id"));
                account.setBankId(rs.getInt("bank_id"));
                account.setCreatedDate(rs.getDate("created_date"));

                return account;
            } else {
                String message = "Счёт с number %s не найден.".formatted(number);
                throw new EntityNotFoundException(message);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обработке SQL-запроса", e);
        }
    }

    @Override
    public Account update(Connection connection,Account account) {
        final long id = account.getId();

        String query = "UPDATE accounts SET balance = ?, currency = ?,number=?,user_id=?,bank_id=?,created_date=? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBigDecimal(1, account.getBalance());
            preparedStatement.setString(2,account.getCurrency());
            preparedStatement.setString(3,account.getNumber());
            preparedStatement.setInt(4,account.getUserId());
            preparedStatement.setInt(5,account.getBankId());
            preparedStatement.setDate(6, (Date) account.getCreatedDate());
            preparedStatement.setLong(7, id);

            int result = preparedStatement.executeUpdate();

            if (result == 0) {
                String message = "Пользователь с id %d не найден.".formatted(id);
                throw new EntityNotFoundException(message);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обработке SQL-запроса", e);
        }

        return account;
    }

    @Override
    public Account create(Account account) {
        return null;
    }
}
