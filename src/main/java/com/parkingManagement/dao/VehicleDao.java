package com.parkingManagement.dao;

import com.parkingManagement.model.Vehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * DAO для управления автомобилями в базе данных с использованием Hibernate.
 */
public class VehicleDao {
    private final EntityManager em;

    /**
     * Создаёт новый VehicleDao с указанным EntityManager.
     *
     * @param em менеджер сущностей Hibernate
     */
    public VehicleDao(EntityManager em) {
        this.em = em;
    }

    /**
     * Создаёт новый автомобиль в базе данных.
     *
     * @param vehicle автомобиль для создания
     * @throws PersistenceException при ошибке сохранения
     */
    public void create(Vehicle vehicle) {
        em.getTransaction().begin();
        try {
            em.persist(vehicle);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new PersistenceException("Ошибка при создании автомобиля: " + e.getMessage());
        }
    }

    /**
     * Находит автомобиль по идентификатору.
     *
     * @param id идентификатор автомобиля
     * @return автомобиль или null, если не найден
     */
    public Vehicle findById(Long id) {
        return em.find(Vehicle.class, id);
    }

    /**
     * Возвращает список всех автомобилей.
     *
     * @return список автомобилей
     */
    public List<Vehicle> findAll() {
        TypedQuery<Vehicle> query = em.createQuery("SELECT v FROM Vehicle v", Vehicle.class);
        return query.getResultList();
    }

    /**
     * Обновляет автомобиль в базе данных.
     *
     * @param vehicle автомобиль для обновления
     * @return true, если обновление успешно, false, если автомобиль не существует
     * @throws PersistenceException при ошибке обновления
     */
    public boolean update(Vehicle vehicle) {
        em.getTransaction().begin();
        try {
            Vehicle existing = em.find(Vehicle.class, vehicle.getId());
            if (existing == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.merge(vehicle);
            em.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new PersistenceException("Ошибка при обновлении автомобиля: " + e.getMessage());
        }
    }

    /**
     * Удаляет автомобиль по идентификатору.
     *
     * @param id идентификатор автомобиля
     * @return true, если удаление успешно, false, если автомобиль не существует
     * @throws PersistenceException при ошибке удаления
     */
    public boolean delete(Long id) {
        em.getTransaction().begin();
        try {
            Vehicle vehicle = em.find(Vehicle.class, id);
            if (vehicle == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(vehicle);
            em.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new PersistenceException("Ошибка при удалении автомобиля: " + e.getMessage());
        }
    }
}