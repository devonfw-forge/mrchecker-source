package com.capgemini.mrchecker.test.core.unit.base.environment.providers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import com.capgemini.mrchecker.test.core.base.encryption.IDataEncryptionService;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.base.environment.providers.SpreadsheetEnvironmentService;
import com.capgemini.mrchecker.test.core.base.runtime.RuntimeParametersCore;
import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;
import com.capgemini.mrchecker.test.core.tags.UnitTest;

@UnitTest
@ResourceLock(value = "RuntimeParametersCoreTest.class")
public class SpreadsheetEnvironmentServiceTest {
	
	public static final String		TEST_ENV				= "TEST_ENV";
	public static final String[][]	CORRECT_CSV_DATA_ARRAY	= { { "service variable", "DEV", TEST_ENV },
					{ "Key_1", "DEV_Value1", TEST_ENV + "_Value1" },
					{ "Key_2", "DEV_Value2", TEST_ENV + "_Value2" }
	};
	
	public static final String	CORRECT_CSV_DATA;
	public static final String	NO_SUCH_ENV	= "No such env";
	
	static {
		final StringBuilder sb = new StringBuilder();
		{
			Arrays.stream(CORRECT_CSV_DATA_ARRAY)
					.forEach(row -> {
						Arrays.stream(row)
								.forEach(s -> sb.append(s)
										.append(','));
						sb.deleteCharAt(sb.length() - 1)
								.append('\n');
					});
			CORRECT_CSV_DATA = sb.toString();
		}
	}
	
	// TODO: how to get string that cannot be parsed
	public static final String UNPARSABLE_CSV_DATA = "\t,DEV," + TEST_ENV + "\n" +
			"Key_1,DEV_Value1," + TEST_ENV + "_Value1\n" +
			"Key_2,DEV_Value2," + TEST_ENV + "_Value2\n";
	
	// TODO: is it always true?
	public static final String	DEFAULT_ENV_WHEN_NO_ENVIRONMENT_SERVICE_SET	= "DEV";
	public static final String	DECRYPTED_VALUE_FROM_MOCK					= "DECRYPTED_VALUE";
	public static final String	NO_SUCH_KEY									= "NO_SUCH_KEY";
	
	@BeforeAll
	public static void setUpClass() {
		SpreadsheetEnvironmentService.delInstance();
	}
	
	@AfterEach
	public void tearDown() {
		SpreadsheetEnvironmentService.delInstance();
	}
	
	private static SpreadsheetEnvironmentService getSut() {
		return (SpreadsheetEnvironmentService) SpreadsheetEnvironmentService.getInstance();
	}
	
	private static SpreadsheetEnvironmentService initAndGetSut(String csvData, String env) {
		SpreadsheetEnvironmentService.init(csvData, env);
		return getSut();
	}
	
	private static SpreadsheetEnvironmentService initAndGetSut() {
		return initAndGetSut(CORRECT_CSV_DATA, TEST_ENV);
	}
	
	@Test
	public void shouldInitiallyBeNull() {
		assertThat(getSut(), is(nullValue()));
	}
	
	@Test
	public void shouldInitOnce() {
		assertThat(initAndGetSut(), is(notNullValue()));
	}
	
	@Test
	public void shouldInitTwice() {
		IEnvironmentService firstRef = initAndGetSut();
		IEnvironmentService secondRef = initAndGetSut();
		
		assertThat(firstRef, is(notNullValue()));
		assertThat(firstRef, is(equalTo(secondRef)));
	}
	
	@Test
	public void shouldInitMultiThread() {
		// TODO: implement multi thread check
	}
	
	// TODO: implement that
	@Disabled
	@Test
	public void shouldInitThrowExceptionWhenUnparsableInput() {
		assertThrows(BFInputDataException.class, () -> initAndGetSut(UNPARSABLE_CSV_DATA, TEST_ENV));
	}
	
	@Test
	public void shouldInitThrowExceptionWhenEnvNotFound() {
		assertThrows(BFInputDataException.class, () -> initAndGetSut(CORRECT_CSV_DATA, NO_SUCH_ENV));
	}
	
	@Test
	public void shouldDelInstance() {
		assertThat(initAndGetSut(), is(notNullValue()));
		
		SpreadsheetEnvironmentService.delInstance();
		assertThat(getSut(), is(nullValue()));
		assertThat(RuntimeParametersCore.ENV.getValue(), is(equalTo(DEFAULT_ENV_WHEN_NO_ENVIRONMENT_SERVICE_SET)));
	}
	
	@Test
	public void shouldGetValue() {
		initAndGetSut();
		
		String testEnv_Value1 = getSut().getValue(CORRECT_CSV_DATA_ARRAY[1][0]);
		String testEnv_Value2 = getSut().getValue(CORRECT_CSV_DATA_ARRAY[2][0]);
		
		assertThat(testEnv_Value1, is(equalTo(CORRECT_CSV_DATA_ARRAY[1][2])));
		assertThat(testEnv_Value2, is(equalTo(CORRECT_CSV_DATA_ARRAY[2][2])));
	}
	
	@Test
	public void shouldGetValueReturnDecryptedValue() {
		initAndGetSut();
		IDataEncryptionService dataEncryptionServiceMock = mock(IDataEncryptionService.class);
		when(dataEncryptionServiceMock.isEncrypted(anyString())).thenReturn(true);
		when(dataEncryptionServiceMock.decrypt(anyString())).thenReturn(DECRYPTED_VALUE_FROM_MOCK);
		
		getSut().setDataEncryptionService(dataEncryptionServiceMock);
		
		assertThat(getSut().getValue(CORRECT_CSV_DATA_ARRAY[1][0]), is(equalTo(DECRYPTED_VALUE_FROM_MOCK)));
	}
	
	@Test
	public void shouldGetValueReturnOriginalValueWhenDataNotEncrypted() {
		initAndGetSut();
		IDataEncryptionService dataEncryptionServiceMock = mock(IDataEncryptionService.class);
		when(dataEncryptionServiceMock.isEncrypted(anyString())).thenReturn(false);
		
		getSut().setDataEncryptionService(dataEncryptionServiceMock);
		
		assertThat(getSut().getValue(CORRECT_CSV_DATA_ARRAY[1][0]), is(equalTo(CORRECT_CSV_DATA_ARRAY[1][2])));
	}
	
	@Test
	public void shouldGetValueThrowExceptionWhenKeyIsNotFound() {
		initAndGetSut();
		
		assertThrows(BFInputDataException.class, () -> getSut().getValue(NO_SUCH_KEY));
	}
	
	@Test
	public void shouldGetEnvironmentReturnValue() {
		initAndGetSut();
		
		assertThat(getSut().getEnvironment(), is(equalTo(TEST_ENV)));
	}
	
	@Test
	public void shouldSetDataEncryptionService() {
		initAndGetSut();
		IDataEncryptionService dataEncryptionServiceMock = mock(IDataEncryptionService.class);
		
		getSut().setDataEncryptionService(dataEncryptionServiceMock);
	}
	
	@Test
	public void shouldSetEnvironment() {
		initAndGetSut();
		
		getSut().setEnvironment(DEFAULT_ENV_WHEN_NO_ENVIRONMENT_SERVICE_SET);
	}
	
	@Test
	public void shouldSetEnvironmentThrowExceptionWhenNoSuchEnvironment() {
		initAndGetSut();
		
		assertThrows(BFInputDataException.class, () -> getSut().setEnvironment(NO_SUCH_ENV));
	}
}
