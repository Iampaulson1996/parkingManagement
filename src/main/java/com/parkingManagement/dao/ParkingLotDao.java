package com.parkingManagement.dao;

import com.parkingManagement.model.ParkingLot;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO для управления парковками в базе данных с использованием Hibernate.
 */
@Repository
public class ParkingLotDao {
    @PersistenceContext
    private EntityManager em;

    /**
     * Создаёт новую парковку в базе данных.

     * @param parkingLot парковка для создания
     * @throws PersistenceException при ошибке сохранения
     */
    public void create(ParkingLot parkingLot) {
        em.getTransaction().begin();
        try {
            em.persist(parkingLot);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new PersistenceException("Ошибка при создании парковки: " + e.getMessage());
        }
    }

    /**
     * Находит парковку по идентификатору.

     * @param id идентификатор парковки
     * @return парковка или null, если не найдена
     */
    public ParkingLot findById(Long id) {
        return em.find(ParkingLot.class, id);
    }

    /**
     * Возвращает список всех парковок.

     * @return список парковок
     */
    public List<ParkingLot> findAll() {
        TypedQuery<ParkingLot> query = em.createQuery("SELECT p FROM ParkingLot p", ParkingLot.class);
        return query.getResultList();
    }

    /**
     * Обновляет парковку в базе данных.

     * @param parkingLot парковка для обновления
     * @return true, если обновление успешно, false, если парковка не существует
     * @throws PersistenceException при ошибке обновления
     */
    public boolean update(ParkingLot parkingLot) {
        em.getTransaction().begin();
        try {
            ParkingLot existing = em.find(ParkingLot.class, parkingLot.getId());
            if (existing == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.merge(parkingLot);
            em.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new PersistenceException("Ошибка при обновлении парковки: " + e.getMessage());
        }
    }

    /**
     * Удаляет парковку по идентификатору.

     * @param id идентификатор парковки
     * @return true, если удаление успешно, false, если парковка не существует
     * @throws PersistenceException при ошибке удаления
     */
    public boolean delete(Long id) {
        em.getTransaction().begin();
        try {
            ParkingLot parkingLot = em.find(ParkingLot.class, id);
            if (parkingLot == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(parkingLot);
            em.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new PersistenceException("Ошибка при удалении парковки: " + e.getMessage());
        }
    }
}