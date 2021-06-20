package com.capgemini.mrchecker.test.core.utils.encryption.controller;

import com.capgemini.mrchecker.test.core.utils.encryption.CryptParams;
import com.capgemini.mrchecker.test.core.utils.encryption.view.IDataEncryptionView;

public interface IDataEncryptionController {
	
	void onEncrypt(CryptParams cryptParams);
	
	void onDecrypt(CryptParams cryptParams);
	
	void setView(IDataEncryptionView view);
}
