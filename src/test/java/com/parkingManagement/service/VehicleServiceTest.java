package com.parkingManagement.service;

import com.parkingManagement.dao.ClientDao;
import com.parkingManagement.dao.VehicleDao;
import com.parkingManagement.model.Client;
import com.parkingManagement.model.Vehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Класс для тестирования операций сервиса VehicleService с использованием методологии AAA.
 */
class VehicleServiceTest {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private VehicleDao vehicleDao;
    private VehicleService vehicleService;
    private ClientDao clientDao;

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
     * Подготавливает DAO, сервис и очищает базу данных перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        vehicleDao = new VehicleDao(em);
        vehicleService = new VehicleService(vehicleDao);
        clientDao = new ClientDao(em);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM ParkingRecord").executeUpdate();
        em.createQuery("DELETE FROM Vehicle").executeUpdate();
        em.createQuery("DELETE FROM Client").executeUpdate();
        em.getTransaction().commit();
    }

    @DisplayName("Создание нового автомобиля с корректными данными")
    @Test
    void testCreateVehicleSuccess() {
        // Подготовка
        Client client = new Client(null, "Иван Иванов", "+79123456789", "ivan@example.com");
        clientDao.create(client);
        Vehicle vehicle = new Vehicle(null, client, "АВС123", "Toyota", "Camry");

        // Действие
        vehicleService.createVehicle(vehicle);

        // Проверка
        Vehicle saved = vehicleDao.findById(vehicle.getId());
        assertNotNull(saved, "Автомобиль должен быть сохранён");
        assertEquals("АВС123", saved.getLicensePlate(), "Регистрационный номер должен совпадать");
        assertEquals("Toyota", saved.getBrand(), "Марка автомобиля должна совпадать");
        assertEquals("Camry", saved.getModel(), "Модель автомобиля должна совпадать");
        assertEquals(client.getId(), saved.getClient().getId(), "Идентификатор клиента должен совпадать");
    }

    @DisplayName("Создание автомобиля с некорректными данными (null номер)")
    @Test
    void testCreateVehicleWithInvalidData() {
        // Подготовка
        Client client = new Client(null, "Иван Иванов", "+79123456789", "ivan@example.com");
        clientDao.create(client);
        Vehicle vehicle = new Vehicle(null, client, null, "Toyota", "Camry");

        // Действие
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> vehicleService.createVehicle(vehicle));

        // Проверка
        assertEquals("Регистрационный номер обязателен", exception.getMessage());
    }

    @DisplayName("Получение автомобиля по существующему идентификатору")
    @Test
    void testGetVehicleSuccess() {
        // Подготовка
        Client client = new Client(null, "Анна Смирнова", "+79087654321", "anna@example.com");
        clientDao.create(client);
        Vehicle vehicle = new Vehicle(null, client, "XYZ789", "Honda", "Civic");
        vehicleDao.create(vehicle);

        // Действие
        Vehicle found = vehicleService.getVehicle(vehicle.getId());

        // Проверка
        assertNotNull(found, "Автомобиль должен быть найден");
        assertEquals(vehicle.getId(), found.getId(), "Идентификатор автомобиля должен совпадать");
    }

    @DisplayName("Получение автомобиля по несуществующему идентификатору")
    @Test
    void testGetVehicleNotFound() {
        // Действие
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> vehicleService.getVehicle(999L));

        // Проверка
        assertEquals("Автомобиль с ID 999 не найден", exception.getMessage());
    }

    @DisplayName("Получение списка всех автомобилей")
    @Test
    void testGetAllVehicles() {
        // Подготовка
        Client client = new Client(null, "Пётр Петров", "+79111111111", "petr@example.com");
        clientDao.create(client);
        Vehicle vehicle1 = new Vehicle(null, client, "DEF456", "Ford", "Focus");
        Vehicle vehicle2 = new Vehicle(null, client, "GHI789", "BMW", "X5");
        vehicleDao.create(vehicle1);
        vehicleDao.create(vehicle2);

        // Действие
        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        // Проверка
        assertEquals(2, vehicles.size(), "Должно быть найдено два автомобиля");
        assertTrue(vehicles.stream().anyMatch(v -> v.getLicensePlate().equals("DEF456")),
                "Список должен содержать автомобиль DEF456");
        assertTrue(vehicles.stream().anyMatch(v -> v.getLicensePlate().equals("GHI789")),
                "Список должен содержать автомобиль GHI789");
    }

    @DisplayName("Обновление существующего автомобиля")
    @Test
    void testUpdateVehicleSuccess() {
        // Подготовка
        Client client = new Client(null, "Старый клиент", "+79123456789", "old@example.com");
        clientDao.create(client);
        Vehicle vehicle = new Vehicle(null, client, "JKL012", "Nissan", "Altima");
        vehicleDao.create(vehicle);
        Client newClient = new Client(null, "Новый клиент", "+79987654321", "new@example.com");
        clientDao.create(newClient);
        Vehicle updatedVehicle = new Vehicle(vehicle.getId(), newClient, "MNO345", "Tesla", "Model 3");

        // Действие
        vehicleService.updateVehicle(updatedVehicle);

        // Проверка
        Vehicle saved = vehicleDao.findById(vehicle.getId());
        assertEquals("MNO345", saved.getLicensePlate(), "Регистрационный номер должен быть обновлён");
        assertEquals("Tesla", saved.getBrand(), "Марка автомобиля должна быть обновлена");
        assertEquals("Model 3", saved.getModel(), "Модель автомобиля должна быть обновлена");
        assertEquals(newClient.getId(), saved.getClient().getId(), "Идентификатор клиента должен быть обновлён");
    }

    @DisplayName("Попытка обновления несуществующего автомобиля")
    @Test
    void testUpdateVehicleNotFound() {
        // Подготовка
        Client client = new Client(null, "Клиент", "+79123456789", "client@example.com");
        clientDao.create(client);
        Vehicle vehicle = new Vehicle(999L, client, "PQR678", "Kia", "Sportage");

        // Действие
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> vehicleService.updateVehicle(vehicle));

        // Проверка
        assertEquals("Автомобиль с ID 999 не найден", exception.getMessage());
    }

    @DisplayName("Удаление существующего автомобиля")
    @Test
    void testDeleteVehicleSuccess() {
        // Подготовка
        Client client = new Client(null, "Для удаления", "+79123456789", "delete@example.com");
        clientDao.create(client);
        Vehicle vehicle = new Vehicle(null, client, "STU901", "Hyundai", "Tucson");
        vehicleDao.create(vehicle);

        // Действие
        vehicleService.deleteVehicle(vehicle.getId());

        // Проверка
        assertNull(vehicleDao.findById(vehicle.getId()), "Автомобиль не должен быть найден после удаления");
    }

    @DisplayName("Попытка удаления несуществующего автомобиля")
    @Test
    void testDeleteVehicleNotFound() {
        // Действие
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> vehicleService.deleteVehicle(999L));

        // Проверка
        assertEquals("Автомобиль с ID 999 не найден", exception.getMessage());
    }
}