package com.sparks.of.fabrication.oop2.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.cglib.core.Local;

import java.lang.reflect.Field;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper class for managing JPA entity transactions and queries.
 */
public class EntityManagerWrapper {

    private static final Logger log = LogManager.getLogger(EntityManagerWrapper.class);
    EntityManagerFactory emf;
    EntityManager em;

    /**
     * Constructor that initializes the EntityManager and connects to the database using the provided environment settings.
     *
     * @param env The environment settings containing database connection details.
     */
    public EntityManagerWrapper(Env env) {
        try {
            Map<String, String> properties = getProperties(env);

            emf = Persistence.createEntityManagerFactory("jpaOOP", properties);

            if (emf == null) {
                log.error("ENTITY MANAGET UNINITIALIZED");
            }
            em = emf.createEntityManager();

            if (em != null) {
                log.info("Successfully created EntityManager and connected to the database.");
            }

        } catch (Exception e) {
            log.error("Error Initializing Entity manager factory CAUSE: {} MESSAGE: {}", e.getMessage(), e.getCause());
        }
    }

    /**
     * Gets the properties for the database connection from the given environment settings.
     *
     * @param env The environment settings containing database connection details.
     * @return A map containing the properties required for database connection.
     */
    @NotNull
    private static Map<String, String> getProperties(Env env) {
        Map<String, String> properties = new HashMap<>();

        properties.put("jakarta.persistence.jdbc.url", env.getDbUrl());
        properties.put("jakarta.persistence.jdbc.user", env.getDbUser());
        properties.put("jakarta.persistence.jdbc.password", env.getDbPassword());
        properties.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
        properties.put("hibernate.hbm2ddl.auto", "update"); // Or "create", "create-drop"

        return properties;
    }

    /**
     * Begins a new transaction in the EntityManager.
     */
    private void beginTransaction() {
        em.getTransaction().begin();
    }

    /**
     * Commits the current transaction in the EntityManager.
     */
    private void commitTransaction() {
        em.getTransaction().commit();
    }

    /**
     * Rolls back the current transaction in the EntityManager.
     */
    private void rollbackTransaction() {
        em.getTransaction().rollback();
    }

