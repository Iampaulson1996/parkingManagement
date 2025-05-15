package com.parkingManagement.service;

import com.parkingManagement.dao.VehicleDao;
import com.parkingManagement.model.Vehicle;

import java.util.List;

/**
 * Сервис для управления автомобилями в системе управления парковкой.
 */
public class VehicleService {
    private final VehicleDao vehicleDao;

    public VehicleService(VehicleDao vehicleDao) {
        this.vehicleDao = vehicleDao;
    }

    /**
     * Создаёт новый автомобиль с проверкой данных.

     * @param vehicle автомобиль для создания
     * @throws IllegalArgumentException при некорректных данных
     */
    public void createVehicle(Vehicle vehicle) {
        validateVehicle(vehicle, false);
        vehicleDao.create(vehicle);
    }

    /**
     * Находит автомобиль по идентификатору.

     * @param id идентификатор автомобиля
     * @return автомобиль
     * @throws IllegalArgumentException если автомобиль не найден
     */
    public Vehicle getVehicle(Long id) {
        validateId(id, "Идентификатор автомобиля");
        Vehicle vehicle = vehicleDao.findById(id);
        if (vehicle == null) {
            throw new IllegalArgumentException("Автомобиль с ID " + id + " не найден");
        }
        return vehicle;
    }

    /**
     * Возвращает список всех автомобилей.

     * @return список автомобилей
     */
    public List<Vehicle> getAllVehicles() {
        return vehicleDao.findAll();
    }

    /**
     * Обновляет автомобиль.

     * @param vehicle автомобиль для обновления
     * @throws IllegalArgumentException если автомобиль не найден
     */
    public void updateVehicle(Vehicle vehicle) {
        validateVehicle(vehicle, true);
        if (!vehicleDao.update(vehicle)) {
            throw new IllegalArgumentException("Автомобиль с ID " + vehicle.getId() + " не найден");
        }
    }

    /**
     * Удаляет автомобиль по идентификатору.

     * @param id идентификатор автомобиля
     * @throws IllegalArgumentException если автомобиль не найден
     */
    public void deleteVehicle(Long id) {
        validateId(id, "Идентификатор автомобиля");
        if (!vehicleDao.delete(id)) {
            throw new IllegalArgumentException("Автомобиль с ID " + id + " не найден");
        }
    }

    /**
     * Проверяет корректность данных автомобиля.

     * @param vehicle  автомобиль для проверки
     * @param isUpdate флаг, указывающий, является ли операция обновлением
     * @throws IllegalArgumentException при некорректных данных
     */
    private void validateVehicle(Vehicle vehicle, boolean isUpdate) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Автомобиль не может быть null");
        }
        if (isUpdate && (vehicle.getId() == null || vehicle.getId() <= 0)) {
            throw new IllegalArgumentException("Идентификатор автомобиля должен быть положительным");
        }
        if (vehicle.getClient() == null || vehicle.getClient().getId() == null ||
                vehicle.getClient().getId() <= 0) {
            throw new IllegalArgumentException("Идентификатор клиента должен быть положительным");
        }
        if (vehicle.getLicensePlate() == null || vehicle.getLicensePlate().trim().isEmpty()) {
            throw new IllegalArgumentException("Регистрационный номер обязателен");
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