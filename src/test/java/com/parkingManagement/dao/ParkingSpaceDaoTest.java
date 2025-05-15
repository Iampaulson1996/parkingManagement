package com.parkingManagement.dao;

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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Класс для тестирования операций DAO для сущности ParkingSpace с использованием методологии AAA.
 */
class ParkingSpaceDaoTest {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private ParkingSpaceDao parkingSpaceDao;
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
     * Подготавливает DAO и очищает базу данных перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        parkingSpaceDao = new ParkingSpaceDao(em);
        parkingLotDao = new ParkingLotDao(em);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM ParkingRecord").executeUpdate();
        em.createQuery("DELETE FROM ParkingSpace").executeUpdate();
        em.createQuery("DELETE FROM ParkingLot").executeUpdate();
        em.getTransaction().commit();
    }

    /**
     * Тестирует создание нового парковочного места в базе данных.
     */
    @Test
    void testCreateParkingSpace() {
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(null, lot, "A1", "REGULAR");
        parkingSpaceDao.create(space);
        ParkingSpace saved = parkingSpaceDao.findById(space.getId());
        assertNotNull(saved, "Парковочное место должно быть сохранено");
        assertEquals("A1", saved.getSpaceNumber(), "Номер места должен совпадать");
        assertEquals("REGULAR", saved.getType(), "Тип места должен совпадать");
        assertEquals(lot.getId(), saved.getParkingLot().getId(), "Идентификатор парковки должен совпадать");
    }

    /**
     * Тестирует поиск парковочного места по существующему идентификатору.
     */
    @Test
    void testFindByIdWhenParkingSpaceExists() {
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(null, lot, "B2", "VIP");
        parkingSpaceDao.create(space);
        ParkingSpace found = parkingSpaceDao.findById(space.getId());
        assertNotNull(found, "Парковочное место должно быть найдено");
        assertEquals(space.getId(), found.getId(), "Идентификатор места должен совпадать");
    }

    /**
     * Тестирует поиск парковочного места по несуществующему идентификатору.
     */
    @Test
    void testFindByIdWhenParkingSpaceNotExists() {
        Long nonExistentId = 999L;
        ParkingSpace found = parkingSpaceDao.findById(nonExistentId);
        assertNull(found, "Парковочное место не должно быть найдено");
    }

    /**
     * Тестирует получение списка всех парковочных мест.
     */
    @Test
    void testFindAll() {
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space1 = new ParkingSpace(null, lot, "C1", "REGULAR");
        ParkingSpace space2 = new ParkingSpace(null, lot, "C2", "DISABLED");
        parkingSpaceDao.create(space1);
        parkingSpaceDao.create(space2);
        List<ParkingSpace> spaces = parkingSpaceDao.findAll();
        assertEquals(2, spaces.size(), "Должно быть найдено два парковочных места");
        assertTrue(spaces.stream().anyMatch(s -> s.getSpaceNumber().equals("C1")), "Список должен содержать место C1");
        assertTrue(spaces.stream().anyMatch(s -> s.getSpaceNumber().equals("C2")), "Список должен содержать место C2");
    }

    /**
     * Тестирует обновление существующего парковочного места.
     */
    @Test
    void testUpdateParkingSpace() {
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(null, lot, "D1", "REGULAR");
        parkingSpaceDao.create(space);
        ParkingSpace updatedSpace = new ParkingSpace(space.getId(), lot, "D2", "VIP");
        boolean result = parkingSpaceDao.update(updatedSpace);
        assertTrue(result, "Обновление должно быть успешным");
        ParkingSpace saved = parkingSpaceDao.findById(space.getId());
        assertEquals("D2", saved.getSpaceNumber(), "Номер места должен быть обновлён");
        assertEquals("VIP", saved.getType(), "Тип места должен быть обновлён");
    }

    /**
     * Тестирует попытку обновления несуществующего парковочного места.
     */
    @Test
    void testUpdateNonExistentParkingSpace() {
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(999L, lot, "E1", "REGULAR");
        boolean result = parkingSpaceDao.update(space);
        assertFalse(result, "Обновление несуществующего места должно вернуть false");
    }

    /**
     * Тестирует удаление существующего парковочного места.
     */
    @Test
    void testDeleteParkingSpace() {
        ParkingLot lot = new ParkingLot(null, "Тестовая парковка", "ул. Тестовая, 123", 10);
        parkingLotDao.create(lot);
        ParkingSpace space = new ParkingSpace(null, lot, "F1", "REGULAR");
        parkingSpaceDao.create(space);
        boolean result = parkingSpaceDao.delete(space.getId());
        assertTrue(result, "Удаление должно быть успешным");
        assertNull(parkingSpaceDao.findById(space.getId()), "Место не должно быть найдено после удаления");
    }

    /**
     * Тестирует попытку удаления несуществующего парковочного места.
     */
    @Test
    void testDeleteNonExistentParkingSpace() {
        Long nonExistentId = 999L;
        boolean result = parkingSpaceDao.delete(nonExistentId);
        assertFalse(result, "Удаление несуществующего места должно вернуть false");
    }
}