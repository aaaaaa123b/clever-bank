package org.example.repository.impl;

import org.example.exception.EntityNotFoundException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.util.ConnectionManager;

import java.sql.*;

public class UserPostgreRepository implements UserRepository {

    private final ConnectionManager connectionManager;

    public UserPostgreRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Finds a user by their ID in the database.
     *
     * @param id the user's ID
     * @return the user object.
     */
    @Override
    public User findById(Long id) {

        final Connection connection = connectionManager.getConnection();
        String query = "SELECT * FROM users WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                final User user = new User();
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setPatronymic(rs.getString("patronymic"));
                user.setLogin(rs.getString("login"));

                return user;
            } else {
                String message = "Пользователь с id %d не найден.".formatted(id);
                throw new EntityNotFoundException(message);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обработке SQL-запроса", e);
        }
    }


    /**
     * Creates a new user in the database.
     *
     * @param firstName  the user's first name
     * @param lastName   the user's last name
     * @param patronymic the user's patronymic name
     * @param login      the user's login
     * @return the user object.
     */
    @Override
    public User addUser(String firstName, String lastName, String patronymic, String login) {

        Connection connection = connectionManager.getConnection();
        final String query = "INSERT INTO users (first_name, last_name, patronymic, login) VALUES (?, ?, ?, ?) RETURNING id";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, patronymic);
            preparedStatement.setString(4, login);


            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    System.out.println("Generated ID: " + id);
                    final User user = new User();
                    user.setId(id);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setPatronymic(patronymic);
                    user.setLogin(login);
                    return user;
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
     * Delete user by user id.
     *
     * @param id user ID
     */
    @Override
    public void deleteById(Long id) {
        Connection connection = connectionManager.getConnection();
        final String query = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении SQL-запроса", e);
        }
    }

    /**
     * Update user by user id in the database
     *
     * @param id user ID
     * @param user object user
     * @return updatet object user.
     */
    @Override
    public User updateUser(Long id, User user) {
        Connection connection = connectionManager.getConnection();

        String query = "UPDATE users SET first_name = ?, last_name = ?, patronymic=?, login=? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getPatronymic());
            preparedStatement.setString(4, user.getLogin());
            preparedStatement.setLong(5, id);

            int result = preparedStatement.executeUpdate();

            if (result == 0) {
                String message = "Пользователь с id %d не найден.".formatted(id);
                throw new EntityNotFoundException(message);
            }

            user.setId(id);
            return user;

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обработке SQL-запроса", e);
        }
    }


}
