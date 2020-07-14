package com.capgemini.mrchecker.test.core.base.environment.providers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.capgemini.mrchecker.test.core.base.encryption.IDataEncryptionService;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.base.runtime.RuntimeParametersCore;
import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.Singleton;

/**
 * This class is responsible for handling addresses of services. Addresses depends on currently set Environment. It is
 * possible to change environment before run adding -DenvURL parameter. Default Environment is set to DEV1
 *
 * @author LUKSTEF
 * @author MBABIARZ
 */

@Singleton
public class SpreadsheetEnvironmentService implements IEnvironmentService {
	
	private static IEnvironmentService instance;
	
	private List<CSVRecord>						records;
	private final Map<String, String>			services			= new HashMap<>();
	private Optional<IDataEncryptionService>	encryptionService	= Optional.empty();
	private String								environmentName;
	private int									envColumnNumber;
	
	private SpreadsheetEnvironmentService(String csvData, String environmentName) {
		fetchEnvData(csvData);
		setEnvironment(environmentName);
	}
	
	public static void init(String csvData, String environment) {
		if (Objects.isNull(instance)) {
			synchronized (SpreadsheetEnvironmentService.class) {
				if (Objects.isNull(instance)) {
					instance = new SpreadsheetEnvironmentService(csvData, environment);
				}
			}
		}
	}
	
	public static IEnvironmentService getInstance() {
		return SpreadsheetEnvironmentService.instance;
	}
	
	public static void delInstance() {
		SpreadsheetEnvironmentService.instance = null;
		RuntimeParametersCore.ENV.refreshParameterValue();
	}
	
	/**
	 * Sets environment (e.g. "QC1")
	 *
	 * @param environmentName
	 *            env to be set
	 */
	public void setEnvironment(String environmentName) {
		this.environmentName = environmentName;
		envColumnNumber = getEnvironmentColumnNumber(this.environmentName);
		updateServicesMapBasedOn();
	}
	
	@Override
	public String getEnvironment() {
		return environmentName;
	}
	
	/**
	 * @param serviceName
	 *            A name of an option to get
	 * @return value of service for current environment
	 */
	public String getValue(String serviceName) {
		String value = services.get(serviceName);
		if (Objects.isNull(value)) {
			throw new BFInputDataException("service " + serviceName + " " + "retrieve address of" + " " + "not found in available services table");
		}
		
		return value;
	}
	
	private void fetchEnvData(String csvData) throws BFInputDataException {
		try {
			CSVParser parser = CSVParser.parse(csvData, CSVFormat.RFC4180.withIgnoreSurroundingSpaces());
			records = parser.getRecords();
		} catch (IOException e) {
			throw new BFInputDataException("Unable to parse CSV data: " + csvData);
		}
	}
	
	private void updateServicesMapBasedOn() {
		services.clear();
		
		Iterator<CSVRecord> it = records.iterator();
		it.next(); // first row contains table headers, so skip it
		while (it.hasNext()) {
			CSVRecord record = it.next();
			String key = record.get(0);
			String value = record.get(envColumnNumber)
					.trim();
			value = optionalDecrypt(value);
			services.put(key, value);
		}
	}
	
	private String optionalDecrypt(String value) {
		if (encryptionService.isPresent() && encryptionService.get()
				.isEncrypted(value)) {
			value = encryptionService.get()
					.decrypt(value);
		}
		
		return value;
	}
	
	private int getEnvironmentColumnNumber(String environmentName) throws BFInputDataException {
		CSVRecord header = records.get(0);
		for (int environmentNumber = 0; environmentNumber < header.size(); environmentNumber++) {
			String environment = header.get(environmentNumber);
			if (environment.equals(environmentName)) {
				BFLogger.logInfo("Selected Environment: " + environmentName);
				return environmentNumber;
			}
		}
		
		throw new BFInputDataException("There is no Environment with name '" + environmentName + "' available");
	}
	
	@Override
	public void setDataEncryptionService(IDataEncryptionService dataEncryptionService) {
		encryptionService = Optional.ofNullable(dataEncryptionService);
		updateServicesMapBasedOn();
	}
}
