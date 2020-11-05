package com.capgemini.mrchecker.database;

import com.capgemini.mrchecker.database.core.base.environment.EnvironmentParam;
import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ParametrizedPersistenceXmlTest extends BaseTest {

    @Test
    public void loadAndUsePersistentXml() {
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "parametrizedConnection", EnvironmentParam.getHibernateConnectionParams());
        String connectionUrl = (String) emfactory.getProperties().get("hibernate.connection.url");
        BFLogger.logInfo("Connection string=" + connectionUrl);
        EntityManager em = emfactory.createEntityManager();
        em. createQuery("FROM " + Emploee.class.getSimpleName()).getResultList().forEach(System.out::println);
        
        em.close();
        emfactory.close();
    }
}
