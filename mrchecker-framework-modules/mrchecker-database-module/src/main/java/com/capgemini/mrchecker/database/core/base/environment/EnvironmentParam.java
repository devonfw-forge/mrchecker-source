package com.capgemini.mrchecker.database.core.base.environment;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum EnvironmentParam {
    DB_USERNAME,
    DB_PASSWORD,
    DB_CONNECTION_URL;

    public String getValue() {
        if (Objects.isNull(BaseTest.getEnvironmentService())) {
            throw new BFInputDataException("Environment Parameters class wasn't initialized properly");
        }

        return BaseTest.getEnvironmentService().getValue(name());
    }

    @Override
    public String toString() {
        return getValue();
    }

    public static Map<String, String> getHibernateConnectionParams() {
        return new HashMap<String, String>() {
            {
                put("hibernate.connection.username", DB_USERNAME.getValue());
                put("hibernate.connection.password", DB_PASSWORD.getValue());
                put("hibernate.connection.url", "jdbc:mysql://" + DB_CONNECTION_URL.getValue());
            }
        };
    }
}
