package com.parkingManagement.service;

import com.parkingManagement.dao.ClientDao;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Класс для тестирования операций сервиса ClientService с использованием методологии AAA.
 */
class ClientServiceTest {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private ClientDao clientDao;
    private ClientService clientService;

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
        clientDao = new ClientDao(em);
        clientService = new ClientService(clientDao);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM ParkingRecord").executeUpdate();
        em.createQuery("DELETE FROM Vehicle").executeUpdate();
        em.createQuery("DELETE FROM Client").executeUpdate();
        em.getTransaction().commit();
    }

    /**
     * Тестирует создание нового клиента с корректными данными.
     */
    @Test
    void testCreateClientSuccess() {
        Client client = new Client(null, "Иван Иванов", "+79123456789", "ivan@example.com");
        clientService.createClient(client);
        Client saved = clientDao.findById(client.getId());
        assertNotNull(saved, "Клиент должен быть сохранён");
        assertEquals("Иван Иванов", saved.getName(), "Имя клиента должно совпадать");
        assertEquals("+79123456789", saved.getPhone(), "Телефон клиента должен совпадать");
        assertEquals("ivan@example.com", saved.getEmail(), "Email клиента должен совпадать");
    }

    /**
     * Тестирует создание клиента с некорректными данными (null имя).
     */
    @Test
    void testCreateClientWithInvalidData() {
        Client client = new Client(null, null, "+79123456789", "ivan@example.com");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> clientService.createClient(client));
        assertEquals("Имя клиента обязательно", exception.getMessage());
    }

    /**
     * Тестирует получение клиента по существующему идентификатору.
     */
    @Test
    void testGetClientSuccess() {
        Client client = new Client(null, "Анна Смирнова", "+79087654321", "anna@example.com");
        clientDao.create(client);
        Client found = clientService.getClient(client.getId());
        assertNotNull(found, "Клиент должен быть найден");
        assertEquals(client.getId(), found.getId(), "Идентификатор клиента должен совпадать");
    }

    /**
     * Тестирует получение клиента по несуществующему идентификатору.
     */
    @Test
    void testGetClientNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> clientService.getClient(999L));
        assertEquals("Клиент с ID 999 не найден", exception.getMessage());
    }

    /**
     * Тестирует получение списка всех клиентов.
     */
    @Test
    void testGetAllClients() {
        Client client1 = new Client(null, "Пётр Петров", "+79111111111", "petr@example.com");
        Client client2 = new Client(null, "Мария Сидорова", "+79222222222", "maria@example.com");
        clientDao.create(client1);
        clientDao.create(client2);
        List<Client> clients = clientService.getAllClients();
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
    void testUpdateClientSuccess() {
        Client client = new Client(null, "Старое Имя", "+79123456789", "old@example.com");
        clientDao.create(client);
        Client updatedClient = new Client(client.getId(), "Новое Имя", "+79987654321", "new@example.com");
        clientService.updateClient(updatedClient);
        Client saved = clientDao.findById(client.getId());
        assertEquals("Новое Имя", saved.getName(), "Имя клиента должно быть обновлено");
        assertEquals("+79987654321", saved.getPhone(), "Телефон клиента должен быть обновлён");
        assertEquals("new@example.com", saved.getEmail(), "Email клиента должен быть обновлён");
    }

    /**
     * Тестирует попытку обновления несуществующего клиента.
     */
    @Test
    void testUpdateClientNotFound() {
        Client client = new Client(999L, "Несуществующий", "+79123456789", "none@example.com");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> clientService.updateClient(client));
        assertEquals("Клиент с ID 999 не найден", exception.getMessage());
    }

    /**
     * Тестирует удаление существующего клиента.
     */
    @Test
    void testDeleteClientSuccess() {
        Client client = new Client(null, "Для Удаления", "+79123456789", "delete@example.com");
        clientDao.create(client);
        clientService.deleteClient(client.getId());
        assertNull(clientDao.findById(client.getId()), "Клиент не должен быть найден после удаления");
    }

    /**
     * Тестирует попытку удаления несуществующего клиента.
     */
    @Test
    void testDeleteClientNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> clientService.deleteClient(999L));
        assertEquals("Клиент с ID 999 не найден", exception.getMessage());
    }
}