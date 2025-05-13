package com.parkingManagement.service;

import com.parkingManagement.dao.ClientDao;
import com.parkingManagement.model.Client;

import java.util.List;

/**
 * Сервис для управления клиентами в системе управления парковкой.
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
     * @throws IllegalArgumentException при некорректных данных
     */
    public void createClient(Client client) {
        validateClient(client, false);
        clientDao.create(client);
    }

    /**
     * Находит клиента по идентификатору.
     *
     * @param id идентификатор клиента
     * @return клиент
     * @throws IllegalArgumentException если клиент не найден
     */
    public Client getClient(Long id) {
        validateId(id, "Идентификатор клиента");
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
     */
    public List<Client> getAllClients() {
        return clientDao.findAll();
    }

    /**
     * Обновляет клиента.
     *
     * @param client клиент для обновления
     * @throws IllegalArgumentException если клиент не найден
     */
    public void updateClient(Client client) {
        validateClient(client, true);
        if (!clientDao.update(client)) {
            throw new IllegalArgumentException("Клиент с ID " + client.getId() + " не найден");
        }
    }

    /**
     * Удаляет клиента по идентификатору.
     *
     * @param id идентификатор клиента
     * @throws IllegalArgumentException если клиент не найден
     */
    public void deleteClient(Long id) {
        validateId(id, "Идентификатор клиента");
        if (!clientDao.delete(id)) {
            throw new IllegalArgumentException("Клиент с ID " + id + " не найден");
        }
    }

    /**
     * Проверяет корректность данных клиента.
     *
     * @param client клиент для проверки
     * @param isUpdate флаг, указывающий, является ли операция обновлением
     * @throws IllegalArgumentException при некорректных данных
     */
    private void validateClient(Client client, boolean isUpdate) {
        if (client == null) {
            throw new IllegalArgumentException("Клиент не может быть null");
        }
        if (isUpdate && (client.getId() == null || client.getId() <= 0)) {
            throw new IllegalArgumentException("Идентификатор клиента должен быть положительным");
        }
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя клиента обязательно");
        }
    }

    /**
     * Проверяет корректность идентификатора.
     *
     * @param id      идентификатор
     * @param field   название поля для сообщения об ошибке
     * @throws IllegalArgumentException при некорректном идентификаторе
     */
    private void validateId(Long id, String field) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(field + " должен быть положительным");
        }
    }
}