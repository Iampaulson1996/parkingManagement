package com.parkingManagement.service;

import com.parkingManagement.dao.VehicleDao;
import com.parkingManagement.model.Vehicle;

import java.sql.SQLException;
import java.util.List;

/**
 * Сервисный класс для управления операциями с автомобилями.
 */
public class VehicleService {
    private final VehicleDao vehicleDao;

    /**
     * Создаёт новый VehicleService с указанным DAO.
     *
     * @param vehicleDao DAO для операций с автомобилями
     */
    public VehicleService(VehicleDao vehicleDao) {
        this.vehicleDao = vehicleDao;
    }

    /**
     * Создаёт новый автомобиль с проверкой данных.
     *
     * @param vehicle автомобиль для создания
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException при некорректных данных
     */
    public void createVehicle(Vehicle vehicle) throws SQLException {
        if (vehicle == null || vehicle.getClientId() == null || vehicle.getClientId() <= 0) {
            throw new IllegalArgumentException("Идентификатор клиента должен быть положительным");
        }
        if (vehicle.getLicensePlate() == null || vehicle.getLicensePlate().trim().isEmpty()) {
            throw new IllegalArgumentException("Регистрационный номер обязателен");
        }
        vehicleDao.create(vehicle);
    }

    /**
     * Находит автомобиль по его идентификатору.
     *
     * @param id идентификатор автомобиля
     * @return автомобиль
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если автомобиль не найден
     */
    public Vehicle getVehicle(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Идентификатор автомобиля должен быть положительным");
        }
        Vehicle vehicle = vehicleDao.findById(id);
        if (vehicle == null) {
            throw new IllegalArgumentException("Автомобиль с ID " + id + " не найден");
        }
        return vehicle;
    }

    /**
     * Возвращает список всех автомобилей.
     *
     * @return список автомобилей
     * @throws SQLException при ошибке доступа к базе данных
     */
    public List<Vehicle> getAllVehicles() throws SQLException {
        return vehicleDao.findAll();
    }

    /**
     * Обновляет существующий автомобиль.
     *
     * @param vehicle автомобиль для обновления
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если автомобиль не найден
     */
    public void updateVehicle(Vehicle vehicle) throws SQLException {
        if (vehicle == null || vehicle.getId() == null || vehicle.getId() <= 0) {
            throw new IllegalArgumentException("Идентификатор автомобиля должен быть положительным");
        }
        if (vehicle.getClientId() == null || vehicle.getClientId() <= 0) {
            throw new IllegalArgumentException("Идентификатор клиента должен быть положительным");
        }
        if (vehicle.getLicensePlate() == null || vehicle.getLicensePlate().trim().isEmpty()) {
            throw new IllegalArgumentException("Регистрационный номер обязателен");
        }
        if (!vehicleDao.update(vehicle)) {
            throw new IllegalArgumentException("Автомобиль с ID " + vehicle.getId() + " не найден");
        }
    }

    /**
     * Удаляет автомобиль по его идентификатору.
     *
     * @param id идентификатор автомобиля для удаления
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если автомобиль не найден
     */
    public void deleteVehicle(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Идентификатор автомобиля должен быть положительным");
        }
        if (!vehicleDao.delete(id)) {
            throw new IllegalArgumentException("Автомобиль с ID " + id + " не найден");
        }
    }
}