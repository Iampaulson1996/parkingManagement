package com.parkingManagement.dao;

import com.parkingManagement.model.ParkingLot;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Класс для тестирования операций DAO для сущности ParkingLot с использованием методологии AAA.
 */
class ParkingLotDaoTest {
    private static EntityManagerFactory emf;
    private static EntityManager em;
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
        parkingLotDao = new ParkingLotDao(em);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM ParkingSpace").executeUpdate();
        em.createQuery("DELETE FROM ParkingLot").executeUpdate();
        em.getTransaction().commit();
    }

    @DisplayName("Создание новой парковки")
    @Test
    void testCreateParkingLot() {
        // Подготовка
        ParkingLot lot = new ParkingLot(null, "Центральная парковка", "ул. Главная, 123", 100);

        // Действие
        parkingLotDao.create(lot);

        // Проверка
        ParkingLot saved = parkingLotDao.findById(lot.getId());
        assertNotNull(saved, "Парковка должна быть сохранена");
        assertEquals("Центральная парковка", saved.getName(), "Название парковки должно совпадать");
        assertEquals("ул. Главная, 123", saved.getAddress(), "Адрес парковки должен совпадать");
        assertEquals(100, saved.getCapacity(), "Вместимость парковки должна совпадать");
    }

    @DisplayName("Поиск парковки по существующему идентификатору")
    @Test
    void testFindByIdWhenParkingLotExists() {
        // Подготовка
        ParkingLot lot = new ParkingLot(null, "Городская парковка", "ул. Дубовая, 456", 50);
        parkingLotDao.create(lot);

        // Действие
        ParkingLot found = parkingLotDao.findById(lot.getId());

        // Проверка
        assertNotNull(found, "Парковка должна быть найдена");
        assertEquals(lot.getId(), found.getId(), "Идентификатор парковки должен совпадать");
    }

    @DisplayName("Поиск парковки по несуществующему идентификатору")
    @Test
    void testFindByIdWhenParkingLotNotExists() {
        // Подготовка
        Long nonExistentId = 999L;

        // Действие
        ParkingLot found = parkingLotDao.findById(nonExistentId);

        // Проверка
        assertNull(found, "Парковка не должна быть найдена");
    }

    @DisplayName("Получение списка всех парковок")
    @Test
    void testFindAll() {
        // Подготовка
        ParkingLot lot1 = new ParkingLot(null, "Парковка 1", "ул. Сосновая, 111", 20);
        ParkingLot lot2 = new ParkingLot(null, "Парковка 2", "ул. Еловая, 222", 30);
        parkingLotDao.create(lot1);
        parkingLotDao.create(lot2);

        // Действие
        List<ParkingLot> lots = parkingLotDao.findAll();

        // Проверка
        assertEquals(2, lots.size(), "Должно быть найдено две парковки");
        assertTrue(lots.stream().anyMatch(l -> l.getName().equals("Парковка 1")), "Список должен содержать Парковку 1");
        assertTrue(lots.stream().anyMatch(l -> l.getName().equals("Парковка 2")), "Список должен содержать Парковку 2");
    }

    @DisplayName("Обновление существующей парковки")
    @Test
    void testUpdateParkingLot() {
        // Подготовка
        ParkingLot lot = new ParkingLot(null, "Старое название", "Старый адрес", 50);
        parkingLotDao.create(lot);
        ParkingLot updatedLot = new ParkingLot(lot.getId(), "Новое название", "Новый адрес", 75);

        // Действие
        boolean result = parkingLotDao.update(updatedLot);

        // Проверка
        assertTrue(result, "Обновление должно быть успешным");
        ParkingLot saved = parkingLotDao.findById(lot.getId());
        assertEquals("Новое название", saved.getName(), "Название парковки должно быть обновлено");
        assertEquals("Новый адрес", saved.getAddress(), "Адрес парковки должен быть обновлён");
        assertEquals(75, saved.getCapacity(), "Вместимость парковки должна быть обновлена");
    }

    @DisplayName("Попытка обновления несуществующей парковки")
    @Test
    void testUpdateNonExistentParkingLot() {
        // Подготовка
        ParkingLot lot = new ParkingLot(999L, "Несуществующая", "Нет адреса", 10);

        // Действие
        boolean result = parkingLotDao.update(lot);

        // Проверка
        assertFalse(result, "Обновление несуществующей парковки должно вернуть false");
    }

    @DisplayName("Удаление существующей парковки")
    @Test
    void testDeleteParkingLot() {
        // Подготовка
        ParkingLot lot = new ParkingLot(null, "Для удаления", "ул. Удаляемая, 123", 40);
        parkingLotDao.create(lot);

        // Действие
        boolean result = parkingLotDao.delete(lot.getId());

        // Проверка
        assertTrue(result, "Удаление должно быть успешным");
        assertNull(parkingLotDao.findById(lot.getId()), "Парковка не должна быть найдена после удаления");
    }

    @DisplayName("Попытка удаления несуществующей парковки")
    @Test
    void testDeleteNonExistentParkingLot() {
        // Подготовка
        Long nonExistentId = 999L;

        // Действие
        boolean result = parkingLotDao.delete(nonExistentId);

        // Проверка
        assertFalse(result, "Удаление несуществующей парковки должно вернуть false");
    }
}