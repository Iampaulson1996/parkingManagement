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

    /**
     * Тестирует создание нового парковочного места с корректными данными.
     */
    @Test
    void testCreateParkingSpaceSuccess() {
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(null, lot, "A1", "REGULAR");
        parkingSpaceService.createParkingSpace(space);
        ParkingSpace saved = parkingSpaceDao.findById(space.getId());
        assertNotNull(saved, "Парковочное место должно быть сохранено");
        assertEquals("A1", saved.getSpaceNumber(), "Номер места должен совпадать");
        assertEquals("REGULAR", saved.getType(), "Тип места должен совпадать");
        assertEquals(lot.getId(), saved.getParkingLot().getId(), "Идентификатор парковки должен совпадать");
    }

    /**
     * Тестирует создание парковочного места с некорректным типом.
     */
    @Test
    void testCreateParkingSpaceWithInvalidType() {
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(null, lot, "A1", "INVALID");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parkingSpaceService.createParkingSpace(space));
        assertEquals("Тип места должен быть REGULAR, DISABLED или VIP", exception.getMessage());
    }

    /**
     * Тестирует получение парковочного места по существующему идентификатору.
     */
    @Test
    void testGetParkingSpaceSuccess() {
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(null, lot, "B2", "VIP");
        parkingSpaceDao.create(space);
        ParkingSpace found = parkingSpaceService.getParkingSpace(space.getId());
        assertNotNull(found, "Парковочное место должно быть найдено");
        assertEquals(space.getId(), found.getId(), "Идентификатор места должен совпадать");
    }

    /**
     * Тестирует получение парковочного места по несуществующему идентификатору.
     */
    @Test
    void testGetParkingSpaceNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parkingSpaceService.getParkingSpace(999L));
        assertEquals("Место с ID 999 не найдено", exception.getMessage());
    }

    /**
     * Тестирует получение списка всех парковочных мест.
     */
    @Test
    void testGetAllParkingSpaces() {
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space1 = new ParkingSpace(null, lot, "C1", "REGULAR");
        ParkingSpace space2 = new ParkingSpace(null, lot, "C2", "DISABLED");
        parkingSpaceDao.create(space1);
        parkingSpaceDao.create(space2);
        List<ParkingSpace> spaces = parkingSpaceService.getAllParkingSpaces();
        assertEquals(2, spaces.size(), "Должно быть найдено два парковочных места");
        assertTrue(spaces.stream().anyMatch(s -> s.getSpaceNumber().equals("C1")),
                "Список должен содержать место C1");
        assertTrue(spaces.stream().anyMatch(s -> s.getSpaceNumber().equals("C2")),
                "Список должен содержать место C2");
    }

    /**
     * Тестирует обновление существующего парковочного места.
     */
    @Test
    void testUpdateParkingSpaceSuccess() {
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(null, lot, "D1", "REGULAR");
        parkingSpaceDao.create(space);
        ParkingSpace updatedSpace = new ParkingSpace(space.getId(), lot, "D2", "VIP");
        parkingSpaceService.updateParkingSpace(updatedSpace);
        ParkingSpace saved = parkingSpaceDao.findById(space.getId());
        assertEquals("D2", saved.getSpaceNumber(), "Номер места должен быть обновлён");
        assertEquals("VIP", saved.getType(), "Тип места должен быть обновлён");
    }

    /**
     * Тестирует попытку обновления несуществующего парковочного места.
     */
    @Test
    void testUpdateParkingSpaceNotFound() {
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(999L, lot, "E1", "REGULAR");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parkingSpaceService.updateParkingSpace(space));
        assertEquals("Место с ID 999 не найдено", exception.getMessage());
    }

    /**
     * Тестирует удаление существующего парковочного места.
     */
    @Test
    void testDeleteParkingSpaceSuccess() {
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(null, lot, "F1", "REGULAR");
        parkingSpaceDao.create(space);
        parkingSpaceService.deleteParkingSpace(space.getId());
        assertNull(parkingSpaceDao.findById(space.getId()), "Место не должно быть найдено после удаления");
    }

    /**
     * Тестирует попытку удаления несуществующего парковочного места.
     */
    @Test
    void testDeleteParkingSpaceNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parkingSpaceService.deleteParkingSpace(999L));
        assertEquals("Место с ID 999 не найдено", exception.getMessage());
    }
}