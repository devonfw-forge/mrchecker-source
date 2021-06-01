package com.capgemini.mrchecker.test.core.utils.encryption.controller;

import javax.swing.*;

import com.capgemini.mrchecker.test.core.utils.encryption.CryptParams;

public interface DataEncryptionController {
	
	void onEncrypt(CryptParams cryptParams);
	
	void onDecrypt(CryptParams cryptParams);
	
	void onKey(JTextField keyTextField);
}
