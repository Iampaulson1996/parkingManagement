package com.parkingManagement.service;

import com.parkingManagement.dao.ParkingRecordDao;
import com.parkingManagement.model.ParkingRecord;

import java.sql.SQLException;
import java.util.List;

/**
 * Сервисный класс для управления операциями с записями о парковке.
 */
public class ParkingRecordService {
    private final ParkingRecordDao parkingRecordDao;

    /**
     * Создаёт новый ParkingRecordService с указанным DAO.
     *
     * @param parkingRecordDao DAO для операций с записями о парковке
     */
    public ParkingRecordService(ParkingRecordDao parkingRecordDao) {
        this.parkingRecordDao = parkingRecordDao;
    }

    /**
     * Создаёт новую запись о парковке с проверкой данных.
     *
     * @param record запись о парковке для создания
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException при некорректных данных
     */
    public void createParkingRecord(ParkingRecord record) throws SQLException {
        if (record == null || record.getParkingSpaceId() == null || record.getParkingSpaceId() <= 0) {
            throw new IllegalArgumentException("Идентификатор парковочного места должен быть положительным");
        }
        if (record.getVehicleId() == null || record.getVehicleId() <= 0) {
            throw new IllegalArgumentException("Идентификатор автомобиля должен быть положительным");
        }
        if (record.getClientId() == null || record.getClientId() <= 0) {
            throw new IllegalArgumentException("Идентификатор клиента должен быть положительным");
        }
        if (record.getEntryTime() == null) {
            throw new IllegalArgumentException("Время въезда обязательно");
        }
        parkingRecordDao.create(record);
    }

    /**
     * Находит запись о парковке по её идентификатору.
     *
     * @param id идентификатор записи
     * @return запись о парковке
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если запись не найдена
     */
    public ParkingRecord getParkingRecord(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Идентификатор записи о парковке должен быть положительным");
        }
        ParkingRecord record = parkingRecordDao.findById(id);
        if (record == null) {
            throw new IllegalArgumentException("Запись о парковке с ID " + id + " не найдена");
        }
        return record;
    }

    /**
     * Возвращает список всех записей о парковке.
     *
     * @return список записей о парковке
     * @throws SQLException при ошибке доступа к базе данных
     */
    public List<ParkingRecord> getAllParkingRecords() throws SQLException {
        return parkingRecordDao.findAll();
    }

    /**
     * Обновляет существующую запись о парковке.
     *
     * @param record запись о парковке для обновления
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если запись не найдена
     */
    public void updateParkingRecord(ParkingRecord record) throws SQLException {
        if (record == null || record.getId() == null || record.getId() <= 0) {
            throw new IllegalArgumentException("Идентификатор записи о парковке должен быть положительным");
        }
        if (record.getParkingSpaceId() == null || record.getParkingSpaceId() <= 0) {
            throw new IllegalArgumentException("Идентификатор парковочного места должен быть положительным");
        }
        if (record.getVehicleId() == null || record.getVehicleId() <= 0) {
            throw new IllegalArgumentException("Идентификатор автомобиля должен быть положительным");
        }
        if (record.getClientId() == null || record.getClientId() <= 0) {
            throw new IllegalArgumentException("Идентификатор клиента должен быть положительным");
        }
        if (record.getEntryTime() == null) {
            throw new IllegalArgumentException("Время въезда обязательно");
        }
        if (!parkingRecordDao.update(record)) {
            throw new IllegalArgumentException("Запись о парковке с ID " + record.getId() + " не найдена");
        }
    }

    /**
     * Удаляет запись о парковке по её идентификатору.
     *
     * @param id идентификатор записи для удаления
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если запись не найдена
     */
    public void deleteParkingRecord(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Идентификатор записи о парковке должен быть положительным");
        }
        if (!parkingRecordDao.delete(id)) {
            throw new IllegalArgumentException("Запись о парковке с ID " + id + " не найдена");
        }
    }
}