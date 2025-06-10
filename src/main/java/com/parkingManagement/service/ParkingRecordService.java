package com.parkingManagement.service;

import com.parkingManagement.dao.ParkingRecordDao;
import com.parkingManagement.model.ParkingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для управления записями о парковке в системе управления парковкой.
 */
@Service
public class ParkingRecordService {
    private final ParkingRecordDao parkingRecordDao;

    /**
     * Конструктор сервиса записей о парковке.
     
     * @param parkingRecordDao DAO для доступа к данным записей о парковке
     */
    @Autowired
    public ParkingRecordService(ParkingRecordDao parkingRecordDao) {
        this.parkingRecordDao = parkingRecordDao;
    }

    /**
     * Создаёт новую запись о парковке с проверкой данных.
     
     * @param record запись о парковке для создания
     * @throws IllegalArgumentException при некорректных данных
     */
    public void createParkingRecord(ParkingRecord record) {
        validateParkingRecord(record, false);
        parkingRecordDao.create(record);
    }

    /**
     * Находит запись о парковке по идентификатору.
     
     * @param id идентификатор записи
     * @return запись о парковке
     * @throws IllegalArgumentException если запись не найдена
     */
    public ParkingRecord getParkingRecord(Long id) {
        validateId(id, "Идентификатор записи");
        ParkingRecord record = parkingRecordDao.findById(id);
        if (record == null) {
            throw new IllegalArgumentException("Запись с ID " + id + " не найдена");
        }
        return record;
    }

    /**
     * Возвращает список всех записей о парковке.
     
     * @return список записей о парковке
     */
    public List<ParkingRecord> getAllParkingRecords() {
        return parkingRecordDao.findAll();
    }

    /**
     * Обновляет данные записи о парковке.
     
     * @param record запись о парковке для обновления
     * @throws IllegalArgumentException если запись не найдена
     */
    public void updateParkingRecord(ParkingRecord record) {
        validateParkingRecord(record, true);
        if (!parkingRecordDao.update(record)) {
            throw new IllegalArgumentException("Запись с ID " + record.getId() + " не найдена");
        }
    }

    /**
     * Удаляет запись о парковке по идентификатору.
     
     * @param id идентификатор записи
     * @throws IllegalArgumentException если запись не найдена
     */
    public void deleteParkingRecord(Long id) {
        validateId(id, "Идентификатор записи");
        if (!parkingRecordDao.delete(id)) {
            throw new IllegalArgumentException("Запись с ID " + id + " не найдена");
        }
    }

    /**
     * Проверяет корректность данных записи о парковке.
     
     * @param record запись для проверки
     * @param isUpdate флаг, указывающий, является ли операция обновлением
     * @throws IllegalArgumentException при некорректных данных
     */
    private void validateParkingRecord(ParkingRecord record, boolean isUpdate) {
        if (record == null) {
            throw new IllegalArgumentException("Запись не может быть null");
        }
        if (isUpdate && (record.getId() == null || record.getId() <= 0)) {
            throw new IllegalArgumentException("Идентификатор записи должен быть положительным");
        }
        if (record.getParkingSpace() == null || record.getParkingSpace().getId() == null || record.getParkingSpace().getId() <= 0) {
            throw new IllegalArgumentException("Идентификатор места должен быть положительным");
        }
        if (record.getVehicle() == null || record.getVehicle().getId() == null || record.getVehicle().getId() <= 0) {
            throw new IllegalArgumentException("Идентификатор автомобиля должен быть положительным");
        }
        if (record.getClient() == null || record.getClient().getId() == null || record.getClient().getId() <= 0) {
            throw new IllegalArgumentException("Идентификатор клиента должен быть положительным");
        }
        if (record.getEntryTime() == null) {
            throw new IllegalArgumentException("Время въезда обязательно");
        }
    }

    /**
     * Проверяет корректность идентификатора.
     
     * @param id идентификатор
     * @param field название поля для сообщения об ошибке
     * @throws IllegalArgumentException при некорректном идентификаторе
     */
    private void validateId(Long id, String field) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(field + " должен быть положительным");
        }
    }
}