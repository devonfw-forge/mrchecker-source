package com.capgemini.mrchecker.database.core.base;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AbstractDao<T, K extends Serializable> implements IDao<T, K> {

	private final EntityManager entityManager;

	public AbstractDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public T save(T entity) {
		entityManager.persist(entity);
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
		return entityManager.merge(entity);
	}

	@Override
	public void delete(T entity) {
		entityManager.remove(entity);
	}

	@Override
	public void delete(K id) {
		entityManager.remove(getOne(id));
	}

	@Override
	public void deleteAll() {
		entityManager.createQuery("TRUNCATE " + getDomainClassName()).executeUpdate();
	}

	@Override
	public long count() {
		return (long) entityManager.createQuery("Select count(*) from " + getDomainClassName()).getSingleResult();
	}

	@Override
	public boolean exists(K id) {
		return findOne(id) != null;
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getDomainClass() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected String getDomainClassName() {
		return getDomainClass().getName();
	}
}
