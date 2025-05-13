package com.parkingManagement.dao;

import com.parkingManagement.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Объект доступа к данным для управления сущностями клиентов в базе данных.
 */
public class ClientDao {
    private final Connection connection;

    public ClientDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Создаёт нового клиента в базе данных.
     *
     * @param client клиент для создания
     * @throws SQLException при ошибке доступа к базе данных
     */
    public void create(Client client) throws SQLException {
        String sql = "INSERT INTO client (name, phone, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getPhone());
            stmt.setString(3, client.getEmail());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    client.setId(rs.getLong(1));
                }
            }
        }
    }

    /**
     * Находит клиента по его идентификатору.
     *
     * @param id идентификатор клиента
     * @return клиент или null, если не найден
     * @throws SQLException при ошибке доступа к базе данных
     */
    public Client findById(Long id) throws SQLException {
        String sql = "SELECT * FROM client WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("email")
                    );
                }
                return null;
            }
        }
    }

    /**
     * Возвращает список всех клиентов из базы данных.
     *
     * @return список клиентов
     * @throws SQLException при ошибке доступа к базе данных
     */
    public List<Client> findAll() throws SQLException {
        String sql = "SELECT * FROM client";
        List<Client> clients = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(new Client(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email")
                ));
            }
        }
        return clients;
    }

    /**
     * Обновляет существующего клиента в базе данных.
     *
     * @param client клиент для обновления
     * @return true, если обновление успешно, false, если клиент не существует
     * @throws SQLException при ошибке доступа к базе данных
     */
    public boolean update(Client client) throws SQLException {
        String sql = "UPDATE client SET name = ?, phone = ?, email = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getPhone());
            stmt.setString(3, client.getEmail());
            stmt.setLong(4, client.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Удаляет клиента по его идентификатору.
     *
     * @param id идентификатор клиента для удаления
     * @return true, если удаление успешно, false, если клиент не существует
     * @throws SQLException при ошибке доступа к базе данных
     */
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM client WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}