    /**
     * Finds a single entity based on a field value.
     *
     * @param tClass The class of the entity to search for.
     * @param field The field in the entity to match against.
     * @param value The value to search for.
     * @param <T> The type of the entity.
     * @param <Y> The type of the field value.
     * @return A Pair containing a success flag and the found entity.
     */
    public <T, Y> Pair<Boolean, T> findEntityByVal(Class<T> tClass, Field field, Y value) {
        try {
            String jpql = "SELECT e FROM " + tClass.getSimpleName() + " e WHERE e." + field.getName() + " = :value";
            TypedQuery<T> query = this.em.createQuery(jpql, tClass);
            query.setParameter("value", value);

            T entity = query.getSingleResult();

            return new Pair<>(true, entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Pair<>(false, null);
        }
    }

    /**
     * Finds all entities of a given class that match a field value.
     *
     * @param tClass The class of the entities to search for.
     * @param field The field in the entity to match against.
     * @param value The value to search for.
     * @param <T> The type of the entity.
     * @param <Y> The type of the field value.
     * @return A Pair containing a success flag and the list of found entities.
     */
    public <T, Y> Pair<Boolean, List<T>> findEntityByValAll(Class<T> tClass, Field field, Y value) {
        try {
            String jpql = "SELECT e FROM " + tClass.getSimpleName() + " e WHERE e." + field.getName() + " = :value";
            TypedQuery<T> query = this.em.createQuery(jpql, tClass);
            query.setParameter("value", value);

            List<T> entity = query.getResultList();

            return new Pair<>(true, entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Pair<>(false, null);
        }
    }

    /**
     * Finds all entities of a given class that match a field value using the "LIKE" operator.
     *
     * @param tClass The class of the entities to search for.
     * @param field The field in the entity to match against.
     * @param value The value to search for.
     * @param <T> The type of the entity.
     * @param <Y> The type of the field value.
     * @return A Pair containing a success flag and the list of found entities.
     */
    public <T, Y> Pair<Boolean, List<T>> findEntityByValAllLikeR(Class<T> tClass, Field field, Y value) {
        try {
            String jpql = "SELECT e FROM " + tClass.getSimpleName() + " e WHERE e." + field.getName() + " LIKE :value";
            TypedQuery<T> query = this.em.createQuery(jpql, tClass);

            query.setParameter("value", value + "%");

            List<T> entities = query.getResultList();

            return new Pair<>(true, entities);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Pair<>(false, null);
        }
    }

    /**
     * Finds entities of a given class between two dates.
     *
     * @param tClass The class of the entities to search for.
     * @param dateField The date field to match against.
     * @param startDate The start date for the search range.
     * @param endDate The end date for the search range.
     * @param <T> The type of the entity.
     * @return A Pair containing a success flag and the list of found entities.
     */
    public <T> Pair<Boolean, List<T>> findEntitiesBetweenDates(Class<T> tClass, Field dateField, LocalDate startDate, LocalDate endDate) {
        try {
            String jpql = "SELECT e FROM " + tClass.getSimpleName() + " e WHERE e." + dateField.getName() + " BETWEEN :startDate AND :endDate";
            TypedQuery<T> query = this.em.createQuery(jpql, tClass);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<T> entities = query.getResultList();

            return new Pair<>(true, entities);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Pair<>(false, null);
        }
    }

    /**
     * Finds all entities of a given class.
     *
     * @param tClass The class of the entities to search for.
     * @param <T> The type of the entity.
     * @return A list containing all found entities.
     */
    public <T> List<T> findAllEntities(Class<T> tClass) {
        try {
            String jpql = "SELECT e FROM " + tClass.getSimpleName() + " e";
            TypedQuery<T> query = em.createQuery(jpql, tClass);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Error fetching all entities for {}: {}", tClass.getSimpleName(), e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Generates a new entity in the database.
     *
     * @param entity The entity to persist.
     * @param <T> The type of the entity.
     * @return A boolean indicating whether the operation was successful.
     */
    public <T> boolean genEntity(T entity) {
        try {
            beginTransaction();
            em.persist(entity);
            commitTransaction();
            log.info("Persisted entity: {}", entity);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rollbackTransaction();
            return false;
        }
    }

    /**
     * Finds an entity by its ID.
     *
     * @param tClass The class of the entity.
     * @param id The ID of the entity.
     * @param <T> The type of the entity.
     * @return A Pair containing a success flag and the found entity.
     */
    public <T> Pair<Boolean, T> findEntityById(Class<T> tClass, int id) {
        try {
            T entity = em.find(tClass, id);

            log.info("Found entity: {}", entity);
            return new Pair<>(true, entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rollbackTransaction();
            return new Pair<>(false, null);
        }
    }

    /**
     * Deletes an entity by its ID.
     *
     * @param tClass The class of the entity.
     * @param id The ID of the entity.
     * @param <T> The type of the entity.
     * @return A boolean indicating whether the operation was successful.
     */
    public <T> boolean deleteEntityById(Class<T> tClass, int id) {
        try {
            T entity = em.find(tClass, id);
            beginTransaction();
            em.remove(entity);
            commitTransaction();

            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rollbackTransaction();

            return false;
        }
    }

    /**
     * Updates an entity by its fields.
     *
     * @param tClass The class of the entity.
     * @param id The ID of the entity to update.
     * @param data The map of fields and values to update.
     * @param <T> The type of the entity.
     * @return A boolean indicating whether the operation was successful.
     */
    public <T> boolean updateEntityByFields(Class<T> tClass, int id, HashMap<Field, ?> data) {
        try {
            @NotNull
            StringBuilder jpql = new StringBuilder();

            jpql.append("UPDATE ");
            jpql.append(tClass.getSimpleName());
            jpql.append(" SET ");

            data.forEach((field, input) -> {
                jpql.append(field.getName()).append(" = ").append(input);
            });

            jpql.append(" WHERE ").append("id = ").append(id);

            em.createQuery(jpql.toString(), tClass).executeUpdate();

            return false;
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            rollbackTransaction();
            return false;
        }
    }

    /**
     * Updates an entity in the database.
     *
     * @param entity The entity to update.
     * @param <T> The type of the entity.
     * @return A boolean indicating whether the operation was successful.
     */
    public <T> boolean updateEntity(T entity) {
        try {
            beginTransaction();
            em.merge(entity);
            commitTransaction();
            log.info("Updated entity: {}", entity);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rollbackTransaction();
            return false;
        }
    }

    /**
     * Finds entities with joins based on a field value.
     *
     * @param tClass The class of the entities to search for.
     * @param field The field to match against.
     * @param value The value to search for.
     * @param joinFields The list of fields to join.
     * @param <T> The type of the entity.
     * @param <Y> The type of the field value.
     * @return A Pair containing a success flag and the list of found entities.
     */
    public <T, Y> Pair<Boolean, List<T>> findEntitiesWithJoins(Class<T> tClass, Field field, Y value, List<String> joinFields) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT e FROM " + tClass.getSimpleName() + " e");

            for (String joinField : joinFields) {
                jpql.append(" JOIN e.").append(joinField);
            }

            jpql.append(" WHERE e.").append(field.getName()).append(" = :value");

            TypedQuery<T> query = this.em.createQuery(jpql.toString(), tClass);
            query.setParameter("value", value);

            List<T> resultList = query.getResultList();
            return new Pair<>(true, resultList);
        } catch (Exception e) {
            log.error("Error executing query with joins: {}", e.getMessage(), e);
            return new Pair<>(false, null);
        }
    }

    /**
     * Cleans up the resources used by the EntityManager.
     *
     * @return A boolean indicating whether the cleanup was successful.
     */
    public boolean cleanUp() {
        try {
            if(em != null && em.isOpen()) {
                em.close();
            }
            if(emf != null && emf.isOpen()) {
                emf.close();
            }
            log.info("Cleared EntityManager");
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}
