package com.capgemini.mrchecker.database.mocks;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;

public class PersistenceProviderMock implements PersistenceProvider {
	
	public static final String SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK = "unitTesting";
	
	@Override
	public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map map) {
		return persistenceUnitName.equals(SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK) ? new EntityManagerFactoryMock() : null;
	}
	
	@Override
	public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo persistenceUnitInfo, Map map) {
		return null;
	}
	
	@Override
	public void generateSchema(PersistenceUnitInfo persistenceUnitInfo, Map map) {
		
	}
	
	@Override
	public boolean generateSchema(String s, Map map) {
		return false;
	}
	
	@Override
	public ProviderUtil getProviderUtil() {
		return null;
	}
}
