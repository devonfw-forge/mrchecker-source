package com.capgemini.mrchecker.database.unit.base.environment;

import com.capgemini.mrchecker.database.core.base.environment.EnvironmentParam;
import com.capgemini.mrchecker.database.tags.UnitTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@UnitTest
public class EnvironmentParamTest {

    private final String ENV = System.getProperty("env", "DEV");

    private static final Map<String, String> userEnvMapping = new HashMap<String, String>() {
        {
            put("DEV", "tester");
            put("QA", "user");
        }
    };

    private static final Map<String, String> passwordEnvMapping = new HashMap<String, String>() {
        {
            put("DEV", "Vx4hylOJhNzRc5kKm4gr");
            put("QA", "pass");
        }
    };

    private static final Map<String, String> connectionUrlEnvMapping = new HashMap<String, String>() {
        {
            put("DEV", "localhost:3307/int_tests");
            put("QA", System.getProperty("qaHost") + ":3306/int_tests");
        }
    };

    @Test
    public void shouldDbUsernameHaveValue() {
        assertThat(EnvironmentParam.DB_USERNAME.getValue(), is(equalTo(userEnvMapping.get(ENV))));
    }

    @Test
    public void shouldDbPasswordHaveValue() {
        assertThat(EnvironmentParam.DB_PASSWORD.getValue(), is(equalTo(passwordEnvMapping.get(ENV))));
    }

    @Test
    public void shouldDbConnectionUrlHaveValue() {
        assertThat(EnvironmentParam.DB_CONNECTION_URL.getValue(), is(equalTo(connectionUrlEnvMapping.get(ENV))));
    }

    @Test
    public void shouldGetHibernateConnectionParamsReturnReference() {
        assertThat(EnvironmentParam.getHibernateConnectionParams(), is(notNullValue()));
    }

    @Test
    public void shouldToStringReturnValue() {
        assertThat(EnvironmentParam.DB_USERNAME.toString(), is(equalTo(EnvironmentParam.DB_USERNAME.getValue())));
    }
}