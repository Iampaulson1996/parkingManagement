package com.parkingManagement.service;

import com.parkingManagement.dao.ParkingSpaceDao;
import com.parkingManagement.model.ParkingSpace;

import java.util.List;
import java.util.Set;

/**
 * Сервис для управления парковочными местами в системе управления парковкой.
 */
public class ParkingSpaceService {
    private static final Set<String> VALID_TYPES = Set.of("REGULAR", "DISABLED", "VIP");
    private final ParkingSpaceDao parkingSpaceDao;

    public ParkingSpaceService(ParkingSpaceDao parkingSpaceDao) {
        this.parkingSpaceDao = parkingSpaceDao;
    }

    /**
     * Создаёт новое парковочное место с проверкой данных.

     * @param parkingSpace парковочное место для создания
     * @throws IllegalArgumentException при некорректных данных
     */
    public void createParkingSpace(ParkingSpace parkingSpace) {
        validateParkingSpace(parkingSpace, false);
        parkingSpaceDao.create(parkingSpace);
    }

    /**
     * Находит парковочное место по идентификатору.

     * @param id идентификатор парковочного места
     * @return парковочное место
     * @throws IllegalArgumentException если место не найдено
     */
    public ParkingSpace getParkingSpace(Long id) {
        validateId(id, "Идентификатор парковочного места");
        ParkingSpace parkingSpace = parkingSpaceDao.findById(id);
        if (parkingSpace == null) {
            throw new IllegalArgumentException("Место с ID " + id + " не найдено");
        }
        return parkingSpace;
    }

    /**
     * Возвращает список всех парковочных мест.

     * @return список парковочных мест
     */
    public List<ParkingSpace> getAllParkingSpaces() {
        return parkingSpaceDao.findAll();
    }

    /**
     * Обновляет парковочное место.

     * @param parkingSpace парковочное место для обновления
     * @throws IllegalArgumentException если место не найдено
     */
    public void updateParkingSpace(ParkingSpace parkingSpace) {
        validateParkingSpace(parkingSpace, true);
        if (!parkingSpaceDao.update(parkingSpace)) {
            throw new IllegalArgumentException("Место с ID " + parkingSpace.getId() + " не найдено");
        }
    }

    /**
     * Удаляет парковочное место по идентификатору.

     * @param id идентификатор парковочного места
     * @throws IllegalArgumentException если место не найдено
     */
    public void deleteParkingSpace(Long id) {
        validateId(id, "Идентификатор парковочного места");
        if (!parkingSpaceDao.delete(id)) {
            throw new IllegalArgumentException("Место с ID " + id + " не найдено");
        }
    }

    /**
     * Проверяет корректность данных парковочного места.

     * @param parkingSpace парковочное место для проверки
     * @param isUpdate     флаг, указывающий, является ли операция обновлением
     * @throws IllegalArgumentException при некорректных данных
     */
    private void validateParkingSpace(ParkingSpace parkingSpace, boolean isUpdate) {
        if (parkingSpace == null) {
            throw new IllegalArgumentException("Место не может быть null");
        }
        if (isUpdate && (parkingSpace.getId() == null || parkingSpace.getId() <= 0)) {
            throw new IllegalArgumentException("Идентификатор места должен быть положительным");
        }
        if (parkingSpace.getParkingLot() == null || parkingSpace.getParkingLot().getId() == null ||
                parkingSpace.getParkingLot().getId() <= 0) {
            throw new IllegalArgumentException("Идентификатор парковки должен быть положительным");
        }
        if (parkingSpace.getSpaceNumber() == null || parkingSpace.getSpaceNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Номер места обязателен");
        }
        if (parkingSpace.getType() == null || parkingSpace.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Тип места обязателен");
        }
        if (!VALID_TYPES.contains(parkingSpace.getType().toUpperCase())) {
            throw new IllegalArgumentException("Тип места должен быть REGULAR, DISABLED или VIP");
        }
    }

    /**
     * Проверяет корректность идентификатора.

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