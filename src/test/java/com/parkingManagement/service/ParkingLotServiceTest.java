package com.parkingManagement.service;

import com.parkingManagement.dao.ParkingLotDao;
import com.parkingManagement.model.ParkingLot;
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
 * Класс для тестирования операций сервиса ParkingLotService с использованием методологии AAA.
 */
class ParkingLotServiceTest {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private ParkingLotDao parkingLotDao;
    private ParkingLotService parkingLotService;

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
        parkingLotDao = new ParkingLotDao(em);
        parkingLotService = new ParkingLotService(parkingLotDao);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM ParkingSpace").executeUpdate();
        em.createQuery("DELETE FROM ParkingLot").executeUpdate();
        em.getTransaction().commit();
    }

    /**
     * Тестирует создание новой парковки с корректными данными.
     */
    @Test
    void testCreateParkingLotSuccess() {
        ParkingLot lot = new ParkingLot(null, "Центральная парковка", "ул. Главная, 123", 100);
        parkingLotService.createParkingLot(lot);
        ParkingLot saved = parkingLotDao.findById(lot.getId());
        assertNotNull(saved, "Парковка должна быть сохранена");
        assertEquals("Центральная парковка", saved.getName(), "Название парковки должно совпадать");
        assertEquals("ул. Главная, 123", saved.getAddress(), "Адрес парковки должен совпадать");
        assertEquals(100, saved.getCapacity(), "Вместимость парковки должна совпадать");
    }

    /**
     * Тестирует создание парковки с некорректными данными (null название).
     */
    @Test
    void testCreateParkingLotWithInvalidData() {
        ParkingLot lot = new ParkingLot(null, null, "ул. Главная, 123", 100);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parkingLotService.createParkingLot(lot));
        assertEquals("Название парковки обязательно", exception.getMessage());
    }

    /**
     * Тестирует получение парковки по существующему идентификатору.
     */
    @Test
    void testGetParkingLotSuccess() {
        ParkingLot lot = new ParkingLot(null, "Городская парковка", "ул. Дубовая, 456", 50);
        parkingLotDao.create(lot);
        ParkingLot found = parkingLotService.getParkingLot(lot.getId());
        assertNotNull(found, "Парковка должна быть найдена");
        assertEquals(lot.getId(), found.getId(), "Идентификатор парковки должен совпадать");
    }

    /**
     * Тестирует получение парковки по несуществующему идентификатору.
     */
    @Test
    void testGetParkingLotNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parkingLotService.getParkingLot(999L));
        assertEquals("Парковка с ID 999 не найдена", exception.getMessage());
    }

    /**
     * Тестирует получение списка всех парковок.
     */
    @Test
    void testGetAllParkingLots() {
        ParkingLot lot1 = new ParkingLot(null, "Парковка 1", "ул. Сосновая, 111", 20);
        ParkingLot lot2 = new ParkingLot(null, "Парковка 2", "ул. Еловая, 222", 30);
        parkingLotDao.create(lot1);
        parkingLotDao.create(lot2);
        List<ParkingLot> lots = parkingLotService.getAllParkingLots();
        assertEquals(2, lots.size(), "Должно быть найдено две парковки");
        assertTrue(lots.stream().anyMatch(l -> l.getName().equals("Парковка 1")),
                "Список должен содержать Парковку 1");
        assertTrue(lots.stream().anyMatch(l -> l.getName().equals("Парковка 2")),
                "Список должен содержать Парковку 2");
    }

    /**
     * Тестирует обновление существующей парковки.
     */
    @Test
    void testUpdateParkingLotSuccess() {
        ParkingLot lot = new ParkingLot(null, "Старое название", "Старый адрес", 50);
        parkingLotDao.create(lot);
        ParkingLot updatedLot = new ParkingLot(lot.getId(), "Новое название", "Новый адрес", 75);
        parkingLotService.updateParkingLot(updatedLot);
        ParkingLot saved = parkingLotDao.findById(lot.getId());
        assertEquals("Новое название", saved.getName(), "Название парковки должно быть обновлено");
        assertEquals("Новый адрес", saved.getAddress(), "Адрес парковки должен быть обновлён");
        assertEquals(75, saved.getCapacity(), "Вместимость парковки должна быть обновлена");
    }

    /**
     * Тестирует попытку обновления несуществующей парковки.
     */
    @Test
    void testUpdateParkingLotNotFound() {
        ParkingLot lot = new ParkingLot(999L, "Несуществующая", "Нет адреса", 10);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parkingLotService.updateParkingLot(lot));
        assertEquals("Парковка с ID 999 не найдена", exception.getMessage());
    }

    /**
     * Тестирует удаление существующей парковки.
     */
    @Test
    void testDeleteParkingLotSuccess() {
        ParkingLot lot = new ParkingLot(null, "Для удаления", "ул. Удаляемая, 123", 40);
        parkingLotDao.create(lot);
        parkingLotService.deleteParkingLot(lot.getId());
        assertNull(parkingLotDao.findById(lot.getId()), "Парковка не должна быть найдена после удаления");
    }

    /**
     * Тестирует попытку удаления несуществующей парковки.
     */
    @Test
    void testDeleteParkingLotNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parkingLotService.deleteParkingLot(999L));
        assertEquals("Парковка с ID 999 не найдена", exception.getMessage());
    }
}