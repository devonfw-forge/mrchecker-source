package com.capgemini.mrchecker.cli.core;

import com.capgemini.mrchecker.cli.core.exceptions.BFCliException;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.logger.BFLoggerInstance;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

abstract public class BasePageCli extends Page {

    private static IEnvironmentService environmentService;

    private Process process;
    private PrintWriter input;
    private BufferedReader responseReader;
    private BufferedReader errorReader;
    private final StringBuffer response = new StringBuffer();
    private final StringBuffer error = new StringBuffer();
    private final boolean isReadError;
    private Thread responseWatcher;
    private Thread errorWatcher;

    private final BFLoggerInstance bfLogger = BFLogger.getLog();

    static {
        // Read Environment variables either from environments.csv or any other input data.
        setEnvironmentInstance();
    }

    private final List<String> baseCommand = new ArrayList<>();

    public BasePageCli(String... defaultCommand) {
        this(false, defaultCommand);
    }

    public BasePageCli(Boolean isReadError, String... defaultCommand) {
        this.isReadError = isReadError;
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
            if (Objects.isNull(process) || !process.isAlive()) {
                createProcess(command);
                openStreams();
                startOutputWatchers();
            }
        } catch (IOException e) {
            throw new BFCliException(e);
        }
    }

    private void createProcess(List<String> command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.environment()
                .putAll(getEnvParams());
        process = pb.start();
        BFLogger.logDebug("Process: " + process.toString());
    }

    protected Map<? extends String, ? extends String> getEnvParams() {
        return Collections.emptyMap();
    }

    private void openStreams() {
        responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
        input = new PrintWriter(process.getOutputStream());
    }

    private void startOutputWatchers() {
        responseWatcher = new Thread(new StreamReadingRunnable(responseReader, response));
        responseWatcher.start();
        if (isReadError) {
            errorWatcher = new Thread(new StreamReadingRunnable(errorReader, error));
            errorWatcher.start();
        }
    }

    @Override
    public void onTestClassFinish() {
        super.onTestClassFinish();
        closeStreams();
        closeProcess();
        waitForWatchersToFinish();
    }

    private void waitForWatchersToFinish() {
        try {
            if (Objects.nonNull(responseWatcher)) {
                responseWatcher.join();
            }

            if (Objects.nonNull(errorWatcher)) {
                errorWatcher.join();
            }
        } catch (InterruptedException e) {
            throw new BFCliException(e);
        }
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
        if (isReadError) {
            return error.length() > 0;
        } else {
            return hasStreamAvailableBytes(errorReader);
        }
    }

    public boolean isAlive() {
        return (Objects.nonNull(process) && process.isAlive()) || (Objects.nonNull(responseWatcher) && responseWatcher.isAlive()) || (Objects.nonNull(errorWatcher) && errorWatcher.isAlive());
    }

    public void waitToFinish() {
        try {
            process.waitFor();
            waitForWatchersToFinish();
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
        if (isReadError) {
            return error.toString();
        } else {
            return getLinesFromReader(errorReader);
        }
    }

    private String getLinesFromReader(BufferedReader reader) {
        try {
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1024];
            while (hasStreamAvailableBytes(reader)) {
                int read = reader.read(buffer);
                sb.append(buffer, 0, read);
                synchronized (this) {
                    bfLogger.logDebug(new String(Arrays.copyOf(buffer, read)));
                }
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

    private static void setEnvironmentInstance() {
        /*
         * Environment variables either from environments.csv or any other input data. For now there is no properties
         * settings file for Selenium module. In future, please have a look on Core Module IEnvironmentService
         * environmetInstance = Guice.createInjector(new EnvironmentModule()) .getInstance(IEnvironmentService.class);
         */
    }

    private class StreamReadingRunnable implements Runnable {

        private final BufferedReader reader;
        private final StringBuffer output;

        private StreamReadingRunnable(BufferedReader reader, StringBuffer output) {
            this.reader = reader;
            this.output = output;
        }

        @Override
        public void run() {
            try {
                while (Objects.nonNull(process) && (process.isAlive() || reader.ready())) {
                    output.append(getLinesFromReader(reader));
                    Thread.sleep(100);
                }
            } catch (InterruptedException | IOException e) {
                throw new BFCliException();
            }
        }
    }
}