package com.capgemini.mrchecker.test.core.unit.base.encryption;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.capgemini.mrchecker.test.core.base.encryption.DataEncryptionModule;
import com.capgemini.mrchecker.test.core.base.encryption.IDataEncryptionService;
import com.capgemini.mrchecker.test.core.base.encryption.providers.DataEncryptionService;
import com.capgemini.mrchecker.test.core.exceptions.BFSecureModuleException;
import com.google.inject.Guice;

public class DataEncryptionModuleTest {
	
	public static final String NO_FILE_PATH = "no file path";
	
	@BeforeClass
	public static void setUpClass() {
		DataEncryptionService.delInstance();
	}
	
	@After
	public void tearDown() {
		DataEncryptionService.delInstance();
	}
	
	@Test
	public void shouldCreateDataEncryptionServiceInstance() {
		IDataEncryptionService dataEncryptionService = Guice.createInjector(new DataEncryptionModule())
				.getInstance(IDataEncryptionService.class);
		assertThat(dataEncryptionService, is(notNullValue()));
	}
	
	@Test(expected = BFSecureModuleException.class)
	public void shouldCreateThrowExceptionWhenWrongFile() {
		Guice.createInjector(new DataEncryptionModule(NO_FILE_PATH))
				.getInstance(IDataEncryptionService.class);
	}
}
