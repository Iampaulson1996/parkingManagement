package com.parkingManagement.dao;

import com.parkingManagement.model.ParkingRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO для управления записями о парковке в базе данных с Hibernate.
 */
@Repository
public class ParkingRecordDao {
    @PersistenceContext
    private EntityManager em;

    /**
     * Создаёт новую запись о парковке в базе данных.

     * @param record запись о парковке для создания
     * @throws PersistenceException при ошибке сохранения
     */
    public void create(ParkingRecord record) {
        em.getTransaction().begin();
        try {
            em.persist(record);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new PersistenceException("Ошибка при создании записи: " + e.getMessage());
        }
    }

    /**
     * Находит запись о парковке по идентификатору.

     * @param id идентификатор записи
     * @return запись о парковке или null, если не найдена
     */
    public ParkingRecord findById(Long id) {
        return em.find(ParkingRecord.class, id);
    }

    /**
     * Возвращает список всех записей о парковке.

     * @return список записей о парковке
     */
    public List<ParkingRecord> findAll() {
        TypedQuery<ParkingRecord> query = em.createQuery("SELECT p FROM ParkingRecord p", ParkingRecord.class);
        return query.getResultList();
    }

    /**
     * Обновляет запись о парковке в базе данных.

     * @param record запись о парковке для обновления
     * @return true, если обновление успешно, false, если запись не существует
     * @throws PersistenceException при ошибке обновления
     */
    public boolean update(ParkingRecord record) {
        em.getTransaction().begin();
        try {
            ParkingRecord existing = em.find(ParkingRecord.class, record.getId());
            if (existing == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.merge(record);
            em.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new PersistenceException("Ошибка при обновлении записи: " + e.getMessage());
        }
    }

    /**
     * Удаляет запись о парковке по идентификатору.

     * @param id идентификатор записи
     * @return true, если удаление успешно, false, если запись не существует
     * @throws PersistenceException при ошибке удаления
     */
    public boolean delete(Long id) {
        em.getTransaction().begin();
        try {
            ParkingRecord record = em.find(ParkingRecord.class, id);
            if (record == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(record);
            em.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new PersistenceException("Ошибка при удалении записи: " + e.getMessage());
        }
    }
}