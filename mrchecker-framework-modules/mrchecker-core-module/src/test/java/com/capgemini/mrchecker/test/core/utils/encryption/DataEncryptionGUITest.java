package com.capgemini.mrchecker.test.core.utils.encryption;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringStartsWith.startsWith;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.assertj.swing.core.EmergencyAbortListener;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import com.capgemini.mrchecker.test.core.tags.IntegrationTest;
import com.capgemini.mrchecker.test.core.utils.encryption.controller.DataEncryptionController;
import com.capgemini.mrchecker.test.core.utils.encryption.controller.IDataEncryptionController;
import com.capgemini.mrchecker.test.core.utils.encryption.view.DataEncryptionGUI;
import com.capgemini.mrchecker.test.core.utils.encryption.view.IDataEncryptionView;

@IntegrationTest
@ResourceLock(value = "SingleThread")
public class DataEncryptionGUITest {
	private static final IDataEncryptionController dataEncryptionController = new DataEncryptionController();
	private FrameFixture dataEncryptionGUIFixture;
	private EmergencyAbortListener listener;
	private static final String secret = "123456789";
	private static final String value = "password";
	private static final String cipher = "ENC(nC9VLcpRUZcMOQGqruRSs3D+cKlZ6Ohl)";
	private static final String cipherForSecretDataFile = "ENC(baAZgn+nIcS0eXFLaRiVVGqxjbtt9tP7)";
	
	@BeforeAll
	public static void setupBeforeAll() {
		FailOnThreadViolationRepaintManager.install();
	}
	
	@BeforeEach
	public void setupBeforeEveryTest() {
		IDataEncryptionView dataEncryptionGUI = GuiActionRunner.execute(() -> new DataEncryptionGUI(dataEncryptionController));
		dataEncryptionController.setView(dataEncryptionGUI);
		listener = EmergencyAbortListener.registerInToolkit();
		dataEncryptionGUIFixture = new FrameFixture((Frame) dataEncryptionGUI);
	}
	
	@Test
	public void shouldEncryptDataWithGivenSecretAndValue() {
		CryptParams cryptParams = new CryptParams(secret, value);
		dataEncryptionGUIFixture.textBox("encryptionKeyTextField")
				.enterText(cryptParams.getSecret());
		dataEncryptionGUIFixture.textBox("encryptionTextField")
				.enterText(cryptParams.getText());
		dataEncryptionGUIFixture.button("encryptButton")
				.click();
		String result = dataEncryptionGUIFixture.textBox("encryptionResultTextField")
				.text();
		
		assertThat(result, startsWith("ENC("));
	}
	
	@Test
	public void shouldSetSecretFromFileAndEncrypt() throws IOException {
		dataEncryptionGUIFixture.textBox("encryptionTextField")
				.enterText(value);
		dataEncryptionGUIFixture.button("chooseEncryptionKeyButton")
				.click();
		dataEncryptionGUIFixture.fileChooser()
				.setCurrentDirectory(new File(System.getProperty("user.dir")))
				.selectFile(new File(System.getProperty("user.dir") + "/src/resources/secretData"))
				.approve();
		dataEncryptionGUIFixture.button("encryptButton")
				.click();
		String keyValue = dataEncryptionGUIFixture.textBox("encryptionKeyTextField")
				.text();
		String result = dataEncryptionGUIFixture.textBox("encryptionResultTextField")
				.text();
		String key = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/resources/secretData"))).trim();
		
		assertThat(keyValue, is(equalTo(key)));
		assertThat(result, startsWith("ENC("));
	}
	
	@Test
	public void shouldShowMessageDialogWhenSecretIsEmptyForEncryption() {
		CryptParams cryptParams = new CryptParams("", value);
		dataEncryptionGUIFixture.textBox("encryptionKeyTextField")
				.enterText(cryptParams.getSecret());
		dataEncryptionGUIFixture.textBox("encryptionTextField")
				.enterText(cryptParams.getText());
		dataEncryptionGUIFixture.button("encryptButton")
				.click();
		dataEncryptionGUIFixture.dialog()
				.button()
				.click();
		
		String result = dataEncryptionGUIFixture.textBox("encryptionResultTextField")
				.text();
		
		assertThat(result, is(equalTo("")));
	}
	
