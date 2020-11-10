package com.capgemini.mrchecker.database.unit.base.environment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.database.core.base.environment.EnvironmentParam;
import com.capgemini.mrchecker.database.tags.UnitTest;

@UnitTest
public class EnvironmentParamTest {
	
	@Test
	public void shouldDbUsernameHaveValue() {
		assertThat(EnvironmentParam.DB_USERNAME.getValue(), is(equalTo("tester")));
	}
	
	@Test
	public void shouldDbPasswordHaveValue() {
		assertThat(EnvironmentParam.DB_PASSWORD.getValue(), is(equalTo("Vx4hylOJhNzRc5kKm4gr")));
	}
	
	@Test
	public void shouldDbConnectionUrlHaveValue() {
		assertThat(EnvironmentParam.DB_CONNECTION_URL.getValue(), is(equalTo("localhost:3307/int_tests")));
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
