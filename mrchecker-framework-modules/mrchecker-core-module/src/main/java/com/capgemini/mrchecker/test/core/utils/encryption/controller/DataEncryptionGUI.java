package com.capgemini.mrchecker.test.core.utils.encryption.controller;

import java.io.File;
import java.util.Objects;

import javax.swing.*;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;

import com.capgemini.mrchecker.test.core.exceptions.BFSecureModuleException;
import com.capgemini.mrchecker.test.core.utils.encryption.model.DataEncryptionModel;

public class DataEncryptionGUI extends JFrame {
	private DataEncryptionModel dataEncryptionModel;
	private JPanel contentPane;
	private JTabbedPane mainPane;
	private JPanel encryptionPane;
	private JPanel decryptionPane;
	private JTextField encryptionTextField;
	private JTextField encryptionKeyTextField;
	private JButton chooseEncryptionKeyButton;
	private JButton encryptButton;
	private JLabel encryptionKeyLabel;
	private JLabel encryptionLabel;
	private JLabel encryptionResultLabel;
	private JTextField encryptionResultTextField;
	private JTextField decryptionTextField;
	private JTextField decryptionResultTextField;
	private JButton decryptButton;
	private JButton chooseDecryptionKeyButton;
	private JTextField decryptionKeyTextField;
	private JLabel decryptionKeyLabel;
	private JLabel decryptionLabel;
	private JLabel decryptionResultLabel;
	
	public DataEncryptionGUI() {
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 400);
		setTitle("Data Encryption Service App");
		
		encryptionResultTextField.setEditable(false);
		decryptionResultTextField.setEditable(false);
		
		chooseEncryptionKeyButton.addActionListener(e -> onChooseKey(encryptionKeyTextField));
		chooseDecryptionKeyButton.addActionListener(e -> onChooseKey(decryptionKeyTextField));
		
		encryptButton.addActionListener(e -> onEncryption());
		decryptButton.addActionListener(e -> onDecryption());
		
		setVisible(true);
	}
	
	private void onChooseKey(JTextField keyTextField) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			keyTextField.setText(selectedFile.getAbsolutePath());
		} else {
			keyTextField.setText("You need to choose a file or provide the key");
		}
	}
	
	private void onEncryption() {
		try {
			dataEncryptionModel = new DataEncryptionModel(encryptionKeyTextField.getText());
			encryptionResultTextField.setText(dataEncryptionModel.encrypt(encryptionTextField.getText()));
		} catch (BFSecureModuleException e) {
			encryptionResultTextField.setText(e.getMessage());
		} finally {
			if (Objects.nonNull(dataEncryptionModel)) {
				dataEncryptionModel.tearDownService();
			}
		}
	}
	
	private void onDecryption() {
		try {
			dataEncryptionModel = new DataEncryptionModel(decryptionKeyTextField.getText());
			decryptionResultTextField.setText(dataEncryptionModel.decrypt(decryptionTextField.getText()));
		} catch (BFSecureModuleException e) {
			decryptionResultTextField.setText(e.getMessage());
		} catch (EncryptionOperationNotPossibleException e) {
			decryptionResultTextField.setText("Wrong key - could not decrypt message");
		} finally {
			if (Objects.nonNull(dataEncryptionModel)) {
				dataEncryptionModel.tearDownService();
			}
		}
	}
	
}
