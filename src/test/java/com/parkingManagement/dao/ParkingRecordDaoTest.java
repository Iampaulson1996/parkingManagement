package com.parkingManagement.dao;

import com.parkingManagement.model.Client;
import com.parkingManagement.model.ParkingLot;
import com.parkingManagement.model.ParkingSpace;
import com.parkingManagement.model.Vehicle;
import com.parkingManagement.model.ParkingRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Класс для тестирования операций DAO для сущности ParkingRecord с использованием методологии AAA.
 */
class ParkingRecordDaoTest {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private ParkingRecordDao parkingRecordDao;
    private ClientDao clientDao;
    private ParkingLotDao parkingLotDao;
    private ParkingSpaceDao parkingSpaceDao;
    private VehicleDao vehicleDao;

    /**
     * Инициализирует EntityManagerFactory и EntityManager перед всеми тестами.
     */
    @BeforeAll
    static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("parkingPUTest");
        em = emf.createEntityManager();
    }

    /**
     * Закрывает EntityManager и EntityManagerFactory после всех тестов.
     */
    @AfterAll
    static void tearDownClass() {
        if (em != null) em.close();
        if (emf != null) emf.close();
    }

    /**
     * Подготавливает DAO и очищает базу данных перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        parkingRecordDao = new ParkingRecordDao(em);
        clientDao = new ClientDao(em);
        parkingLotDao = new ParkingLotDao(em);
        parkingSpaceDao = new ParkingSpaceDao(em);
        vehicleDao = new VehicleDao(em);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM ParkingRecord").executeUpdate();
        em.createQuery("DELETE FROM Vehicle").executeUpdate();
        em.createQuery("DELETE FROM ParkingSpace").executeUpdate();
        em.createQuery("DELETE FROM ParkingLot").executeUpdate();
        em.createQuery("DELETE FROM Client").executeUpdate();
        em.getTransaction().commit();
    }

    @DisplayName("Создание новой записи о парковке в базе данных")
    @Test
    void testCreateParkingRecord() {
        // Подготовка
        Client client = new Client(null, "Иван Иванов", "+79123456789", "ivan@example.com");
        clientDao.create(client);

        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);

        ParkingSpace space = new ParkingSpace(null, lot, "A1", "REGULAR");
        parkingSpaceDao.create(space);

        Vehicle vehicle = new Vehicle(null, client, "АВС123", "Toyota", "Camry");
        vehicleDao.create(vehicle);

        ParkingRecord record = new ParkingRecord(null, space, vehicle, client, LocalDateTime.now(), null);

        // Действие
        parkingRecordDao.create(record);

        // Проверка
        ParkingRecord saved = parkingRecordDao.findById(record.getId());
        assertNotNull(saved, "Запись о парковке должна быть сохранена");
        assertEquals(space.getId(), saved.getParkingSpace().getId(), "Идентификатор места должен совпадать");
        assertEquals(vehicle.getId(), saved.getVehicle().getId(), "Идентификатор автомобиля должен совпадать");
        assertEquals(client.getId(), saved.getClient().getId(), "Идентификатор клиента должен совпадать");
        assertNotNull(saved.getEntryTime(), "Время въезда должно быть установлено");
    }

    @DisplayName("Поиск записи о парковке по существующему идентификатору")
    @Test
    void testFindByIdWhenParkingRecordExists() {
        // Подготовка
        Client client = new Client(null, "Анна Смирнова", "+79087654321", "anna@example.com");
        clientDao.create(client);

        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);

        ParkingSpace space = new ParkingSpace(null, lot, "B2", "VIP");
        parkingSpaceDao.create(space);

        Vehicle vehicle = new Vehicle(null, client, "XYZ789", "Honda", "Civic");
        vehicleDao.create(vehicle);

        ParkingRecord record = new ParkingRecord(null, space, vehicle, client, LocalDateTime.now(), null);
        parkingRecordDao.create(record);

        // Действие
        ParkingRecord found = parkingRecordDao.findById(record.getId());

        // Проверка
        assertNotNull(found, "Запись о парковке должна быть найдена");
        assertEquals(record.getId(), found.getId(), "Идентификатор записи должен совпадать");
    }

    @DisplayName("Поиск записи о парковке по несуществующему идентификатору")
    @Test
    void testFindByIdWhenParkingRecordNotExists() {
        // Подготовка
        Long nonExistentId = 999L;

        // Действие
        ParkingRecord found = parkingRecordDao.findById(nonExistentId);

        // Проверка
        assertNull(found, "Запись о парковке не должна быть найдена");
    }

    @DisplayName("Получение списка всех записей о парковке")
    @Test
    void testFindAll() {
        // Подготовка
        Client client = new Client(null, "Пётр Петров", "+79111111111", "petr@example.com");
        clientDao.create(client);

        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);

        ParkingSpace space = new ParkingSpace(null, lot, "C1", "REGULAR");
        parkingSpaceDao.create(space);

        Vehicle vehicle = new Vehicle(null, client, "DEF456", "Ford", "Focus");
        vehicleDao.create(vehicle);

        ParkingRecord record1 = new ParkingRecord(null, space, vehicle, client, LocalDateTime.now(), null);
        ParkingRecord record2 = new ParkingRecord(null, space, vehicle, client, LocalDateTime.now().minusHours(1), null);
        parkingRecordDao.create(record1);
        parkingRecordDao.create(record2);

        // Действие
        List<ParkingRecord> records = parkingRecordDao.findAll();

        // Проверка
        assertEquals(2, records.size(), "Должно быть найдено две записи о парковке");
    }

    @DisplayName("Обновление существующей записи о парковке")
    @Test
    void testUpdateParkingRecord() {
        // Подготовка
        Client client = new Client(null, "Старый клиент", "+79123456789", "old@example.com");
        clientDao.create(client);

        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);

        ParkingSpace space = new ParkingSpace(null, lot, "D1", "REGULAR");
        parkingSpaceDao.create(space);

        Vehicle vehicle = new Vehicle(null, client, "JKL012", "Nissan", "Altima");
        vehicleDao.create(vehicle);

        ParkingRecord record = new ParkingRecord(null, space, vehicle, client, LocalDateTime.now(), null);
        parkingRecordDao.create(record);

        Client newClient = new Client(null, "Новый клиент", "+79987654321", "new@example.com");
        clientDao.create(newClient);

        ParkingSpace newSpace = new ParkingSpace(null, lot, "D2", "VIP");
        parkingSpaceDao.create(newSpace);

        Vehicle newVehicle = new Vehicle(null, newClient, "MNO345", "Tesla", "Model 3");
        vehicleDao.create(newVehicle);

        ParkingRecord updatedRecord = new ParkingRecord(record.getId(), newSpace, newVehicle, newClient,
                LocalDateTime.now().minusHours(2), LocalDateTime.now());

        // Действие
        boolean result = parkingRecordDao.update(updatedRecord);

        // Проверка
        assertTrue(result, "Обновление должно быть успешным");
        ParkingRecord saved = parkingRecordDao.findById(record.getId());
        assertEquals(newSpace.getId(), saved.getParkingSpace().getId(), "Идентификатор места должен быть обновлён");
        assertEquals(newVehicle.getId(), saved.getVehicle().getId(), "Идентификатор автомобиля должен быть обновлён");
        assertEquals(newClient.getId(), saved.getClient().getId(), "Идентификатор клиента должен быть обновлён");
        assertNotNull(saved.getExitTime(), "Время выезда должно быть установлено");
    }

    @DisplayName("Попытка обновления несуществующей записи о парковке")
    @Test
    void testUpdateNonExistentParkingRecord() {
        // Подготовка
        Client client = new Client(null, "Клиент", "+79123456789", "client@example.com");
        clientDao.create(client);

        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);

        ParkingSpace space = new ParkingSpace(null, lot, "E1", "REGULAR");
        parkingSpaceDao.create(space);

        Vehicle vehicle = new Vehicle(null, client, "PQR678", "Kia", "Sportage");
        vehicleDao.create(vehicle);

        ParkingRecord record = new ParkingRecord(999L, space, vehicle, client, LocalDateTime.now(), null);

        // Действие
        boolean result = parkingRecordDao.update(record);

        // Проверка
        assertFalse(result, "Обновление несуществующей записи должно вернуть false");
    }

    @DisplayName("Удаление существующей записи о парковке")
    @Test
    void testDeleteParkingRecord() {
        // Подготовка
        Client client = new Client(null, "Для удаления", "+79123456789", "delete@example.com");
        clientDao.create(client);

        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);

        ParkingSpace space = new ParkingSpace(null, lot, "F1", "REGULAR");
        parkingSpaceDao.create(space);

        Vehicle vehicle = new Vehicle(null, client, "STU901", "Hyundai", "Tucson");
        vehicleDao.create(vehicle);

        ParkingRecord record = new ParkingRecord(null, space, vehicle, client, LocalDateTime.now(), null);
        parkingRecordDao.create(record);

        // Действие
        boolean result = parkingRecordDao.delete(record.getId());

        // Проверка
        assertTrue(result, "Удаление должно быть успешным");
        assertNull(parkingRecordDao.findById(record.getId()), "Запись не должна быть найдена после удаления");
    }

    @DisplayName("Попытка удаления несуществующей записи о парковке")
    @Test
    void testDeleteNonExistentParkingRecord() {
        // Подготовка
        Long nonExistentId = 999L;

        // Действие
        boolean result = parkingRecordDao.delete(nonExistentId);

        // Проверка
        assertFalse(result, "Удаление несуществующей записи должно вернуть false");
    }
}