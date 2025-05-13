package com.parkingManagement.service;

import com.parkingManagement.dao.ParkingLotDao;
import com.parkingManagement.model.ParkingLot;

import java.util.List;

/**
 * Сервис для управления парковками в системе управления парковкой.
 */
public class ParkingLotService {
    private final ParkingLotDao parkingLotDao;

    public ParkingLotService(ParkingLotDao parkingLotDao) {
        this.parkingLotDao = parkingLotDao;
    }

    /**
     * Создаёт новую парковку с проверкой данных.
     *
     * @param parkingLot парковка для создания
     * @throws IllegalArgumentException при некорректных данных
     */
    public void createParkingLot(ParkingLot parkingLot) {
        validateParkingLot(parkingLot, false);
        parkingLotDao.create(parkingLot);
    }

    /**
     * Находит парковку по идентификатору.
     *
     * @param id идентификатор парковки
     * @return парковка
     * @throws IllegalArgumentException если парковка не найдена
     */
    public ParkingLot getParkingLot(Long id) {
        validateId(id, "Идентификатор парковки");
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
     */
    public List<ParkingLot> getAllParkingLots() {
        return parkingLotDao.findAll();
    }

    /**
     * Обновляет парковку.
     *
     * @param parkingLot парковка для обновления
     * @throws IllegalArgumentException если парковка не найдена
     */
    public void updateParkingLot(ParkingLot parkingLot) {
        validateParkingLot(parkingLot, true);
        if (!parkingLotDao.update(parkingLot)) {
            throw new IllegalArgumentException("Парковка с ID " + parkingLot.getId() + " не найдена");
        }
    }

    /**
     * Удаляет парковку по идентификатору.
     *
     * @param id идентификатор парковки
     * @throws IllegalArgumentException если парковка не найдена
     */
    public void deleteParkingLot(Long id) {
        validateId(id, "Идентификатор парковки");
        if (!parkingLotDao.delete(id)) {
            throw new IllegalArgumentException("Парковка с ID " + id + " не найдена");
        }
    }

    /**
     * Проверяет корректность данных парковки.
     *
     * @param parkingLot парковка для проверки
     * @param isUpdate   флаг, указывающий, является ли операция обновлением
     * @throws IllegalArgumentException при некорректных данных
     */
    private void validateParkingLot(ParkingLot parkingLot, boolean isUpdate) {
        if (parkingLot == null) {
            throw new IllegalArgumentException("Парковка не может быть null");
        }
        if (isUpdate && (parkingLot.getId() == null || parkingLot.getId() <= 0)) {
            throw new IllegalArgumentException("Идентификатор парковки должен быть положительным");
        }
        if (parkingLot.getName() == null || parkingLot.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Название парковки обязательно");
        }
        if (parkingLot.getAddress() == null || parkingLot.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Адрес парковки обязателен");
        }
        if (parkingLot.getCapacity() == null || parkingLot.getCapacity() <= 0) {
            throw new IllegalArgumentException("Вместимость должна быть положительной");
        }
    }

    /**
     * Проверяет корректность идентификатора.
     *
     * @param id      идентификатор
     * @param field   название поля для сообщения об ошибке
     * @throws IllegalArgumentException при некорректном идентификаторе
     */
    private void validateId(Long id, String field) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(field + " должен быть положительным");
        }
    }
}