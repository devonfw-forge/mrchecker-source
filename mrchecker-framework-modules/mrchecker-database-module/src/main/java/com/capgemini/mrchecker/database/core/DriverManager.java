package com.capgemini.mrchecker.database.core;

import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.capgemini.mrchecker.database.core.base.environment.EnvironmentParam;

public final class DriverManager {
	
	public static final String PARAMETRIZED_PERSISTENCE_UNIT = "parametrizedConnection";
	
	private static final ThreadLocal<EntityManagerFactory> emfs = new ThreadLocal<>();
	
	private DriverManager() {
	}
	
	public static EntityManager createEntityManager(String dbPrefix) {
		return getEntityManagerFactory(dbPrefix).createEntityManager();
	}
	
	private static EntityManagerFactory getEntityManagerFactory(String dbPrefix) {
		if (Objects.isNull(emfs.get())) {
			emfs.set(dbPrefix.equals(PARAMETRIZED_PERSISTENCE_UNIT) ? Persistence.createEntityManagerFactory(dbPrefix, EnvironmentParam.getHibernateConnectionParams())
					: Persistence.createEntityManagerFactory(dbPrefix));
		}
		
		return emfs.get();
	}
	
	public static void closeDriver() {
		if (!Objects.isNull(emfs.get())) {
			emfs.get()
					.close();
			emfs.set(null);
		}
	}
}