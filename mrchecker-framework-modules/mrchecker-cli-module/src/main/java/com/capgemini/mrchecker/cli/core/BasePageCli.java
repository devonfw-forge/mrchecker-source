package com.capgemini.mrchecker.cli.core;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.capgemini.mrchecker.cli.core.base.properties.PropertiesCli;
import com.capgemini.mrchecker.cli.core.base.runtime.RuntimeParameters;
import com.capgemini.mrchecker.cli.core.exceptions.BFCliException;
import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.analytics.IAnalytics;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.logger.BFLoggerInstance;
import com.google.inject.Guice;

abstract public class BasePageCli extends Page {

    private static IEnvironmentService environmentService;
    private final static IAnalytics ANALYTICS;
    public final static String ANALYTICS_CATEGORY_NAME = "CLI-MODULE";

    private final static PropertiesCli PROPERTIES_CLI;

    private Process process;
    private PrintWriter input;
    private BufferedReader responseReader;
    private BufferedReader errorReader;
    private final StringBuffer response = new StringBuffer();

    private final BFLoggerInstance bfLogger = BFLogger.getLog();

    static {
        // Get analytics instance created in BaseTest
        ANALYTICS = BaseTest.getAnalytics();

        // Get and then set properties information from selenium.settings file
        PROPERTIES_CLI = setPropertiesSettings();

        // Read System or maven parameters
        setRuntimeParametersCli();

        // Read Environment variables either from environments.csv or any other input data.
        setEnvironmentInstance();
    }

    public static IAnalytics getAnalytics() {
        return BasePageCli.ANALYTICS;
    }

    private final List<String> baseCommand = new ArrayList<>();

    public BasePageCli(String... defaultCommand) {
        baseCommand.addAll(Arrays.asList(defaultCommand));
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.CLI;
    }

    protected void executeCommand() {
        doExecuteCommand(baseCommand);
    }

    protected void executeCommand(String... params) {
        List<String> command = new ArrayList<>();
        command.addAll(baseCommand);
        command.addAll(Arrays.asList(params));
        doExecuteCommand(command);
    }

    private void doExecuteCommand(List<String> command) {
        try {
            createProcess(command);
            openStreams();
            startOutputWatcher();
        } catch (IOException e) {
            throw new BFCliException(e);
        }
    }



	private void createProcess(List<String> command) throws IOException {
        if (Objects.isNull(process) || !process.isAlive()) {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.environment().putAll(getEnvParams());
            process = pb.start();
            BFLogger.logDebug("Process: " + process.toString());
        }
    }

    protected Map<? extends String,? extends String> getEnvParams() {
        return Collections.emptyMap();
    }

    private void openStreams() {
        responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
        input = new PrintWriter(process.getOutputStream());
    }


	private void startOutputWatcher() {
    	new Thread(new ResponseReader()).start();
	}

    @Override
    public void onTestClassFinish() {
        super.onTestClassFinish();
        closeStreams();
        closeProcess();
    }

    private void closeStreams() {
        for (Closeable closeable : new Closeable[]{input, responseReader, errorReader}) {
            if (!Objects.isNull(closeable)) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    throw new BFCliException(e);
                }
            }
        }
    }

    private void closeProcess() {
        if (Objects.isNull(process)) {
            BFLogger.logDebug("closeProcess() was called but there was no process for this page.");
        } else {
            try {
                BFLogger.logDebug("Closing process for this page.");
                if (process.isAlive()) {
                    process.destroyForcibly();
                }
            } catch (Exception e) {
                BFLogger.logDebug("Ooops! Something went wrong while closing the process: " + e.getMessage());
            } finally {
                process = null;
            }
        }
    }

    public boolean hasResponse() {
        return response.length() > 0;
    }

    public boolean hasErrors() {
        return hasStreamAvailableBytes(errorReader);
    }

    public boolean isAlive() {
        return process.isAlive();
    }

    public int waitToFinish() {
        try {
            return process.waitFor();
        } catch (InterruptedException e) {
            throw new BFCliException(e);
        }
    }

    public void writeCommand(String command) {
        input.println(command);
        input.flush();
    }

    public String readResponse() {
        return response.toString();
    }

    public String readError() {
        return getLinesFromReader(errorReader);
    }

    private String getLinesFromReader(BufferedReader reader) {
        try {
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1024];
            while (hasStreamAvailableBytes(reader)) {
                int read = reader.read(buffer);
                sb.append(buffer, 0, read);
                bfLogger.logDebug(new String(Arrays.copyOf(buffer, read)));
            }
            return sb.toString();
        } catch (IOException e) {
            throw new BFCliException(e);
        }
    }

    private boolean hasStreamAvailableBytes(BufferedReader reader) {
        try {
            return reader.ready();
        } catch (IOException e) {
            throw new BFCliException(e);
        }
    }

    private static PropertiesCli setPropertiesSettings() {
        // Get and then set properties information from settings.properties file
        return Guice.createInjector(PropertiesSettingsModule.init())
                .getInstance(PropertiesCli.class);
    }

    private static void setRuntimeParametersCli() {
        // Read System or maven parameters
        BFLogger.logDebug(java.util.Arrays.asList(RuntimeParameters.values())
                .toString());
    }

    private static void setEnvironmentInstance() {
        /*
         * Environment variables either from environmnets.csv or any other input data. For now there is no properties
         * settings file for Selenium module. In future, please have a look on Core Module IEnvironmentService
         * environmetInstance = Guice.createInjector(new EnvironmentModule()) .getInstance(IEnvironmentService.class);
         */
    }

    private class ResponseReader implements Runnable {

		@Override
		public void run() {
				try {
                    while(isAlive() || responseReader.ready()) {
                        response.append(getLinesFromReader(responseReader));
                        Thread.sleep(500);
                    }
				} catch (InterruptedException | IOException e) {
					throw new BFCliException();
				}
		}
	}
}