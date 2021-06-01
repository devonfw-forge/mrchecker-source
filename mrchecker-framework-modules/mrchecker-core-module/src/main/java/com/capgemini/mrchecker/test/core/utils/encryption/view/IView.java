package com.capgemini.mrchecker.test.core.utils.encryption.view;

import javax.swing.*;

public interface IView {
	void setEncryptionFieldValue(String text);
	
	void setDecryptionFieldValue(String text);
	
	void setKeyFieldValue(String text, JTextField keyTextField);
}
