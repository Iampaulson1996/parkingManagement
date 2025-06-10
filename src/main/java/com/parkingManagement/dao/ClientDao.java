package com.parkingManagement.dao;

import com.parkingManagement.model.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO для управления клиентами в базе данных с использованием Hibernate.
 */
@Repository
public class ClientDao {
    @PersistenceContext
    private EntityManager em;

    /**
     * Создаёт нового клиента в базе данных.

     * @param client клиент для создания
     * @throws PersistenceException при ошибке сохранения
     */
    public void create(Client client) {
        em.getTransaction().begin();
        try {
            em.persist(client);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new PersistenceException("Ошибка при создании клиента: " + e.getMessage());
        }
    }

    /**
     * Находит клиента по идентификатору.

     * @param id идентификатор клиента
     * @return клиент или null, если не найден
     */
    public Client findById(Long id) {
        return em.find(Client.class, id);
    }

    /**
     * Возвращает список всех клиентов.

     * @return список клиентов
     */
    public List<Client> findAll() {
        TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c", Client.class);
        return query.getResultList();
    }

    /**
     * Обновляет клиента в базе данных.

     * @param client клиент для обновления
     * @return true, если обновление успешно, false, если клиент не существует
     * @throws PersistenceException при ошибке обновления
     */
    public boolean update(Client client) {
        em.getTransaction().begin();
        try {
            Client existing = em.find(Client.class, client.getId());
            if (existing == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.merge(client);
            em.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new PersistenceException("Ошибка при обновлении клиента: " + e.getMessage());
        }
    }

    /**
     * Удаляет клиента по идентификатору.

     * @param id идентификатор клиента
     * @return true, если удаление успешно, false, если клиент не существует
     * @throws PersistenceException при ошибке удаления
     */
    public boolean delete(Long id) {
        em.getTransaction().begin();
        try {
            Client client = em.find(Client.class, id);
            if (client == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(client);
            em.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new PersistenceException("Ошибка при удалении клиента: " + e.getMessage());
        }
    }
}