package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    final String url = "jdbc:postgresql://localhost:5432/dbCleverBank";
    final String username = "postgres";
    final String password = "4021";

    static {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Драйвер 'org.postgresql.Driver' успешно загружен!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Драйвер для подключения к БД не был найден!", e);
        }
    }

    public Connection getConnection() {
        Connection connection;

        try {
            connection = DriverManager.getConnection(url, username, password);
           // System.out.println("Соединение успешно установлено!");
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось установить соединение с БД: ", e);
        }

        return connection;
    }

}
