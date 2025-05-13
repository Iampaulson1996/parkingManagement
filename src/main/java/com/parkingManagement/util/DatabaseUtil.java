package com.parkingManagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Утилитный класс для управления подключением к базе данных.
 */
public class DatabaseUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/parking";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123"; // Замените на ваш пароль PostgreSQL

    /**
     * Устанавливает соединение с базой данных PostgreSQL.
     *
     * @return объект Connection
     * @throws SQLException при ошибке доступа к базе данных
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}