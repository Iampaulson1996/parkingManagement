package com.parkingManagement.dao;

import com.parkingManagement.model.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Объект доступа к данным для управления сущностями автомобилей в базе данных.
 */
public class VehicleDao {
    private final Connection connection;

    public VehicleDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Создаёт новый автомобиль в базе данных.
     *
     * @param vehicle автомобиль для создания
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если клиент не существует
     */
    public void create(Vehicle vehicle) throws SQLException {
        String checkSql = "SELECT id FROM client WHERE id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setLong(1, vehicle.getClientId());
            if (!checkStmt.executeQuery().next()) {
                throw new IllegalArgumentException("Клиент с ID " + vehicle.getClientId() + " не существует");
            }
        }

        String sql = "INSERT INTO vehicle (client_id, license_plate, brand, model) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, vehicle.getClientId());
            stmt.setString(2, vehicle.getLicensePlate());
            stmt.setString(3, vehicle.getBrand());
            stmt.setString(4, vehicle.getModel());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    vehicle.setId(rs.getLong(1));
                }
            }
        }
    }

    /**
     * Находит автомобиль по его идентификатору.
     *
     * @param id идентификатор автомобиля
     * @return автомобиль или null, если не найден
     * @throws SQLException при ошибке доступа к базе данных
     */
    public Vehicle findById(Long id) throws SQLException {
        String sql = "SELECT * FROM vehicle WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Vehicle(
                            rs.getLong("id"),
                            rs.getLong("client_id"),
                            rs.getString("license_plate"),
                            rs.getString("brand"),
                            rs.getString("model")
                    );
                }
                return null;
            }
        }
    }

    /**
     * Возвращает список всех автомобилей из базы данных.
     *
     * @return список автомобилей
     * @throws SQLException при ошибке доступа к базе данных
     */
    public List<Vehicle> findAll() throws SQLException {
        String sql = "SELECT * FROM vehicle";
        List<Vehicle> vehicles = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vehicles.add(new Vehicle(
                        rs.getLong("id"),
                        rs.getLong("client_id"),
                        rs.getString("license_plate"),
                        rs.getString("brand"),
                        rs.getString("model")
                ));
            }
        }
        return vehicles;
    }

    /**
     * Обновляет существующий автомобиль в базе данных.
     *
     * @param vehicle автомобиль для обновления
     * @return true, если обновление успешно, false, если автомобиль не существует
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если клиент не существует
     */
    public boolean update(Vehicle vehicle) throws SQLException {
        String checkSql = "SELECT id FROM client WHERE id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setLong(1, vehicle.getClientId());
            if (!checkStmt.executeQuery().next()) {
                throw new IllegalArgumentException("Клиент с ID " + vehicle.getClientId() + " не существует");
            }
        }

        String sql = "UPDATE vehicle SET client_id = ?, license_plate = ?, brand = ?, model = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, vehicle.getClientId());
            stmt.setString(2, vehicle.getLicensePlate());
            stmt.setString(3, vehicle.getBrand());
            stmt.setString(4, vehicle.getModel());
            stmt.setLong(5, vehicle.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Удаляет автомобиль по его идентификатору.
     *
     * @param id идентификатор автомобиля для удаления
     * @return true, если удаление успешно, false, если автомобиль не существует
     * @throws SQLException при ошибке доступа к базе данных
     */
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM vehicle WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}