	@Test
	public void shouldShowMessageDialogWhenSecretIsTooShortForEncryption() {
		CryptParams cryptParams = new CryptParams("123", value);
		dataEncryptionGUIFixture.textBox("encryptionKeyTextField")
				.enterText(cryptParams.getSecret());
		dataEncryptionGUIFixture.textBox("encryptionTextField")
				.enterText(cryptParams.getText());
		dataEncryptionGUIFixture.button("encryptButton")
				.click();
		dataEncryptionGUIFixture.dialog()
				.button()
				.click();
		
		String result = dataEncryptionGUIFixture.textBox("encryptionResultTextField")
				.text();
		
		assertThat(result, is(equalTo("")));
	}
	
	@Test
	public void shouldShowMessageDialogWhenCancelFileChooserForEncryption() {
		dataEncryptionGUIFixture.textBox("encryptionTextField")
				.enterText(value);
		dataEncryptionGUIFixture.button("chooseEncryptionKeyButton")
				.click();
		dataEncryptionGUIFixture.fileChooser()
				.cancel();
		dataEncryptionGUIFixture.dialog()
				.button()
				.click();
		String keyValue = dataEncryptionGUIFixture.textBox("encryptionKeyTextField")
				.text();
		String result = dataEncryptionGUIFixture.textBox("encryptionResultTextField")
				.text();
		
		assertThat(keyValue, is(equalTo("")));
		assertThat(result, is(equalTo("")));
	}
	
	@Test
	public void shouldDecryptDataWithGivenSecretAndValue() {
		dataEncryptionGUIFixture.tabbedPane()
				.selectTab("Decryption");
		CryptParams cryptParams = new CryptParams(secret, cipher);
		dataEncryptionGUIFixture.textBox("decryptionKeyTextField")
				.enterText(cryptParams.getSecret());
		dataEncryptionGUIFixture.textBox("decryptionTextField")
				.enterText(cryptParams.getText());
		dataEncryptionGUIFixture.button("decryptButton")
				.click();
		String result = dataEncryptionGUIFixture.textBox("decryptionResultTextField")
				.text();
		
		assertThat(result, is(equalTo(value)));
	}
	
	@Test
	public void shouldSetSecretFromFileAndDecrypt() throws IOException {
		dataEncryptionGUIFixture.tabbedPane()
				.selectTab("Decryption");
		dataEncryptionGUIFixture.textBox("decryptionTextField")
				.enterText(cipherForSecretDataFile);
		dataEncryptionGUIFixture.button("chooseDecryptionKeyButton")
				.click();
		dataEncryptionGUIFixture.fileChooser()
				.setCurrentDirectory(new File(System.getProperty("user.dir")))
				.selectFile(new File(System.getProperty("user.dir") + "/src/resources/secretData"))
				.approve();
		dataEncryptionGUIFixture.button("decryptButton")
				.click();
		String keyValue = dataEncryptionGUIFixture.textBox("decryptionKeyTextField")
				.text();
		String result = dataEncryptionGUIFixture.textBox("decryptionResultTextField")
				.text();
		String key = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/resources/secretData"))).trim();
		
		assertThat(keyValue, is(equalTo(key)));
		assertThat(result, is(equalTo(value)));
	}
	
	@Test
	public void shouldShowMessageDialogWhenSecretIsEmptyForDecryption() {
		dataEncryptionGUIFixture.tabbedPane()
				.selectTab("Decryption");
		CryptParams cryptParams = new CryptParams("", cipher);
		dataEncryptionGUIFixture.textBox("decryptionKeyTextField")
				.enterText(cryptParams.getSecret());
		dataEncryptionGUIFixture.textBox("decryptionTextField")
				.enterText(cryptParams.getText());
		dataEncryptionGUIFixture.button("decryptButton")
				.click();
		dataEncryptionGUIFixture.dialog()
				.button()
				.click();
		
		String result = dataEncryptionGUIFixture.textBox("decryptionResultTextField")
				.text();
		
		assertThat(result, is(equalTo("")));
	}
	
	@Test
	public void shouldShowMessageDialogWhenSecretIsTooShortForDecryption() {
		dataEncryptionGUIFixture.tabbedPane()
				.selectTab("Decryption");
		CryptParams cryptParams = new CryptParams("123", cipher);
		dataEncryptionGUIFixture.textBox("decryptionKeyTextField")
				.enterText(cryptParams.getSecret());
		dataEncryptionGUIFixture.textBox("decryptionTextField")
				.enterText(cryptParams.getText());
		dataEncryptionGUIFixture.button("decryptButton")
				.click();
		dataEncryptionGUIFixture.dialog()
				.button()
				.click();
		
		String result = dataEncryptionGUIFixture.textBox("decryptionResultTextField")
				.text();
		
		assertThat(result, is(equalTo("")));
	}
	
