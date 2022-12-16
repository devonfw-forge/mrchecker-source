package com.capgemini.mrchecker.database.core.base;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class AbstractDao<T, K extends Serializable> implements IDao<T, K> {

    private final EntityManager entityManager;
    private final Class<T> domainClass;

    public AbstractDao(EntityManager entityManager) {
        this(null, null, entityManager);
    }

    public AbstractDao(Class<T> entityType, Class<K> idType, EntityManager entityManager) {
        this.entityManager = entityManager;
        this.domainClass = entityType;
    }

    @Override
    public T save(T entity) {
        performTransactionWithOperation(() -> {
            entityManager.persist(entity);
            return null;
        });
        return entity;
    }

    @Override
    public T getOne(K id) {
        return entityManager.getReference(getDomainClass(), id);
    }

    @Override
    public T findOne(K id) {
        return entityManager.find(getDomainClass(), id);
    }

    @Override
    public List<T> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(getDomainClass());
        criteriaQuery.from(getDomainClass());
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public T update(T entity) {
        return performTransactionWithOperation(() -> entityManager.merge(entity));
    }

    @Override
    public void delete(T entity) {
        if (!Objects.isNull(entity)) {
            performTransactionWithOperation(() -> {
                entityManager.remove(entity);
                return null;
            });
        }
    }

    @Override
    public void delete(K id) {
        T entity = findOne(id);
        delete(entity);
    }

    @Override
    public void deleteAll() {
        performTransactionWithOperation(() -> {
            entityManager.flush();
            entityManager.clear();
            return entityManager.createQuery("DELETE FROM " + getDomainClassName())
                    .executeUpdate();
        });
    }

    @Override
    public long count() {
        return (long) entityManager.createQuery("SELECT COUNT(*) FROM " + getDomainClassName())
                .getSingleResult();
    }

    @Override
    public boolean exists(K id) {
        return findOne(id) != null;
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getDomainClass() {
        return (Class<T>) (Objects.isNull(domainClass) ? ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0] : domainClass);
    }

    protected String getDomainClassName() {
        return getDomainClass().getName();
    }

    private <R> R performTransactionWithOperation(Supplier<R> operation) {
        entityManager.getTransaction()
                .begin();
        R result = operation.get();
        entityManager.getTransaction()
                .commit();
        return result;
    }
}
