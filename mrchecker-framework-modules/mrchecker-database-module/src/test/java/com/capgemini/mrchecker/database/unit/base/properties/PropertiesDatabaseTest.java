package com.capgemini.mrchecker.database.unit.base.properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.google.inject.Guice;
import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.database.core.base.properties.PropertiesDatabase;
import com.capgemini.mrchecker.database.tags.UnitTest;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

@UnitTest
public class PropertiesDatabaseTest {

	public static final String[][] TEST_PROPERTIES_ARRAY = {
			{ "database.oracle", "TEST_DB" },
			{ "database.proxy", "TEST_PROXY" }
	};

	public static final String	TEST_PROPERTIES			= buildProperties();

	private static String buildProperties() {
		final StringBuilder sb = new StringBuilder();
		Arrays.stream(PropertiesDatabaseTest.TEST_PROPERTIES_ARRAY)
				.forEach(row -> sb.append(row[0])
						.append("=")
						.append(row[1])
						.append('\n'));
		return sb.toString();
	}


	@Test
	public void shouldGetProxyReturnDriverPathWhenCreatedByConstructor() {
		PropertiesDatabase sut = createSUTByConstructor();
		
		assertThat(sut.getJdbcOracle(), is(equalTo("./lib/dbdrivers/oracle/ojdbc8.jar")));
	}
	
	@Test
	public void shouldGetProxyReturnEmptyStringWhenCreatedByConstructor() {
		PropertiesDatabase sut = createSUTByConstructor();
		
		assertThat(sut.getProxy(), is(equalTo("")));
	}

	@Test
	public void shouldGetProxyReturnDriverPathWhenCreatedByDI() {
		PropertiesDatabase sut = createSUTByDI();

		assertThat(sut.getJdbcOracle(), is(equalTo("TEST_DB")));
	}

	@Test
	public void shouldGetProxyReturnEmptyStringWhenCreatedByDI() {
		PropertiesDatabase sut = createSUTByDI();

		assertThat(sut.getProxy(), is(equalTo("TEST_PROXY")));
	}

	
	private PropertiesDatabase createSUTByConstructor() {
		return new PropertiesDatabase();
	}
	
	private PropertiesDatabase createSUTByDI() {
		return Guice.createInjector(PropertiesSettingsModule.init(new ByteArrayInputStream(TEST_PROPERTIES.getBytes())))
				.getInstance(PropertiesDatabase.class);
	}
}