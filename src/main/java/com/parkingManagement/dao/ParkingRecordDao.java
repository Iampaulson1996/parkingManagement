package com.parkingManagement.dao;

import com.parkingManagement.model.ParkingRecord;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Объект доступа к данным для управления записями о парковке в базе данных.
 */
public class ParkingRecordDao {
    private final Connection connection;

    public ParkingRecordDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Создаёт новую запись о парковке в базе данных.
     *
     * @param record запись о парковке для создания
     * @throws SQLException             при ошибке доступа к базе данных
     * @throws IllegalArgumentException если связанные сущности не существуют
     */
    public void create(ParkingRecord record) throws SQLException {
        String checkSpaceSql = "SELECT id FROM parking_space WHERE id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSpaceSql)) {
            checkStmt.setLong(1, record.getParkingSpaceId());
            if (!checkStmt.executeQuery().next()) {
                throw new IllegalArgumentException("Парковочное место с ID " + record.getParkingSpaceId() +
                        " не существует");
            }
        }

        String checkVehicleSql = "SELECT id FROM vehicle WHERE id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkVehicleSql)) {
            checkStmt.setLong(1, record.getVehicleId());
            if (!checkStmt.executeQuery().next()) {
                throw new IllegalArgumentException("Автомобиль с ID " + record.getVehicleId() + " не существует");
            }
        }

        String checkClientSql = "SELECT id FROM client WHERE id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkClientSql)) {
            checkStmt.setLong(1, record.getClientId());
            if (!checkStmt.executeQuery().next()) {
                throw new IllegalArgumentException("Клиент с ID " + record.getClientId() + " не существует");
            }
        }

        String sql = "INSERT INTO parking_record (parking_space_id, vehicle_id, client_id, entry_time, exit_time) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, record.getParkingSpaceId());
            stmt.setLong(2, record.getVehicleId());
            stmt.setLong(3, record.getClientId());
            stmt.setTimestamp(4, Timestamp.valueOf(record.getEntryTime()));
            stmt.setTimestamp(5, record.getExitTime() != null ? Timestamp.valueOf(record.getExitTime())
                    : null);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    record.setId(rs.getLong(1));
                }
            }
        }
    }

    /**
     * Находит запись о парковке по её идентификатору.
     *
     * @param id идентификатор записи
     * @return запись о парковке или null, если не найдена
     * @throws SQLException при ошибке доступа к базе данных
     */
    public ParkingRecord findById(Long id) throws SQLException {
        String sql = "SELECT * FROM parking_record WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ParkingRecord(
                            rs.getLong("id"),
                            rs.getLong("parking_space_id"),
                            rs.getLong("vehicle_id"),
                            rs.getLong("client_id"),
                            rs.getTimestamp("entry_time").toLocalDateTime(),
                            rs.getTimestamp("exit_time") != null ? rs.getTimestamp("exit_time")
                                    .toLocalDateTime() : null
                    );
                }
                return null;
            }
        }
    }

    /**
     * Возвращает список всех записей о парковке из базы данных.
     *
     * @return список записей о парковке
     * @throws SQLException при ошибке доступа к базе данных
     */
    public List<ParkingRecord> findAll() throws SQLException {
        String sql = "SELECT * FROM parking_record";
        List<ParkingRecord> records = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                records.add(new ParkingRecord(
                        rs.getLong("id"),
                        rs.getLong("parking_space_id"),
                        rs.getLong("vehicle_id"),
                        rs.getLong("client_id"),
                        rs.getTimestamp("entry_time").toLocalDateTime(),
                        rs.getTimestamp("exit_time") != null ? rs.getTimestamp("exit_time")
                                .toLocalDateTime() : null
                ));
            }
        }
        return records;
    }

    /**
     * Обновляет существующую запись о парковке в базе данных.
     *
     * @param record запись о парковке для обновления
     * @return true, если обновление успешно, false, если запись не существует
     * @throws SQLException             при ошибке доступа к базе данных
     * @throws IllegalArgumentException если связанные сущности не существуют
     */
    public boolean update(ParkingRecord record) throws SQLException {
        String checkSpaceSql = "SELECT id FROM parking_space WHERE id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSpaceSql)) {
            checkStmt.setLong(1, record.getParkingSpaceId());
            if (!checkStmt.executeQuery().next()) {
                throw new IllegalArgumentException("Парковочное место с ID " + record.getParkingSpaceId()
                        + " не существует");
            }
        }

        String checkVehicleSql = "SELECT id FROM vehicle WHERE id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkVehicleSql)) {
            checkStmt.setLong(1, record.getVehicleId());
            if (!checkStmt.executeQuery().next()) {
                throw new IllegalArgumentException("Автомобиль с ID " + record.getVehicleId() + " не существует");
            }
        }

        String checkClientSql = "SELECT id FROM client WHERE id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkClientSql)) {
            checkStmt.setLong(1, record.getClientId());
            if (!checkStmt.executeQuery().next()) {
                throw new IllegalArgumentException("Клиент с ID " + record.getClientId() + " не существует");
            }
        }

        String sql = "UPDATE parking_record SET parking_space_id = ?, vehicle_id = ?, client_id = ?, " +
                "entry_time = ?, exit_time = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, record.getParkingSpaceId());
            stmt.setLong(2, record.getVehicleId());
            stmt.setLong(3, record.getClientId());
            stmt.setTimestamp(4, Timestamp.valueOf(record.getEntryTime()));
            stmt.setTimestamp(5, record.getExitTime() != null ? Timestamp.valueOf(record.getExitTime())
                    : null);
            stmt.setLong(6, record.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Удаляет запись о парковке по её идентификатору.
     *
     * @param id идентификатор записи для удаления
     * @return true, если удаление успешно, false, если запись не существует
     * @throws SQLException при ошибке доступа к базе данных
     */
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM parking_record WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}