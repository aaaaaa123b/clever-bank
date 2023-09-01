package org.example.repository.impl;

import org.example.enumeration.TransactionType;
import org.example.exception.EntityNotFoundException;
import org.example.model.Account;
import org.example.model.Transaction;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.example.util.ConnectionManager;

import java.sql.*;
import java.util.Optional;

public class TransactionPostgreRepository implements TransactionRepository {
    private final ConnectionManager connectionManager;
    private final AccountRepository accountRepository;

    public TransactionPostgreRepository(ConnectionManager connectionManager, AccountRepository accountRepository) {
        this.connectionManager = connectionManager;
        this.accountRepository = accountRepository;
    }

    public Transaction create(Transaction transaction) {
        Connection connection = connectionManager.getConnection();
        final String query = "INSERT INTO transactions ( time, type, sender_account,recipient_account,amount,date) VALUES (?,?, ?, ?, ?,?)RETURNING id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setTime(1, transaction.getTime());
            preparedStatement.setString(2, transaction.getType().name());

            Long recepientAccountId = Optional.ofNullable(transaction.getRecipientAccount())
                    .map(Account::getId)
                    .orElse(null);

            Long senderAccountId = Optional.ofNullable(transaction.getSenderAccount())
                    .map(Account::getId)
                    .orElse(null);

            preparedStatement.setObject(3, senderAccountId);
            preparedStatement.setObject(4, recepientAccountId);
            preparedStatement.setBigDecimal(5, transaction.getAmount());
            preparedStatement.setDate(6, transaction.getDate());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    long generatedId = resultSet.getLong("id");
                    System.out.println("Generated ID: " + generatedId);
                } else {
                    System.out.println("Не удалось добавить пользователя.");
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении SQL-запроса", e);
        }
        return null;
    }

    @Override
    public Transaction findById(int id) {
        final Connection connection = connectionManager.getConnection();
        String query = "SELECT * FROM transactions WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                final Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));

                transaction.setTime(rs.getTime("time"));
                transaction.setType(TransactionType.valueOf(rs.getString("type")));
//                Long recepientAccountId = Optional.ofNullable(transaction.getRecipientAccount())
//                        .map(Account::getId)
//                        .orElse(null);
//
//                Long senderAccountId = Optional.ofNullable(transaction.getSenderAccount())
//                        .map(Account::getId)
//                        .orElse(null);
//
//                preparedStatement.setObject(3, senderAccountId);
//                preparedStatement.setObject(4, recepientAccountId);

                Optional.ofNullable(rs.getObject("sender_account", Long.class))
                        .ifPresent(senderAccountId -> {
                            transaction.setSenderAccount(accountRepository.findById(senderAccountId));
                        });
                Optional.ofNullable(rs.getObject("recipient_account", Long.class))
                        .ifPresent(recipientAccount -> {
                            transaction.setRecipientAccount(accountRepository.findById(recipientAccount));
                        });
//                transaction.setSenderAccount(
//                        accountRepository.findById(rs.getLong("sender_account"))
//                );
//                transaction.setRecipientAccount(
//                        accountRepository.findById(rs.getLong("recipient_account"))
//                );
                transaction.setAmount(rs.getBigDecimal("amount"));
                transaction.setDate(rs.getDate("date"));

                return transaction;
            } else {
                String message = "Банк с id %d не найден.".formatted(id);
                throw new EntityNotFoundException(message);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обработке SQL-запроса", e);
        }
    }

}
