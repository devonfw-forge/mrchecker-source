package com.capgemini.mrchecker.database.core;

import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class DriverManager {
	
	private static EntityManagerFactory emf;
	
	private DriverManager() {
	}
	
	public static EntityManager createEntityManager(String dbPrefix) {
		return getEntityManagerFactory(dbPrefix).createEntityManager();
	}
	
	private synchronized static EntityManagerFactory getEntityManagerFactory(String dbPrefix) {
		if (Objects.isNull(emf)) {
			emf = Persistence.createEntityManagerFactory(dbPrefix);
		}
		
		return emf;
	}
	
	public synchronized static void closeDriver() {
		if (!Objects.isNull(emf)) {
			emf.close();
			emf = null;
		}
	}
}