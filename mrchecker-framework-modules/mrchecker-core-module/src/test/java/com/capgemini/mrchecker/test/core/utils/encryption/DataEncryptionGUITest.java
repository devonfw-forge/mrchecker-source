package com.capgemini.mrchecker.test.core.utils.encryption;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.awt.*;

import org.assertj.swing.core.EmergencyAbortListener;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.ResourceLock;

import com.capgemini.mrchecker.test.core.tags.UnitTest;
import com.capgemini.mrchecker.test.core.utils.encryption.controller.DataEncryptionController;
import com.capgemini.mrchecker.test.core.utils.encryption.controller.IDataEncryptionController;
import com.capgemini.mrchecker.test.core.utils.encryption.exceptions.EncryptionServiceException;
import com.capgemini.mrchecker.test.core.utils.encryption.view.DataEncryptionGUI;
import com.capgemini.mrchecker.test.core.utils.encryption.view.IDataEncryptionView;

@UnitTest
@ResourceLock(value = "SingleThread")
@Disabled("Due to missing GUI on Jenkins")
public class DataEncryptionGUITest {
	private static final IDataEncryptionController dataEncryptionController = mock(DataEncryptionController.class);
	private IDataEncryptionView dataEncryptionGUI;
	private FrameFixture dataEncryptionGUIFixture;
	private EmergencyAbortListener listener;
	private static final String secretNumber = "123456789";
	private static final String value = "password";
	private static final String cipher = "ENC(nC9VLcpRUZcMOQGqruRSs3D+cKlZ6Ohl)";
	
	@BeforeAll
	public static void setupBeforeAll() {
		FailOnThreadViolationRepaintManager.install();
	}
	
	@BeforeEach
	public void setupBeforeEveryTest() {
		dataEncryptionGUI = GuiActionRunner.execute(() -> new DataEncryptionGUI(dataEncryptionController));
		dataEncryptionController.setView(dataEncryptionGUI);
		listener = EmergencyAbortListener.registerInToolkit();
		dataEncryptionGUIFixture = new FrameFixture((Frame) dataEncryptionGUI);
	}
	
	@Test
	public void shouldEncryptDataWithGivenSecretAndValue() {
		enterSecretAndTextForEncryption(secretNumber);
		mockEncryption();
		String result = clickEncryptAndGetResult();
		
		assertThat(result, is(equalTo(cipher)));
	}
	
	@Test
	public void shouldSetSecretFromFileAndEncrypt() {
		enterSecretAndTextForEncryption();
		mockEncryptionFileChoose();
		mockEncryption();
		String result = clickEncryptAndGetResult();
		
		assertThat(result, is(equalTo(cipher)));
	}
	
	@Test
	public void shouldShowMessageDialogWhenSecretIsEmptyForEncryption() {
		enterSecretAndTextForEncryption();
		String result = clickEncryptAndGetPopup();
		
		assertThat(result, isEmptyString());
	}
	
	@Test
	public void shouldShowMessageDialogWhenSecretIsTooShortForEncryption() {
		enterSecretAndTextForEncryption("123");
		String result = clickEncryptAndGetPopup();
		
		assertThat(result, isEmptyString());
	}
	
	@Test
	public void shouldShowMessageDialogWhenCancelFileChooserForEncryption() {
		enterSecretAndTextForEncryption();
		cancelEncryptionFileChooserAndGetPopup();
		String keyValue = dataEncryptionGUIFixture.textBox("encryptionKeyTextField")
				.text();
		String result = dataEncryptionGUIFixture.textBox("encryptionResultTextField")
				.text();
		
		assertThat(keyValue, isEmptyString());
		assertThat(result, isEmptyString());
	}
	
	@Test
	public void shouldDecryptDataWithGivenSecretAndValue() {
		enterSecretAndTextForDecryption(secretNumber, cipher);
		mockDecryption();
		String result = clickDecryptAndGetResult();
		
		assertThat(result, is(equalTo(value)));
	}
	
	@Test
	public void shouldSetSecretFromFileAndDecrypt() {
		enterSecretAndTextForDecryption();
		mockDecryptionFileChoose();
		mockDecryption();
		String result = clickDecryptAndGetResult();
		
		assertThat(result, is(equalTo(value)));
	}
	
	@Test
	public void shouldShowMessageDialogWhenSecretIsEmptyForDecryption() {
		enterSecretAndTextForDecryption();
		String result = clickDecryptAndGetPopup();
		
		assertThat(result, isEmptyString());
	}
	
	@Test
	public void shouldShowMessageDialogWhenSecretIsTooShortForDecryption() {
		enterSecretAndTextForDecryption("123", cipher);
		String result = clickDecryptAndGetPopup();
		
		assertThat(result, isEmptyString());
	}
	
	@Test
	public void shouldShowMessageDialogWhenValueIsNotEncryptedForDecryption() {
		enterSecretAndTextForDecryption(secretNumber, value);
		String result = clickDecryptAndGetPopup();
		
		assertThat(result, isEmptyString());
	}
	
