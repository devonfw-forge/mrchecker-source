package com.capgemini.mrchecker.test.core.unit.utils.encryption;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.test.core.exceptions.BFSecureModuleException;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import com.capgemini.mrchecker.test.core.utils.encryption.model.DataEncryptionModel;

@UnitTest
public class DataEncryptionModelTest {
	
	private static final String TEXT_1 = "password";
	private static final String KEY_1 = "1a2b3c4d5e6f7g8h";
	private static final String CIPHER_TEXT_FOR_KEY_STRING = "ENC(VZ6DVAvYbMsAzEOqWkWNKiunTlYX6ErB)";
	private static final String CIPHER_TEXT_FOR_KEY_FILE = "ENC(5gRt4C8HoUF/kZjQL/I2FYikYQLRgbV+)";
	private static final String SECRET_DATA_FILE = "src/resources/secretData";
	private static final String SECRET_DATA_FILE_CONTENT = "123FUbBO5Tt5PiTUEQrXGgWrDLCMthnzLKNy1zA5FVTFiTdHRQAyPRIGXmsAjPUPlJSoSLeSBM";
	
	@Test
	public void shouldEncryptDataWithKeyAsString() {
		DataEncryptionModel dataEncryptionModel = new DataEncryptionModel(KEY_1);
		String result = dataEncryptionModel.encrypt(TEXT_1);
		
		dataEncryptionModel.tearDownService();
		
		assertThat(result, is(not(equalTo(TEXT_1))));
		assertThat(PropertyValueEncryptionUtils.isEncryptedValue(result), is(equalTo(true)));
	}
	
	@Test
	public void shouldEncryptDataWithKeyAsFile() {
		DataEncryptionModel dataEncryptionModel = new DataEncryptionModel(SECRET_DATA_FILE);
		String result = dataEncryptionModel.encrypt(TEXT_1);
		
		dataEncryptionModel.tearDownService();
		
		assertThat(result, is(not(equalTo(TEXT_1))));
		assertThat(PropertyValueEncryptionUtils.isEncryptedValue(result), is(equalTo(true)));
	}
	
	@Test
	public void shouldDecryptDataWithKeyAsString() {
		DataEncryptionModel dataEncryptionModel = new DataEncryptionModel(KEY_1);
		String result = dataEncryptionModel.decrypt(CIPHER_TEXT_FOR_KEY_STRING);
		
		dataEncryptionModel.tearDownService();
		
		assertThat(result, is(not(equalTo(CIPHER_TEXT_FOR_KEY_STRING))));
		assertThat(result, is(equalTo(TEXT_1)));
		assertThat(PropertyValueEncryptionUtils.isEncryptedValue(CIPHER_TEXT_FOR_KEY_STRING), is(equalTo(true)));
		assertThat(PropertyValueEncryptionUtils.isEncryptedValue(result), is(equalTo(false)));
	}
	
	@Test
	public void shouldDecryptDataWithKeyAsFile() {
		DataEncryptionModel dataEncryptionModel = new DataEncryptionModel(SECRET_DATA_FILE);
		String result = dataEncryptionModel.decrypt(CIPHER_TEXT_FOR_KEY_FILE);
		
		dataEncryptionModel.tearDownService();
		
		assertThat(result, is(not(equalTo(CIPHER_TEXT_FOR_KEY_FILE))));
		assertThat(result, is(equalTo(TEXT_1)));
		assertThat(PropertyValueEncryptionUtils.isEncryptedValue(CIPHER_TEXT_FOR_KEY_FILE), is(equalTo(true)));
		assertThat(PropertyValueEncryptionUtils.isEncryptedValue(result), is(equalTo(false)));
	}
	
	@Test
	public void shouldGetSameResultsWhenTextEncryptedWithTwoDifferentKeysWereDecryptedWithThatKeys() {
		DataEncryptionModel dataEncryptionModel = new DataEncryptionModel(KEY_1);
		String textEncryptedString = dataEncryptionModel.encrypt(TEXT_1);
		String result1 = dataEncryptionModel.decrypt(textEncryptedString);
		dataEncryptionModel.tearDownService();
		
		dataEncryptionModel = new DataEncryptionModel(SECRET_DATA_FILE);
		String textEncryptedFile = dataEncryptionModel.encrypt(TEXT_1);
		String result2 = dataEncryptionModel.decrypt(textEncryptedFile);
		dataEncryptionModel.tearDownService();
		
		assertThat(result1, is(equalTo(result2)));
	}
	
	@Test
	public void shouldReadFileContentWhenSecretIsValidPathToFile() {
		DataEncryptionModel dataEncryptionModel = new DataEncryptionModel(SECRET_DATA_FILE);
		String textEncrypted = dataEncryptionModel.encrypt(TEXT_1);
		dataEncryptionModel.tearDownService();
		
		dataEncryptionModel = new DataEncryptionModel(SECRET_DATA_FILE_CONTENT);
		String result = dataEncryptionModel.decrypt(textEncrypted);
		
		assertThat(result, is(equalTo(TEXT_1)));
	}
	
	@Test
	public void shouldThrowExceptionWhenTryingToInitializeWithEmptyKey() {
		assertThrows(BFSecureModuleException.class, () -> new DataEncryptionModel(""));
	}
	
	@Test
	public void shouldThrowExceptionWhenTryingToInitializeWithTooShortKey() {
		assertThrows(BFSecureModuleException.class, () -> new DataEncryptionModel("1234"));
	}
	
	@Test
	public void shouldThrowExceptionWhenTryingToDecryptTextThatIsNotEncrypted() {
		assertThrows(BFSecureModuleException.class, () -> new DataEncryptionModel(KEY_1).decrypt("not encrypted text"));
	}
	
	@Test
	public void shouldThrowExceptionWhenTryingToDecryptWithDifferentKey() {
		DataEncryptionModel dataEncryptionModel = new DataEncryptionModel(KEY_1);
		String textEncrypted = dataEncryptionModel.encrypt(TEXT_1);
		dataEncryptionModel.tearDownService();
		
		assertThrows(EncryptionOperationNotPossibleException.class, () -> new DataEncryptionModel(SECRET_DATA_FILE).decrypt(textEncrypted));
	}
	
}
