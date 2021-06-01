package com.capgemini.mrchecker.test.core.utils.encryption.view;

import javax.swing.*;

import com.capgemini.mrchecker.test.core.utils.encryption.CryptParams;
import com.capgemini.mrchecker.test.core.utils.encryption.controller.DataEncryptionController;
import com.capgemini.mrchecker.test.core.utils.encryption.controller.DataEncryptionControllerImp;

public class DataEncryptionGUI extends JFrame implements IView {
	
	private final DataEncryptionController dataEncryptionController;
	
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
		dataEncryptionController = new DataEncryptionControllerImp(this);
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 600);
		setTitle("Data Encryption Service App");
		
		encryptionResultTextField.setEditable(false);
		decryptionResultTextField.setEditable(false);
		
		chooseEncryptionKeyButton.addActionListener(e -> dataEncryptionController.onKey(encryptionKeyTextField));
		chooseDecryptionKeyButton.addActionListener(e -> dataEncryptionController.onKey(decryptionKeyTextField));
		
		encryptButton.addActionListener(e -> dataEncryptionController.onEncrypt(new CryptParams(encryptionKeyTextField.getText(), encryptionTextField.getText())));
		decryptButton.addActionListener(e -> dataEncryptionController.onDecrypt(new CryptParams(decryptionKeyTextField.getText(), decryptionTextField.getText())));
		
		setVisible(true);
	}
	
	@Override
	public void setEncryptionFieldValue(String text) {
		encryptionResultTextField.setText(text);
	}
	
	@Override
	public void setDecryptionFieldValue(String text) {
		decryptionResultTextField.setText(text);
	}
	
	@Override
	public void setKeyFieldValue(String text, JTextField keyTextField) {
		keyTextField.setText(text);
	}
}
