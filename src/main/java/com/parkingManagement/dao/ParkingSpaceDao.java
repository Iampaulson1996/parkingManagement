package com.parkingManagement.dao;

import com.parkingManagement.model.ParkingSpace;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO для управления парковочными местами в базе данных с Hibernate.
 */
@Repository
public class ParkingSpaceDao {
    @PersistenceContext
    private EntityManager em;

    /**
     * Создаёт новое парковочное место в базе данных.

     * @param parkingSpace парковочное место для создания
     * @throws PersistenceException при ошибке сохранения
     */
    public void create(ParkingSpace parkingSpace) {
        em.getTransaction().begin();
        try {
            em.persist(parkingSpace);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new PersistenceException("Ошибка при создании места: " + e.getMessage());
        }
    }

    /**
     * Находит парковочное место по идентификатору.

     * @param id идентификатор парковочного места
     * @return парковочное место или null, если не найдено
     */
    public ParkingSpace findById(Long id) {
        return em.find(ParkingSpace.class, id);
    }

    /**
     * Возвращает список всех парковочных мест.

     * @return список парковочных мест
     */
    public List<ParkingSpace> findAll() {
        TypedQuery<ParkingSpace> query = em.createQuery("SELECT p FROM ParkingSpace p", ParkingSpace.class);
        return query.getResultList();
    }

    /**
     * Обновляет парковочное место в базе данных.

     * @param parkingSpace парковочное место для обновления
     * @return true, если обновление успешно, false, если место не существует
     * @throws PersistenceException при ошибке обновления
     */
    public boolean update(ParkingSpace parkingSpace) {
        em.getTransaction().begin();
        try {
            ParkingSpace existing = em.find(ParkingSpace.class, parkingSpace.getId());
            if (existing == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.merge(parkingSpace);
            em.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new PersistenceException("Ошибка при обновлении места: " + e.getMessage());
        }
    }

    /**
     * Удаляет парковочное место по идентификатору.

     * @param id идентификатор парковочного места
     * @return true, если удаление успешно, false, если место не существует
     * @throws PersistenceException при ошибке удаления
     */
    public boolean delete(Long id) {
        em.getTransaction().begin();
        try {
            ParkingSpace parkingSpace = em.find(ParkingSpace.class, id);
            if (parkingSpace == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(parkingSpace);
            em.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new PersistenceException("Ошибка при удалении места: " + e.getMessage());
        }
    }
}