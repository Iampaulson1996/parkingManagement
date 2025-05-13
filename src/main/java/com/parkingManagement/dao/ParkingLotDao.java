package com.parkingManagement.dao;

import com.parkingManagement.model.ParkingLot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Объект доступа к данным для управления сущностями парковок в базе данных.
 */
public class ParkingLotDao {
    private final Connection connection;

    public ParkingLotDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Создаёт новую парковку в базе данных.
     *
     * @param parkingLot парковка для создания
     * @throws SQLException при ошибке доступа к базе данных
     */
    public void create(ParkingLot parkingLot) throws SQLException {
        String sql = "INSERT INTO parking_lot (name, address, capacity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, parkingLot.getName());
            stmt.setString(2, parkingLot.getAddress());
            stmt.setInt(3, parkingLot.getCapacity());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    parkingLot.setId(rs.getLong(1));
                }
            }
        }
    }

    /**
     * Находит парковку по её идентификатору.
     *
     * @param id идентификатор парковки
     * @return парковка или null, если не найдена
     * @throws SQLException при ошибке доступа к базе данных
     */
    public ParkingLot findById(Long id) throws SQLException {
        String sql = "SELECT * FROM parking_lot WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ParkingLot(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getInt("capacity")
                    );
                }
                return null;
            }
        }
    }

    /**
     * Возвращает список всех парковок из базы данных.
     *
     * @return список парковок
     * @throws SQLException при ошибке доступа к базе данных
     */
    public List<ParkingLot> findAll() throws SQLException {
        String sql = "SELECT * FROM parking_lot";
        List<ParkingLot> lots = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lots.add(new ParkingLot(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getInt("capacity")
                ));
            }
        }
        return lots;
    }

    /**
     * Обновляет существующую парковку в базе данных.
     *
     * @param parkingLot парковка для обновления
     * @return true, если обновление успешно, false, если парковка не существует
     * @throws SQLException при ошибке доступа к базе данных
     */
    public boolean update(ParkingLot parkingLot) throws SQLException {
        String sql = "UPDATE parking_lot SET name = ?, address = ?, capacity = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, parkingLot.getName());
            stmt.setString(2, parkingLot.getAddress());
            stmt.setInt(3, parkingLot.getCapacity());
            stmt.setLong(4, parkingLot.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Удаляет парковку по её идентификатору.
     *
     * @param id идентификатор парковки для удаления
     * @return true, если удаление успешно, false, если парковка не существует
     * @throws SQLException при ошибке доступа к базе данных
     */
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM parking_lot WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}