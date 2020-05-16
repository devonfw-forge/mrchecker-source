package com.capgemini.mrchecker.test.core.base.encryption;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import com.capgemini.mrchecker.test.core.base.encryption.providers.DataEncryptionService;
import com.capgemini.mrchecker.test.core.exceptions.BFSecureModuleException;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class DataEncryptionModule extends AbstractModule {
	
	private static final String	DEFAULT_SECRET_DATA_FILE	= "/src/resources/secretData";
	private final String		secretFilePath;
	
	public DataEncryptionModule(String secretFilePath) {
		this.secretFilePath = secretFilePath;
	}
	
	public DataEncryptionModule() {
		this(DEFAULT_SECRET_DATA_FILE);
	}
	
	@Override
	protected void configure() {
		
	}
	
	@Provides
	@Singleton
	IDataEncryptionService provideSpreadsheetEnvironmentService() {
		String path = System.getProperty("user.dir") + Paths.get(secretFilePath);
		try (InputStream secretSource = new FileInputStream(path)) {
			DataEncryptionService.init(secretSource);
		} catch (FileNotFoundException e) {
			throw new BFSecureModuleException("File with secret not found: " + path);
		} catch (IOException e) {
			throw new BFSecureModuleException("File with secret processing exception: " + path);
		}
		
		return DataEncryptionService.getInstance();
	}
}
