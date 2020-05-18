package com.capgemini.mrchecker.test.core.unit.base.environment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.capgemini.mrchecker.test.core.base.environment.EnvironmentModule;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.base.environment.providers.SpreadsheetEnvironmentService;
import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;
import com.google.inject.Guice;

public class EnvironmentModuleTest {
	
	public static final String NO_FILE_PATH = "no file path";
	
	@BeforeClass
	public static void setUpClass() {
		SpreadsheetEnvironmentService.delInstance();
	}
	
	@After
	public void tearDown() {
		SpreadsheetEnvironmentService.delInstance();
	}
	
	@Test
	public void shouldCreateEnvironmentServiceInstance() {
		IEnvironmentService environmentService = Guice.createInjector(new EnvironmentModule())
				.getInstance(IEnvironmentService.class);
		assertThat(environmentService, is(notNullValue()));
	}
	
	@Test(expected = BFInputDataException.class)
	public void shouldCreateThrowExceptionWhenWrongFile() {
		Guice.createInjector(new EnvironmentModule(NO_FILE_PATH))
				.getInstance(IEnvironmentService.class);
	}
}