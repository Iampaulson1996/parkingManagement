package com.parkingManagement.service;

import com.parkingManagement.dao.ClientDao;
import com.parkingManagement.model.Client;

import java.sql.SQLException;
import java.util.List;

/**
 * Сервисный класс для управления операциями с клиентами.
 */
public class ClientService {
    private final ClientDao clientDao;

    public ClientService(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    /**
     * Создаёт нового клиента с проверкой данных.
     *
     * @param client клиент для создания
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException при некорректных данных
     */
    public void createClient(Client client) throws SQLException {
        if (client == null || client.getName() == null || client.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя клиента обязательно");
        }
        clientDao.create(client);
    }

    /**
     * Находит клиента по его идентификатору.
     *
     * @param id идентификатор клиента
     * @return клиент
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если клиент не найден
     */
    public Client getClient(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Идентификатор клиента должен быть положительным");
        }
        Client client = clientDao.findById(id);
        if (client == null) {
            throw new IllegalArgumentException("Клиент с ID " + id + " не найден");
        }
        return client;
    }

    /**
     * Возвращает список всех клиентов.
     *
     * @return список клиентов
     * @throws SQLException при ошибке доступа к базе данных
     */
    public List<Client> getAllClients() throws SQLException {
        return clientDao.findAll();
    }

    /**
     * Обновляет существующего клиента.
     *
     * @param client клиент для обновления
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если клиент не найден
     */
    public void updateClient(Client client) throws SQLException {
        if (client == null || client.getId() == null || client.getId() <= 0) {
            throw new IllegalArgumentException("Идентификатор клиента должен быть положительным");
        }
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя клиента обязательно");
        }
        if (!clientDao.update(client)) {
            throw new IllegalArgumentException("Клиент с ID " + client.getId() + " не найден");
        }
    }

    /**
     * Удаляет клиента по его идентификатору.
     *
     * @param id идентификатор клиента для удаления
     * @throws SQLException при ошибке доступа к базе данных
     * @throws IllegalArgumentException если клиент не найден
     */
    public void deleteClient(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Идентификатор клиента должен быть положительным");
        }
        if (!clientDao.delete(id)) {
            throw new IllegalArgumentException("Клиент с ID " + id + " не найден");
        }
    }
}