package com.capgemini.mrchecker.test.core.utils.encryption.model;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import com.capgemini.mrchecker.test.core.base.encryption.IDataEncryptionService;
import com.capgemini.mrchecker.test.core.base.encryption.providers.DataEncryptionService;
import com.capgemini.mrchecker.test.core.logger.BFLogger;

public class DataEncryptionModel {
	
	private final IDataEncryptionService dataEncryptionService;
	
	public DataEncryptionModel(String secret) {
		dataEncryptionService = initAndGetSut(secret);
	}
	
	public String encrypt(String text) {
		return dataEncryptionService.encrypt(text);
	}
	
	public String decrypt(String text) {
		return dataEncryptionService.decrypt(text);
	}
	
	public void tearDownService() {
		DataEncryptionService.delInstance();
	}
	
	private static DataEncryptionService getService() {
		return (DataEncryptionService) DataEncryptionService.getInstance();
	}
	
	private static DataEncryptionService initAndGetSut(String secret) {
		try (FileInputStream secretSource = new FileInputStream(secret)) {
			DataEncryptionService.init(secretSource);
		} catch (IOException e) {
			try (ByteArrayInputStream secretSource = new ByteArrayInputStream(secret.getBytes())) {
				DataEncryptionService.init(secretSource);
			} catch (IOException ioException) {
				BFLogger.logInfo("Could not set secret because: " + ioException.getMessage());
			}
		}
		return getService();
	}
	
}
