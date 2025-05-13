package com.parkingManagement.dao;

import com.parkingManagement.model.ParkingSpace;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Объект доступа к данным для управления сущностями парковочных мест в базе данных.
 */
public class ParkingSpaceDao {
    private final Connection connection;

    public ParkingSpaceDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Создаёт новое парковочное место в базе данных.
     *
     * @param parkingSpace парковочное место для создания
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если парковка не существует
     */
    public void create(ParkingSpace parkingSpace) throws SQLException {
        String checkSql = "SELECT id FROM parking_lot WHERE id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setLong(1, parkingSpace.getParkingLotId());
            if (!checkStmt.executeQuery().next()) {
                throw new IllegalArgumentException("Парковка с ID " + parkingSpace.getParkingLotId() + " не существует");
            }
        }

        String sql = "INSERT INTO parking_space (parking_lot_id, space_number, type) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, parkingSpace.getParkingLotId());
            stmt.setString(2, parkingSpace.getSpaceNumber());
            stmt.setString(3, parkingSpace.getType());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    parkingSpace.setId(rs.getLong(1));
                }
            }
        }
    }

    /**
     * Находит парковочное место по его идентификатору.
     *
     * @param id идентификатор парковочного места
     * @return парковочное место или null, если не найдено
     * @throws SQLException при ошибке доступа к базе данных
     */
    public ParkingSpace findById(Long id) throws SQLException {
        String sql = "SELECT * FROM parking_space WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ParkingSpace(
                            rs.getLong("id"),
                            rs.getLong("parking_lot_id"),
                            rs.getString("space_number"),
                            rs.getString("type")
                    );
                }
                return null;
            }
        }
    }

    /**
     * Возвращает список всех парковочных мест из базы данных.
     *
     * @return список парковочных мест
     * @throws SQLException при ошибке доступа к базе данных
     */
    public List<ParkingSpace> findAll() throws SQLException {
        String sql = "SELECT * FROM parking_space";
        List<ParkingSpace> spaces = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                spaces.add(new ParkingSpace(
                        rs.getLong("id"),
                        rs.getLong("parking_lot_id"),
                        rs.getString("space_number"),
                        rs.getString("type")
                ));
            }
        }
        return spaces;
    }

    /**
     * Обновляет существующее парковочное место в базе данных.
     *
     * @param parkingSpace парковочное место для обновления
     * @return true, если обновление успешно, false, если парковочное место не существует
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если парковка не существует
     */
    public boolean update(ParkingSpace parkingSpace) throws SQLException {
        String checkSql = "SELECT id FROM parking_lot WHERE id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setLong(1, parkingSpace.getParkingLotId());
            if (!checkStmt.executeQuery().next()) {
                throw new IllegalArgumentException("Парковка с ID " + parkingSpace.getParkingLotId() + " не существует");
            }
        }

        String sql = "UPDATE parking_space SET parking_lot_id = ?, space_number = ?, type = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, parkingSpace.getParkingLotId());
            stmt.setString(2, parkingSpace.getSpaceNumber());
            stmt.setString(3, parkingSpace.getType());
            stmt.setLong(4, parkingSpace.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Удаляет парковочное место по его идентификатору.
     *
     * @param id идентификатор парковочного места для удаления
     * @return true, если удаление успешно, false, если парковочное место не существует
     * @throws SQLException при ошибке доступа к базе данных
     */
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM parking_space WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}