	@Test
	public void shouldShowMessageDialogWhenValueIsNotEncryptedForDecryption() {
		dataEncryptionGUIFixture.tabbedPane()
				.selectTab("Decryption");
		CryptParams cryptParams = new CryptParams(secret, value);
		dataEncryptionGUIFixture.textBox("decryptionKeyTextField")
				.enterText(cryptParams.getSecret());
		dataEncryptionGUIFixture.textBox("decryptionTextField")
				.enterText(cryptParams.getText());
		dataEncryptionGUIFixture.button("decryptButton")
				.click();
		dataEncryptionGUIFixture.dialog()
				.button()
				.click();
		
		String result = dataEncryptionGUIFixture.textBox("decryptionResultTextField")
				.text();
		
		assertThat(result, is(equalTo("")));
	}
	
	@Test
	public void shouldShowMessageDialogWhenCancelFileChooserForDecryption() {
		dataEncryptionGUIFixture.tabbedPane()
				.selectTab("Decryption");
		dataEncryptionGUIFixture.textBox("decryptionTextField")
				.enterText(value);
		dataEncryptionGUIFixture.button("chooseDecryptionKeyButton")
				.click();
		dataEncryptionGUIFixture.fileChooser()
				.cancel();
		dataEncryptionGUIFixture.dialog()
				.button()
				.click();
		String keyValue = dataEncryptionGUIFixture.textBox("decryptionKeyTextField")
				.text();
		String result = dataEncryptionGUIFixture.textBox("decryptionResultTextField")
				.text();
		
		assertThat(keyValue, is(equalTo("")));
		assertThat(result, is(equalTo("")));
	}
	
	@Test
	public void shouldCorrectlyEncryptAndThenDecryptValue() throws IOException {
		dataEncryptionGUIFixture.textBox("encryptionTextField")
				.enterText(value);
		dataEncryptionGUIFixture.button("chooseEncryptionKeyButton")
				.click();
		dataEncryptionGUIFixture.fileChooser()
				.setCurrentDirectory(new File(System.getProperty("user.dir")))
				.selectFile(new File(System.getProperty("user.dir") + "/src/resources/secretData"))
				.approve();
		dataEncryptionGUIFixture.button("encryptButton")
				.click();
		String encryptionKeyValue = dataEncryptionGUIFixture.textBox("encryptionKeyTextField")
				.text();
		String encryptionResult = dataEncryptionGUIFixture.textBox("encryptionResultTextField")
				.text();
		String encryptionKey = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/resources/secretData"))).trim();
		
		dataEncryptionGUIFixture.tabbedPane()
				.selectTab("Decryption");
		dataEncryptionGUIFixture.textBox("decryptionTextField")
				.enterText(encryptionResult);
		dataEncryptionGUIFixture.button("chooseDecryptionKeyButton")
				.click();
		dataEncryptionGUIFixture.fileChooser()
				.setCurrentDirectory(new File(System.getProperty("user.dir")))
				.selectFile(new File(System.getProperty("user.dir") + "/src/resources/secretData"))
				.approve();
		dataEncryptionGUIFixture.button("decryptButton")
				.click();
		String decryptionKeyValue = dataEncryptionGUIFixture.textBox("decryptionKeyTextField")
				.text();
		String decryptionResult = dataEncryptionGUIFixture.textBox("decryptionResultTextField")
				.text();
		String decryptionKey = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/resources/secretData"))).trim();
		
		assertThat(encryptionKeyValue, is(equalTo(encryptionKey)));
		assertThat(decryptionKeyValue, is(equalTo(decryptionKey)));
		assertThat(encryptionKeyValue, is(equalTo(decryptionKeyValue)));
		
		assertThat(encryptionResult, startsWith("ENC("));
		assertThat(decryptionResult, is(equalTo(value)));
	}
	
	@AfterEach
	public void tearDown() {
		listener.unregister();
		dataEncryptionGUIFixture.cleanUp();
	}
	
}
