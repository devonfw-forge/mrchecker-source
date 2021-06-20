package com.capgemini.mrchecker.test.core.utils.encryption;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import com.capgemini.mrchecker.test.core.tags.UnitTest;
import com.capgemini.mrchecker.test.core.utils.encryption.controller.DataEncryptionController;
import com.capgemini.mrchecker.test.core.utils.encryption.controller.IDataEncryptionController;
import com.capgemini.mrchecker.test.core.utils.encryption.exceptions.EncryptionServiceException;
import com.capgemini.mrchecker.test.core.utils.encryption.view.IDataEncryptionView;

@UnitTest
@ResourceLock(value = "SingleThread")
public class DataEncryptionControllerTest {
	private static final IDataEncryptionController dataEncryptionController = new DataEncryptionController();
	private static final GuiStub dataEncryptionGUI = new GuiStub();
	private static final String cipher = "ENC(nC9VLcpRUZcMOQGqruRSs3D+cKlZ6Ohl)";
	
	@BeforeAll
	public static void setup() {
		dataEncryptionController.setView(dataEncryptionGUI);
	}
	
	@Test
	public void shouldEncryptData() {
		dataEncryptionController.onEncrypt(new CryptParams("123456789", "password"));
		
		assertThat(dataEncryptionGUI.encryptionFieldValue, startsWith("ENC("));
	}
	
	@Test
	public void shouldDecryptData() {
		dataEncryptionController.onDecrypt(new CryptParams("123456789", cipher));
		
		assertThat(dataEncryptionGUI.decryptionFieldValue, is(equalTo("password")));
	}
	
	@Test
	public void shouldThrowExceptionWhenKeyIsTooShort() {
		assertThrows(EncryptionServiceException.class, () -> {
			dataEncryptionController.onEncrypt(new CryptParams("123", "password"));
		});
		assertThrows(EncryptionServiceException.class, () -> {
			dataEncryptionController.onDecrypt(new CryptParams("123", cipher));
		});
	}
	
	@Test
	public void shouldThrowExceptionWhenKeyIsEmpty() {
		assertThrows(EncryptionServiceException.class, () -> {
			dataEncryptionController.onEncrypt(new CryptParams("", "password"));
		});
		assertThrows(EncryptionServiceException.class, () -> {
			dataEncryptionController.onDecrypt(new CryptParams("", cipher));
		});
	}
	
	@Test
	public void shouldThrowExceptionWhenTextForDecryptionIsNotEncrypted() {
		assertThrows(EncryptionServiceException.class, () -> {
			dataEncryptionController.onDecrypt(new CryptParams("123456789", "not encrypted"));
		});
	}
	
	private static class GuiStub implements IDataEncryptionView {
		
		public String encryptionFieldValue;
		public String decryptionFieldValue;
		
		@Override
		public void setEncryptionFieldValue(String text) {
			encryptionFieldValue = text;
		}
		
		@Override
		public void setDecryptionFieldValue(String text) {
			decryptionFieldValue = text;
		}
	}
	
}
