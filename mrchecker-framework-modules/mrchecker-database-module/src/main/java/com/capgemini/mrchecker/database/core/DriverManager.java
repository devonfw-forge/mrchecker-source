package com.capgemini.mrchecker.database.core;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Objects;

public class DriverManager {

	private static EntityManagerFactory emf;

	public static EntityManager createEntityManager(String dbPrefix) {
		return getEntityManagerFactory(dbPrefix).createEntityManager();
	}

	private static EntityManagerFactory getEntityManagerFactory(String dbPrefix) {
		return Objects.isNull(emf) ? Persistence.createEntityManagerFactory(dbPrefix) : emf;
	}

}
