package com.capgemini.mrchecker.test.core.base.environment;

import com.capgemini.mrchecker.test.core.base.environment.providers.SpreadsheetEnvironmentService;
import com.capgemini.mrchecker.test.core.base.runtime.RuntimeParametersCore;
import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EnvironmentModule extends AbstractModule {

    private static final String DEFAULT_ENV_PARAMS_FILE = "/src/resources/environments/environments.csv";
    private final String envParamsFilePath;

    public EnvironmentModule(String envParamsFilePath) {
        this.envParamsFilePath = envParamsFilePath;
    }

    public EnvironmentModule() {
        this(DEFAULT_ENV_PARAMS_FILE);
    }

    @Override
    protected void configure() {

    }

    @Provides
    @SuppressWarnings("unused")
    IEnvironmentService provideSpreadsheetEnvironmentService() {
        String environment = RuntimeParametersCore.ENV.getValue();
        String path = System.getProperty("user.dir") + Paths.get(envParamsFilePath);
        String csvData;
        try {
            csvData = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new BFInputDataException("Spreadsheet file could not be processed: " + path);
        }

        SpreadsheetEnvironmentService.init(csvData, environment);
        return SpreadsheetEnvironmentService.getInstance();
    }
}