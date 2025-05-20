package com.parkingManagement.service;

import com.parkingManagement.dao.ParkingLotDao;
import com.parkingManagement.dao.ParkingSpaceDao;
import com.parkingManagement.model.ParkingLot;
import com.parkingManagement.model.ParkingSpace;
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
 * Класс для тестирования операций сервиса ParkingSpaceService с использованием методологии AAA.
 */
class ParkingSpaceServiceTest {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private ParkingSpaceDao parkingSpaceDao;
    private ParkingSpaceService parkingSpaceService;
    private ParkingLotDao parkingLotDao;

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
        parkingSpaceDao = new ParkingSpaceDao(em);
        parkingSpaceService = new ParkingSpaceService(parkingSpaceDao);
        parkingLotDao = new ParkingLotDao(em);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM ParkingRecord").executeUpdate();
        em.createQuery("DELETE FROM ParkingSpace").executeUpdate();
        em.createQuery("DELETE FROM ParkingLot").executeUpdate();
        em.getTransaction().commit();
    }

    @DisplayName("Создание нового парковочного места с корректными данными")
    @Test
    void testCreateParkingSpaceSuccess() {
        // Подготовка
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(null, lot, "A1", "REGULAR");

        // Действие
        parkingSpaceService.createParkingSpace(space);

        // Проверка
        ParkingSpace saved = parkingSpaceDao.findById(space.getId());
        assertNotNull(saved, "Парковочное место должно быть сохранено");
        assertEquals("A1", saved.getSpaceNumber(), "Номер места должен совпадать");
        assertEquals("REGULAR", saved.getType(), "Тип места должен совпадать");
        assertEquals(lot.getId(), saved.getParkingLot().getId(), "Идентификатор парковки должен совпадать");
    }

    @DisplayName("Создание парковочного места с некорректным типом")
    @Test
    void testCreateParkingSpaceWithInvalidType() {
        // Подготовка
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(null, lot, "A1", "INVALID");

        // Действие
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parkingSpaceService.createParkingSpace(space));

        // Проверка
        assertEquals("Тип места должен быть REGULAR, DISABLED или VIP", exception.getMessage());
    }

    @DisplayName("Получение парковочного места по существующему идентификатору")
    @Test
    void testGetParkingSpaceSuccess() {
        // Подготовка
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(null, lot, "B2", "VIP");
        parkingSpaceDao.create(space);

        // Действие
        ParkingSpace found = parkingSpaceService.getParkingSpace(space.getId());

        // Проверка
        assertNotNull(found, "Парковочное место должно быть найдено");
        assertEquals(space.getId(), found.getId(), "Идентификатор места должен совпадать");
    }

    @DisplayName("Получение парковочного места по несуществующему идентификатору")
    @Test
    void testGetParkingSpaceNotFound() {
        // Действие
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parkingSpaceService.getParkingSpace(999L));

        // Проверка
        assertEquals("Место с ID 999 не найдено", exception.getMessage());
    }

    @DisplayName("Получение списка всех парковочных мест")
    @Test
    void testGetAllParkingSpaces() {
        // Подготовка
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space1 = new ParkingSpace(null, lot, "C1", "REGULAR");
        ParkingSpace space2 = new ParkingSpace(null, lot, "C2", "DISABLED");
        parkingSpaceDao.create(space1);
        parkingSpaceDao.create(space2);

        // Действие
        List<ParkingSpace> spaces = parkingSpaceService.getAllParkingSpaces();

        // Проверка
        assertEquals(2, spaces.size(), "Должно быть найдено два парковочных места");
        assertTrue(spaces.stream().anyMatch(s -> s.getSpaceNumber().equals("C1")),
                "Список должен содержать место C1");
        assertTrue(spaces.stream().anyMatch(s -> s.getSpaceNumber().equals("C2")),
                "Список должен содержать место C2");
    }

    @DisplayName("Обновление существующего парковочного места")
    @Test
    void testUpdateParkingSpaceSuccess() {
        // Подготовка
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(null, lot, "D1", "REGULAR");
        parkingSpaceDao.create(space);
        ParkingSpace updatedSpace = new ParkingSpace(space.getId(), lot, "D2", "VIP");

        // Действие
        parkingSpaceService.updateParkingSpace(updatedSpace);

        // Проверка
        ParkingSpace saved = parkingSpaceDao.findById(space.getId());
        assertEquals("D2", saved.getSpaceNumber(), "Номер места должен быть обновлён");
        assertEquals("VIP", saved.getType(), "Тип места должен быть обновлён");
    }

    @DisplayName("Попытка обновления несуществующего парковочного места")
    @Test
    void testUpdateParkingSpaceNotFound() {
        // Подготовка
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(999L, lot, "E1", "REGULAR");

        // Действие
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parkingSpaceService.updateParkingSpace(space));

        // Проверка
        assertEquals("Место с ID 999 не найдено", exception.getMessage());
    }

    @DisplayName("Удаление существующего парковочного места")
    @Test
    void testDeleteParkingSpaceSuccess() {
        // Подготовка
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(null, lot, "F1", "REGULAR");
        parkingSpaceDao.create(space);

        // Действие
        parkingSpaceService.deleteParkingSpace(space.getId());

        // Проверка
        assertNull(parkingSpaceDao.findById(space.getId()), "Место не должно быть найдено после удаления");
    }

    @DisplayName("Попытка удаления несуществующего парковочного места")
    @Test
    void testDeleteParkingSpaceNotFound() {
        // Действие
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parkingSpaceService.deleteParkingSpace(999L));

        // Проверка
        assertEquals("Место с ID 999 не найдено", exception.getMessage());
    }
}