package com.capgemini.mrchecker.test.core.unit.base.encryption;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import com.capgemini.mrchecker.test.core.base.encryption.DataEncryptionModule;
import com.capgemini.mrchecker.test.core.base.encryption.IDataEncryptionService;
import com.capgemini.mrchecker.test.core.base.encryption.providers.DataEncryptionService;
import com.capgemini.mrchecker.test.core.exceptions.BFSecureModuleException;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import com.google.inject.Guice;

@UnitTest
@ResourceLock(value = "SingleThread")
public class DataEncryptionModuleTest {
	public static final String NO_FILE_PATH = "no file path";
	
	@BeforeAll
	public static void setUpClass() {
		DataEncryptionService.delInstance();
	}
	
	@AfterEach
	public void tearDown() {
		DataEncryptionService.delInstance();
	}
	
	@Test
	public void shouldCreateDataEncryptionServiceInstance() {
		IDataEncryptionService dataEncryptionService = Guice.createInjector(new DataEncryptionModule())
				.getInstance(IDataEncryptionService.class);
		assertThat(dataEncryptionService, is(notNullValue()));
	}
	
	@Test
	public void shouldCreateThrowBFSecureModuleExceptionWhenWrongFile() {
		assertThrows(BFSecureModuleException.class, () -> Guice.createInjector(new DataEncryptionModule(NO_FILE_PATH))
				.getInstance(IDataEncryptionService.class));
	}
}
