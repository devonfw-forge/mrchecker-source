package com.capgemini.mrchecker.database;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ParametrizingPersistenceXmlTest {

    @Test
    public void loadAndUsePersistentXml() {
        BFLogger.logInfo("username string=" + (String) System.getProperties().get("db.username"));
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "parametrizedConnection" );
        String connectionUrl = (String) emfactory.getProperties().get("hibernate.connection.url");
        BFLogger.logInfo("Connection string=" + connectionUrl);
    }
}
