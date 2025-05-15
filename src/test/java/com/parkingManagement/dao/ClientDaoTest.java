package com.parkingManagement.dao;

import com.parkingManagement.model.Client;
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
 * Класс для тестирования операций DAO для сущности Client с использованием методологии AAA.
 */
class ClientDaoTest {
    private static EntityManagerFactory emf;
    private static EntityManager em;
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
     * Подготавливает DAO и очищает базу данных перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        clientDao = new ClientDao(em);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM ParkingRecord").executeUpdate();
        em.createQuery("DELETE FROM Vehicle").executeUpdate();
        em.createQuery("DELETE FROM Client").executeUpdate();
        em.getTransaction().commit();
    }

    /**
     * Тестирует создание нового клиента в базе данных.
     */
    @Test
    void testCreateClient() {
        Client client = new Client(null, "Иван Иванов", "+79123456789", "ivan@example.com");
        clientDao.create(client);
        Client saved = clientDao.findById(client.getId());
        assertNotNull(saved, "Клиент должен быть сохранён");
        assertEquals("Иван Иванов", saved.getName(), "Имя клиента должно совпадать");
        assertEquals("+79123456789", saved.getPhone(), "Телефон клиента должен совпадать");
        assertEquals("ivan@example.com", saved.getEmail(), "Email клиента должен совпадать");
    }

    /**
     * Тестирует поиск клиента по существующему идентификатору.
     */
    @Test
    void testFindByIdWhenClientExists() {
        Client client = new Client(null, "Анна Смирнова", "+79087654321", "anna@example.com");
        clientDao.create(client);
        Client found = clientDao.findById(client.getId());
        assertNotNull(found, "Клиент должен быть найден");
        assertEquals(client.getId(), found.getId(), "Идентификатор клиента должен совпадать");
    }

    /**
     * Тестирует поиск клиента по несуществующему идентификатору.
     */
    @Test
    void testFindByIdWhenClientNotExists() {
        Long nonExistentId = 999L;
        Client found = clientDao.findById(nonExistentId);
        assertNull(found, "Клиент не должен быть найден");
    }

    /**
     * Тестирует получение списка всех клиентов.
     */
    @Test
    void testFindAll() {
        Client client1 = new Client(null, "Пётр Петров", "+79111111111", "petr@example.com");
        Client client2 = new Client(null, "Мария Сидорова", "+79222222222", "maria@example.com");
        clientDao.create(client1);
        clientDao.create(client2);
        List<Client> clients = clientDao.findAll();
        assertEquals(2, clients.size(), "Должно быть найдено два клиента");
        assertTrue(clients.stream().anyMatch(c -> c.getName().equals("Пётр Петров")),
                "Список должен содержать Петра Петрова");
        assertTrue(clients.stream().anyMatch(c -> c.getName().equals("Мария Сидорова")),
                "Список должен содержать Марию Сидорову");
    }

    /**
     * Тестирует обновление существующего клиента.
     */
    @Test
    void testUpdateClient() {
        Client client = new Client(null, "Старое Имя", "+79123456789", "old@example.com");
        clientDao.create(client);
        Client updatedClient = new Client(client.getId(), "Новое Имя", "+79987654321", "new@example.com");
        boolean result = clientDao.update(updatedClient);
        assertTrue(result, "Обновление должно быть успешным");
        Client saved = clientDao.findById(client.getId());
        assertEquals("Новое Имя", saved.getName(), "Имя клиента должно быть обновлено");
        assertEquals("+79987654321", saved.getPhone(), "Телефон клиента должен быть обновлён");
        assertEquals("new@example.com", saved.getEmail(), "Email клиента должен быть обновлён");
    }

    /**
     * Тестирует попытку обновления несуществующего клиента.
     */
    @Test
    void testUpdateNonExistentClient() {
        Client client = new Client(999L, "Несуществующий", "+79123456789", "none@example.com");
        boolean result = clientDao.update(client);
        assertFalse(result, "Обновление несуществующего клиента должно вернуть false");
    }

    /**
     * Тестирует удаление существующего клиента.
     */
    @Test
    void testDeleteClient() {
        Client client = new Client(null, "Для Удаления", "+79123456789", "delete@example.com");
        clientDao.create(client);
        boolean result = clientDao.delete(client.getId());
        assertTrue(result, "Удаление должно быть успешным");
        assertNull(clientDao.findById(client.getId()), "Клиент не должен быть найден после удаления");
    }

    /**
     * Тестирует попытку удаления несуществующего клиента.
     */
    @Test
    void testDeleteNonExistentClient() {
        Long nonExistentId = 999L;
        boolean result = clientDao.delete(nonExistentId);
        assertFalse(result, "Удаление несуществующего клиента должно вернуть false");
    }
}