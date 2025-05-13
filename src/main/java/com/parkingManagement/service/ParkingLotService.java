package com.parkingManagement.service;

import com.parkingManagement.dao.ParkingLotDao;
import com.parkingManagement.model.ParkingLot;

import java.sql.SQLException;
import java.util.List;

/**
 * Сервисный класс для управления операциями с парковками.
 */
public class ParkingLotService {
    private final ParkingLotDao parkingLotDao;

    /**
     * Создаёт новый ParkingLotService с указанным DAO.
     *
     * @param parkingLotDao DAO для операций с парковками
     */
    public ParkingLotService(ParkingLotDao parkingLotDao) {
        this.parkingLotDao = parkingLotDao;
    }

    /**
     * Создаёт новую парковку с проверкой данных.
     *
     * @param parkingLot парковка для создания
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException при некорректных данных
     */
    public void createParkingLot(ParkingLot parkingLot) throws SQLException {
        if (parkingLot == null || parkingLot.getName() == null || parkingLot.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Название парковки обязательно");
        }
        if (parkingLot.getAddress() == null || parkingLot.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Адрес парковки обязателен");
        }
        if (parkingLot.getCapacity() == null || parkingLot.getCapacity() <= 0) {
            throw new IllegalArgumentException("Вместимость парковки должна быть положительной");
        }
        parkingLotDao.create(parkingLot);
    }

    /**
     * Находит парковку по её идентификатору.
     *
     * @param id идентификатор парковки
     * @return парковка
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если парковка не найдена
     */
    public ParkingLot getParkingLot(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Идентификатор парковки должен быть положительным");
        }
        ParkingLot parkingLot = parkingLotDao.findById(id);
        if (parkingLot == null) {
            throw new IllegalArgumentException("Парковка с ID " + id + " не найдена");
        }
        return parkingLot;
    }

    /**
     * Возвращает список всех парковок.
     *
     * @return список парковок
     * @throws SQLException при ошибке доступа к базе данных
     */
    public List<ParkingLot> getAllParkingLots() throws SQLException {
        return parkingLotDao.findAll();
    }

    /**
     * Обновляет существующую парковку.
     *
     * @param parkingLot парковка для обновления
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если парковка не найдена
     */
    public void updateParkingLot(ParkingLot parkingLot) throws SQLException {
        if (parkingLot == null || parkingLot.getId() == null || parkingLot.getId() <= 0) {
            throw new IllegalArgumentException("Идентификатор парковки должен быть положительным");
        }
        if (parkingLot.getName() == null || parkingLot.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Название парковки обязательно");
        }
        if (parkingLot.getAddress() == null || parkingLot.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Адрес парковки обязателен");
        }
        if (parkingLot.getCapacity() == null || parkingLot.getCapacity() <= 0) {
            throw new IllegalArgumentException("Вместимость парковки должна быть положительной");
        }
        if (!parkingLotDao.update(parkingLot)) {
            throw new IllegalArgumentException("Парковка с ID " + parkingLot.getId() + " не найдена");
        }
    }

    /**
     * Удаляет парковку по её идентификатору.
     *
     * @param id идентификатор парковки для удаления
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если парковка не найдена
     */
    public void deleteParkingLot(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Идентификатор парковки должен быть положительным");
        }
        if (!parkingLotDao.delete(id)) {
            throw new IllegalArgumentException("Парковка с ID " + id + " не найдена");
        }
    }
}