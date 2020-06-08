package com.capgemini.mrchecker.test.core.unit.base.encryption.providers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import com.capgemini.mrchecker.test.core.base.encryption.IDataEncryptionService;
import com.capgemini.mrchecker.test.core.base.encryption.providers.DataEncryptionService;
import com.capgemini.mrchecker.test.core.exceptions.BFSecureModuleException;
import com.capgemini.mrchecker.test.core.tags.UnitTest;

@UnitTest
@ResourceLock(value = "SingleThread")
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
	public static final int		THREAD_COUNT		= 4;
	
	@BeforeAll
	public static void setUpClass() {
		DataEncryptionService.delInstance();
	}
	
	@AfterEach
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
	
	@Test
	public void shouldInitThrowExceptionWhenSecretSourceIsEmpty() {
		assertThrows(BFSecureModuleException.class, () -> initAndGetSut(SECRET_EMPTY));
	}
	
	@Test
	public void shouldDelInstance() throws IOException {
		assertThat(initAndGetSut(), is(notNullValue()));
		
		DataEncryptionService.delInstance();
		
		assertThat(getSut(), is(nullValue()));
	}
	
	@Test
	public void shouldSetSecretThrowExceptionWhenSecretIsNull() {
		assertThrows(BFSecureModuleException.class, () -> initAndGetSut().setSecret(null));
	}
	
	@Test
	public void shouldSetSecretThrowExceptionWhenSecretIsShort() {
		assertThrows(BFSecureModuleException.class, () -> initAndGetSut().setSecret(SECRET_TOO_SHORT));
	}
	
	@Test
	public void shouldSetSecretThrowExceptionWhenSecretIsTrimable() {
		assertThrows(BFSecureModuleException.class, () -> initAndGetSut().setSecret(SECRET_TRIMABLE));
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
	
	@Test
	public void shouldDecryptThrowExceptionWhenCipherTextIsNull() {
		assertThrows(BFSecureModuleException.class, () -> initAndGetSut().decrypt(null));
	}
	
	// TODO: implement multi thread
	@Disabled
	@Test
	public void shouldCreateMultiThread() throws InterruptedException {
		Collection<InstanceGetter> tasks = new ArrayList<>();
		for (int i = 0; i < THREAD_COUNT; i++) {
			tasks.add(new InstanceGetter());
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		List<Future<IDataEncryptionService>> encryptionServices = executor.invokeAll(tasks);
		executor.shutdown();
		executor.awaitTermination(100, TimeUnit.MILLISECONDS);
		
		encryptionServices.stream()
				.map(iDataEncryptionServiceFuture -> {
					try {
						return iDataEncryptionServiceFuture.get();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
					return null;
				})
				.forEach(s -> assertThat(s, is(equalTo(getSut()))));
	}
	
	public static class InstanceGetter implements Callable<IDataEncryptionService> {
		
		private static final CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT);
		
		@Override
		public IDataEncryptionService call() throws Exception {
			barrier.await();
			return getSut();
		}
	}
}
