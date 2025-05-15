package com.parkingManagement.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

/**
 * Утилитный класс для управления EntityManagerFactory в Hibernate.
 */
public class HibernateUtil {
    private static final EntityManagerFactory emf;

    static {
        try {
            emf = Persistence.createEntityManagerFactory("parkingPU");
        } catch (PersistenceException e) {
            throw new RuntimeException("Ошибка инициализации Hibernate: " + e.getMessage());
        }
    }

    /**
     * Возвращает EntityManagerFactory для работы с Hibernate.

     * @return EntityManagerFactory
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    /**
     * Закрывает EntityManagerFactory при завершении работы приложения.
     */
    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            try {
                emf.close();
            } catch (PersistenceException e) {
                throw new RuntimeException("Ошибка закрытия EntityManagerFactory: " + e.getMessage());
            }
        }
    }
}