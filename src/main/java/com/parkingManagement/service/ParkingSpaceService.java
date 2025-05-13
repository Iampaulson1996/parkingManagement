package com.parkingManagement.service;

import com.parkingManagement.dao.ParkingSpaceDao;
import com.parkingManagement.model.ParkingSpace;

import java.sql.SQLException;
import java.util.List;

/**
 * Сервисный класс для управления операциями с парковочными местами.
 */
public class ParkingSpaceService {
    private final ParkingSpaceDao parkingSpaceDao;

    /**
     * Создаёт новый ParkingSpaceService с указанным DAO.
     *
     * @param parkingSpaceDao DAO для операций с парковочными местами
     */
    public ParkingSpaceService(ParkingSpaceDao parkingSpaceDao) {
        this.parkingSpaceDao = parkingSpaceDao;
    }

    /**
     * Создаёт новое парковочное место с проверкой данных.
     *
     * @param parkingSpace парковочное место для создания
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException при некорректных данных
     */
    public void createParkingSpace(ParkingSpace parkingSpace) throws SQLException {
        if (parkingSpace == null || parkingSpace.getParkingLotId() == null || parkingSpace.getParkingLotId() <= 0) {
            throw new IllegalArgumentException("Идентификатор парковки должен быть положительным");
        }
        if (parkingSpace.getSpaceNumber() == null || parkingSpace.getSpaceNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Номер парковочного места обязателен");
        }
        if (parkingSpace.getType() == null || parkingSpace.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Тип парковочного места обязателен");
        }
        parkingSpaceDao.create(parkingSpace);
    }

    /**
     * Находит парковочное место по его идентификатору.
     *
     * @param id идентификатор парковочного места
     * @return парковочное место
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если парковочное место не найдено
     */
    public ParkingSpace getParkingSpace(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Идентификатор парковочного места должен быть положительным");
        }
        ParkingSpace parkingSpace = parkingSpaceDao.findById(id);
        if (parkingSpace == null) {
            throw new IllegalArgumentException("Парковочное место с ID " + id + " не найдено");
        }
        return parkingSpace;
    }

    /**
     * Возвращает список всех парковочных мест.
     *
     * @return список парковочных мест
     * @throws SQLException при ошибке доступа к базе данных
     */
    public List<ParkingSpace> getAllParkingSpaces() throws SQLException {
        return parkingSpaceDao.findAll();
    }

    /**
     * Обновляет существующее парковочное место.
     *
     * @param parkingSpace парковочное место для обновления
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если парковочное место не найдено
     */
    public void updateParkingSpace(ParkingSpace parkingSpace) throws SQLException {
        if (parkingSpace == null || parkingSpace.getId() == null || parkingSpace.getId() <= 0) {
            throw new IllegalArgumentException("Идентификатор парковочного места должен быть положительным");
        }
        if (parkingSpace.getParkingLotId() == null || parkingSpace.getParkingLotId() <= 0) {
            throw new IllegalArgumentException("Идентификатор парковки должен быть положительным");
        }
        if (parkingSpace.getSpaceNumber() == null || parkingSpace.getSpaceNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Номер парковочного места обязателен");
        }
        if (parkingSpace.getType() == null || parkingSpace.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Тип парковочного места обязателен");
        }
        if (!parkingSpaceDao.update(parkingSpace)) {
            throw new IllegalArgumentException("Парковочное место с ID " + parkingSpace.getId() + " не найдено");
        }
    }

    /**
     * Удаляет парковочное место по его идентификатору.
     *
     * @param id идентификатор парковочного места для удаления
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если парковочное место не найдено
     */
    public void deleteParkingSpace(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Идентификатор парковочного места должен быть положительным");
        }
        if (!parkingSpaceDao.delete(id)) {
            throw new IllegalArgumentException("Парковочное место с ID " + id + " не найдено");
        }
    }
}