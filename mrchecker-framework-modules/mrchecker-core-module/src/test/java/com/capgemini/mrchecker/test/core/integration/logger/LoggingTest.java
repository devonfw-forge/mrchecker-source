package com.capgemini.mrchecker.test.core.integration.logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.logger.BFLoggerInstance;
import com.capgemini.mrchecker.test.core.tags.IntegrationTest;
import com.capgemini.mrchecker.test.core.utils.FileUtils;

@IntegrationTest
public class LoggingTest {
	public static final String FEND = "END";
	
	@Test
	public void shouldGetLog() {
		assertThat(BFLogger.getLog(), is(not(nullValue())));
	}
	
	@Test
	public void shouldGetLogMultithreaded() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		Collection<Callable<BFLoggerInstance>> tasks = new ArrayList<>();
		tasks.add(new BFLoggerGetter());
		tasks.add(new BFLoggerGetter());
		
		List<Future<BFLoggerInstance>> loggerInstances = executorService.invokeAll(tasks);
		executorService.shutdown();
		executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
		
		assertThat(loggerInstances.get(0), is(not(equalTo(loggerInstances.get(1)))));
	}
	
	public static class BFLoggerGetter implements Callable<BFLoggerInstance> {
		
		@Override
		public BFLoggerInstance call() {
			return BFLogger.getLog();
		}
	}
	
	@Test
	public void shouldLogInfo() throws IOException {
		String message = getMessage("Info");
		
		BFLogger.logInfo(message);
		
		String lastLogLine = FileUtils.getLastLineInFile(FileUtils.getLogFilePath());
		assertThat(lastLogLine, containsString(message));
		assertThat(lastLogLine, containsString("logInfo"));
	}
	
	@Test
	public void shouldLogDebug() throws IOException {
		String message = getMessage("Debug");
		
		BFLogger.logDebug(message);
		
		String lastLogLine = FileUtils.getLastLineInFile(FileUtils.getLogFilePath());
		assertThat(lastLogLine, containsString(message));
		assertThat(lastLogLine, containsString("logDebug"));
	}
	
	@Test
	public void shouldLogAnalytics() throws IOException {
		String message = getMessage("Analytics");
		
		BFLogger.logAnalytics(message);
		
		String lastLogLine = FileUtils.getLastLineInFile(FileUtils.getLogFilePath());
		assertThat(lastLogLine, containsString(message));
		assertThat(lastLogLine, containsString("logAnalytics"));
	}
	
	@Test
	public void shouldLogFunctionBegin() throws IOException {
		String message = getMessage("Function begin");
		
		BFLogger.logFunctionBegin(message);
		
		String lastLogLine = FileUtils.getLastLineInFile(FileUtils.getLogFilePath());
		assertThat(lastLogLine, containsString(message));
		assertThat(lastLogLine, containsString("logDebug"));
		assertThat(lastLogLine, containsString("Function: "));
	}
	
	@Test
	public void shouldLogFunctionEnd() throws IOException {
		BFLogger.logFunctionBegin("Function begin");
		
		BFLogger.logFunctionEnd();
		
		String lastLogLine = FileUtils.getLastLineInFile(FileUtils.getLogFilePath());
		assertThat(lastLogLine, containsString("END"));
		assertThat(lastLogLine, containsString("logDebug"));
	}
	
	@Test
	public void shouldLogError() throws IOException {
		String message = getMessage("Error");
		
		BFLogger.logError(message);
		
		String lastLogLine = FileUtils.getLastLineInFile(FileUtils.getLogFilePath());
		assertThat(lastLogLine, containsString(message));
		assertThat(lastLogLine, containsString("logError"));
	}
	
	@Test
	public void shouldLogErrorWithEnd() throws IOException {
		BFLogger.logFunctionBegin("Function begin");
		
		BFLogger.logError(FEND);
		
		String lastLogLine = FileUtils.getLastLineInFile(FileUtils.getLogFilePath());
		assertThat(lastLogLine, containsString(FEND));
		assertThat(lastLogLine, containsString("logError"));
	}
	
	@Test
	public void shouldLogEnv() throws IOException {
		String message = getMessage("Env");
		
		BFLogger.logEnv(message);
		
		String lastLogLine = FileUtils.getLastLineInFile(FileUtils.getEnvLogFilePath());
		assertThat(lastLogLine, containsString(message));
		assertThat(lastLogLine, containsString("logEnv"));
		
		lastLogLine = FileUtils.getLastLineInFile(FileUtils.getLogFilePath());
		assertThat(lastLogLine, containsString(message));
		assertThat(lastLogLine, containsString("logEnv"));
	}
	
	@Test
	public void shouldLogEnvWithEnd() throws IOException {
		BFLogger.logFunctionBegin("Function begin");
		
		BFLogger.logEnv(FEND);
		
		String lastLogLine = FileUtils.getLastLineInFile(FileUtils.getEnvLogFilePath());
		assertThat(lastLogLine, containsString(FEND));
		assertThat(lastLogLine, containsString("logEnv"));
		
		lastLogLine = FileUtils.getLastLineInFile(FileUtils.getLogFilePath());
		assertThat(lastLogLine, containsString(FEND));
		assertThat(lastLogLine, containsString("logEnv"));
	}
	
	@Test
	public void shouldLogTime() throws IOException {
		long startTime = System.currentTimeMillis();
		String message = getMessage("Method name");
		
		BFLogger.logTime(startTime, message);
		
		String lastLogLine = FileUtils.getLastLineInFile(FileUtils.getLogFilePath());
		assertThat(lastLogLine, containsString("Waiting for [" + message + "] took"));
		assertThat(lastLogLine, containsString("logDebug"));
	}
	
	@Test
	public void shouldLogTimeWithArgument() throws IOException {
		long startTime = System.currentTimeMillis();
		String message = getMessage("Method name");
		
		BFLogger.logTime(startTime, message, "Argument");
		
		String lastLogLine = FileUtils.getLastLineInFile(FileUtils.getLogFilePath());
		assertThat(lastLogLine, containsString("Waiting for [" + message + ": Argument] took"));
		assertThat(lastLogLine, containsString("logDebug"));
	}
	
	private static String getMessage(String message) {
		return System.currentTimeMillis() + "_" + message;
	}
	
	@Test
	public void shouldDumpLogFile() throws IOException {
		shouldLogDebug();
		
		String[] logDump = BFLogger.RestrictedMethods.dumpSeparateLog()
				.split(System.lineSeparator());
		
		assertThat(logDump[0], is(equalTo(FileUtils.getFirstLineInFile(FileUtils.getLogFilePath()))));
		assertThat(logDump[logDump.length - 1], is(equalTo(FileUtils.getLastLineInFile(FileUtils.getLogFilePath()))));
	}
	
	@Test
	public void shouldStartNewLogFile() throws IOException {
		shouldLogDebug();
		shouldLogDebug();
		
		BFLogger.RestrictedMethods.startSeparateLog();
		shouldLogDebug();
		
		assertThat(Files.lines(Paths.get(FileUtils.getLogFilePath()))
				.count(), is(equalTo(1L)));
	}
}