	@Test
	public void shouldShowMessageDialogWhenCancelFileChooserForDecryption() {
		enterSecretAndTextForDecryption();
		cancelDecryptionFileChooserAndGetPopup();
		String keyValue = dataEncryptionGUIFixture.textBox("decryptionKeyTextField")
				.text();
		String result = dataEncryptionGUIFixture.textBox("decryptionResultTextField")
				.text();
		
		assertThat(keyValue, isEmptyString());
		assertThat(result, isEmptyString());
	}
	
	@Test
	public void shouldCorrectlyEncryptAndThenDecryptValue() {
		enterSecretAndTextForEncryption();
		mockEncryptionFileChoose();
		mockEncryption();
		String encryptionResult = clickEncryptAndGetResult();
		enterSecretAndTextForDecryption(secretNumber, encryptionResult);
		mockDecryptionFileChoose();
		mockDecryption();
		String decryptionResult = clickDecryptAndGetResult();
		
		assertThat(encryptionResult, is(equalTo(cipher)));
		assertThat(decryptionResult, is(equalTo(value)));
	}
	
	private void enterSecretAndTextForEncryption(String secret) {
		dataEncryptionGUIFixture.textBox("encryptionKeyTextField")
				.enterText(secret);
		dataEncryptionGUIFixture.textBox("encryptionTextField")
				.enterText(value);
	}
	
	private void enterSecretAndTextForEncryption() {
		enterSecretAndTextForEncryption("");
	}
	
	private void mockEncryption() {
		doAnswer(i -> {
			dataEncryptionGUI.setEncryptionFieldValue(cipher);
			return null;
		}).when(dataEncryptionController)
				.onEncrypt(any(CryptParams.class));
	}
	
	private void mockEncryptionFileChoose() {
		dataEncryptionGUI.setStringSupplier(secretNumber);
		dataEncryptionGUIFixture.button("chooseEncryptionKeyButton")
				.click();
	}
	
	private void mockEncryptionThrowForTooShortOrEmptySecret() {
		doThrow(EncryptionServiceException.class).when(dataEncryptionController)
				.onEncrypt(any(CryptParams.class));
	}
	
	private String clickEncryptAndGetResult() {
		dataEncryptionGUIFixture.button("encryptButton")
				.click();
		return dataEncryptionGUIFixture.textBox("encryptionResultTextField")
				.text();
	}
	
	private String clickEncryptAndGetPopup() {
		mockEncryptionThrowForTooShortOrEmptySecret();
		dataEncryptionGUIFixture.button("encryptButton")
				.click();
		dataEncryptionGUIFixture.dialog()
				.button()
				.click();
		return dataEncryptionGUIFixture.textBox("encryptionResultTextField")
				.text();
	}
	
	private void cancelEncryptionFileChooserAndGetPopup() {
		dataEncryptionGUIFixture.button("chooseEncryptionKeyButton")
				.click();
		dataEncryptionGUIFixture.fileChooser()
				.cancel();
		dataEncryptionGUIFixture.dialog()
				.button()
				.click();
	}
	
	private void enterSecretAndTextForDecryption(String secret, String text) {
		dataEncryptionGUIFixture.tabbedPane()
				.selectTab("Decryption");
		dataEncryptionGUIFixture.textBox("decryptionKeyTextField")
				.enterText(secret);
		dataEncryptionGUIFixture.textBox("decryptionTextField")
				.enterText(text);
	}
	
	private void enterSecretAndTextForDecryption() {
		enterSecretAndTextForDecryption("", cipher);
	}
	
	private void mockDecryption() {
		doAnswer(i -> {
			dataEncryptionGUI.setDecryptionFieldValue(value);
			return null;
		}).when(dataEncryptionController)
				.onDecrypt(any(CryptParams.class));
	}
	
	private void mockDecryptionFileChoose() {
		dataEncryptionGUI.setStringSupplier(secretNumber);
		dataEncryptionGUIFixture.button("chooseDecryptionKeyButton")
				.click();
	}
	
	private void mockDecryptionThrowForTooShortOrEmptySecret() {
		doThrow(EncryptionServiceException.class).when(dataEncryptionController)
				.onDecrypt(any(CryptParams.class));
	}
	
	private String clickDecryptAndGetResult() {
		dataEncryptionGUIFixture.button("decryptButton")
				.click();
		return dataEncryptionGUIFixture.textBox("decryptionResultTextField")
				.text();
	}
	
	private String clickDecryptAndGetPopup() {
		mockDecryptionThrowForTooShortOrEmptySecret();
		dataEncryptionGUIFixture.button("decryptButton")
				.click();
		dataEncryptionGUIFixture.dialog()
				.button()
				.click();
		return dataEncryptionGUIFixture.textBox("decryptionResultTextField")
				.text();
	}
	
	private void cancelDecryptionFileChooserAndGetPopup() {
		dataEncryptionGUIFixture.button("chooseDecryptionKeyButton")
				.click();
		dataEncryptionGUIFixture.fileChooser()
				.cancel();
		dataEncryptionGUIFixture.dialog()
				.button()
				.click();
	}
	
	@AfterEach
	public void tearDown() {
		listener.unregister();
		dataEncryptionGUIFixture.cleanUp();
	}
	
}
