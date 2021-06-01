package com.capgemini.mrchecker.test.core.utils.encryption.controller;

import static javax.swing.JOptionPane.showMessageDialog;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.*;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;

import com.capgemini.mrchecker.test.core.base.encryption.providers.DataEncryptionService;
import com.capgemini.mrchecker.test.core.exceptions.BFSecureModuleException;
import com.capgemini.mrchecker.test.core.utils.encryption.CryptParams;
import com.capgemini.mrchecker.test.core.utils.encryption.view.IView;

public class DataEncryptionControllerImp implements DataEncryptionController {
	private final IView view;
	
	public DataEncryptionControllerImp(IView view) {
		this.view = view;
	}
	
	@Override
	public void onEncrypt(CryptParams cryptParams) {
		try {
			DataEncryptionService.init(new ByteArrayInputStream(cryptParams.getSecret()
					.getBytes()));
			view.setEncryptionFieldValue(DataEncryptionService.getInstance()
					.encrypt(cryptParams.getText()));
		} catch (BFSecureModuleException e) {
			showMessageDialog(null, "Could not encrypt because:\n" + e.getMessage());
			view.setEncryptionFieldValue("");
		} finally {
			DataEncryptionService.delInstance();
		}
	}
	
	@Override
	public void onDecrypt(CryptParams cryptParams) {
		try {
			DataEncryptionService.init(new ByteArrayInputStream(cryptParams.getSecret()
					.getBytes()));
			view.setDecryptionFieldValue(DataEncryptionService.getInstance()
					.decrypt(cryptParams.getText()));
		} catch (BFSecureModuleException e) {
			showMessageDialog(null, "Could not decrypt because:\n" + e.getMessage());
			view.setEncryptionFieldValue("");
		} catch (EncryptionOperationNotPossibleException e) {
			showMessageDialog(null, "Could not decrypt because:\nWrong key for given encrypted text");
			view.setEncryptionFieldValue("");
		} finally {
			DataEncryptionService.delInstance();
		}
	}
	
	@Override
	public void onKey(JTextField keyTextField) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				view.setKeyFieldValue(Files.readAllLines(selectedFile.toPath())
						.stream()
						.findFirst()
						.orElseThrow(() -> new IOException("Empty file")), keyTextField);
			} catch (IOException e) {
				showMessageDialog(null, "Could not read key from given file\nError:" + e.getMessage());
			}
		} else {
			showMessageDialog(null, "You need to choose a file or provide the key");
		}
	}
}
