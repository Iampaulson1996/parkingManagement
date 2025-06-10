package com.parkingManagement.service;

import com.parkingManagement.dao.ClientDao;
import com.parkingManagement.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Сервис для управления клиентами в системе управления парковкой.
 */
@Service
public class ClientService {
    private final ClientDao clientDao;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+\\d{10,15}$");

    /**
     * Конструктор сервиса клиентов.
     
     * @param clientDao DAO для доступа к данным клиентов
     */
    @Autowired
    public ClientService(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    /**
     * Создаёт нового клиента с проверкой данных.
     
     * @param client клиент для создания
     * @throws IllegalArgumentException при некорректных данных
     */
    public void createClient(Client client) {
        validateClient(client, false);
        clientDao.create(client);
    }

    /**
     * Находит клиента по идентификатору.
     
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
     
     * @return список клиентов
     */
    public List<Client> getAllClients() {
        return clientDao.findAll();
    }

    /**
     * Обновляет данные клиента.
     
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
        if (client.getEmail() != null && !client.getEmail().isEmpty() && !EMAIL_PATTERN.matcher(client.getEmail()).matches()) {
            throw new IllegalArgumentException("Некорректный email");
        }
        if (client.getPhone() != null && !client.getPhone().isEmpty() && !PHONE_PATTERN.matcher(client.getPhone()).matches()) {
            throw new IllegalArgumentException("Некорректный номер телефона");
        }
    }

    /**
     * Проверяет корректность идентификатора.
     
     * @param id идентификатор
     * @param field название поля для сообщения об ошибке
     * @throws IllegalArgumentException при некорректном идентификаторе
     */
    private void validateId(Long id, String field) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(field + " должен быть положительным");
        }
    }
}