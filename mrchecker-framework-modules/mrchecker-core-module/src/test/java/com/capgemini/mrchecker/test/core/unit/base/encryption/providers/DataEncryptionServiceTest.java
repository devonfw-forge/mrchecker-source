package com.capgemini.mrchecker.test.core.unit.base.encryption.providers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.capgemini.mrchecker.test.core.base.encryption.IDataEncryptionService;
import com.capgemini.mrchecker.test.core.base.encryption.providers.DataEncryptionService;
import com.capgemini.mrchecker.test.core.exceptions.BFSecureModuleException;

public class DataEncryptionServiceTest {
	
	public static final String	SECRET_TOO_SHORT	= "1234567";
	public static final String	SECRET_TRIMABLE		= " 1234567";
	public static final String	CORRECT_SECRET_1	= "12345678";
	public static final String	CORRECT_SECRET_2	= "87954321";
	public static final String	SECRET_EMPTY		= "";
	public static final String	CIPHERTEXT_1		= "ENC(Kneu8D+oIkD6mMAE/sB97ht95fT1lAhc)";
	public static final String	PLAINTEXT_1			= "plain text";
	public static final String	CIPHERTEXT_2		= "ENC(QTkxI0hWAyTDXdDJd8a9mP8lGGnngflplukqzFj5nj4=)";
	public static final String	PLAINTEXT_2			= "another plain text";
	
	@BeforeClass
	public static void setUpClass() {
		DataEncryptionService.delInstance();
	}
	
	@After
	public void tearDown() {
		DataEncryptionService.delInstance();
	}
	
	private static DataEncryptionService getSut() {
		return (DataEncryptionService) DataEncryptionService.getInstance();
	}
	
	private static DataEncryptionService initAndGetSut(String secret) throws IOException {
		try (ByteArrayInputStream secretSource = new ByteArrayInputStream(secret.getBytes())) {
			DataEncryptionService.init(secretSource);
		}
		return getSut();
	}
	
	private static DataEncryptionService initAndGetSut() throws IOException {
		return initAndGetSut(CORRECT_SECRET_1);
	}
	
	@Test
	public void shouldInitiallyBeNull() {
		assertThat(getSut(), is(nullValue()));
	}
	
	@Test
	public void shouldInitOnce() throws IOException {
		assertThat(initAndGetSut(), is(notNullValue()));
	}
	
	@Test
	public void shouldInitTwice() throws IOException {
		IDataEncryptionService firstRef = initAndGetSut();
		IDataEncryptionService secondRef = initAndGetSut();
		
		assertThat(firstRef, is(notNullValue()));
		assertThat(firstRef, is(equalTo(secondRef)));
	}
	
	@Test(expected = BFSecureModuleException.class)
	public void shouldInitThrowExceptionWhenSecretSourceIsEmpty() throws IOException {
		initAndGetSut(SECRET_EMPTY);
	}
	
	@Test
	public void shouldDelInstance() throws IOException {
		assertThat(initAndGetSut(), is(notNullValue()));
		
		DataEncryptionService.delInstance();
		
		assertThat(getSut(), is(nullValue()));
	}
	
	@Test(expected = BFSecureModuleException.class)
	public void shouldSetSecretThrowExceptionWhenSecretIsNull() throws IOException {
		initAndGetSut().setSecret(null);
	}
	
	@Test(expected = BFSecureModuleException.class)
	public void shouldSetSecretThrowExceptionWhenSecretIsShort() throws IOException {
		initAndGetSut().setSecret(SECRET_TOO_SHORT);
	}
	
	@Test(expected = BFSecureModuleException.class)
	public void shouldSetSecretThrowExceptionWhenSecretIsTrimable() throws IOException {
		initAndGetSut().setSecret(SECRET_TRIMABLE);
	}
	
	@Test
	public void shouldSetSecret() throws IOException {
		initAndGetSut().setSecret(CORRECT_SECRET_1);
	}
	
	@Test
	public void shouldIsEncryptedReturnFalseWhenTextIsNull() throws IOException {
		assertThat(initAndGetSut().isEncrypted(null), is(equalTo(false)));
	}
	
	@Test
	public void shouldIsEncryptedReturnValue() throws IOException {
		assertThat(initAndGetSut().isEncrypted(CIPHERTEXT_1), is(equalTo(true)));
	}
	
	@Test
	public void shouldEncryptReturnCipherText() throws IOException {
		String ciphertextVal = initAndGetSut().encrypt(CIPHERTEXT_1);
		
		assertThat(ciphertextVal, is(not(equalTo(PLAINTEXT_1))));
	}
	
	@Test
	public void shouldDecryptReturnPlaintext() throws IOException {
		String plaintextVal = initAndGetSut().decrypt(CIPHERTEXT_1);
		
		assertThat(plaintextVal, is(equalTo(PLAINTEXT_1)));
	}
	
	@Test
	public void shouldDecryptWithChangedSecret() throws IOException {
		shouldDecryptReturnPlaintext();
		getSut().setSecret(CORRECT_SECRET_2);
		
		String plaintextVal = getSut().decrypt(CIPHERTEXT_2);
		
		assertThat(plaintextVal, is(equalTo(PLAINTEXT_2)));
	}
	
	@Test(expected = BFSecureModuleException.class)
	public void shouldDecryptThrowExceptionWhenCipherTextIsNull() throws IOException {
		initAndGetSut().decrypt(null);
	}
	
	@Test
	public void shouldCreateMultiThread() throws IOException {
		// TODO: implement multi thread check
	}
}
