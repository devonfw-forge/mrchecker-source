package com.capgemini.mrchecker.test.core.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public final class FileTestUtils {
	
	public static final String LOG_DIR = "./logs/";
	
	private FileTestUtils() {
	}
	
	public static String getLogFilePath() {
		return LOG_DIR + Thread.currentThread()
				.getName() + ".log";
	}
	
	public static String getEnvLogFilePath() {
		return LOG_DIR + Thread.currentThread()
				.getName() + "_env.log";
	}
	
	public static String getFirstLineInFile(String logFilePath) throws IOException {
		return Files.lines(Paths.get(logFilePath))
				.findFirst()
				.orElse(null);
	}
	
	public static String getLastLineInFile(String logFilePath) throws IOException {
		return Files.lines(Paths.get(logFilePath))
				.reduce((first, second) -> second)
				.orElse(null);
	}
	
	public static String getAllLinesInFile(String logFilePath) throws IOException {
		return Files.lines(Paths.get(logFilePath))
				.collect(Collectors.joining());
	}
}