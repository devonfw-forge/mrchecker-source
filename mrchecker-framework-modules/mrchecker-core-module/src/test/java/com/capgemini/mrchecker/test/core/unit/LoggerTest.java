package com.capgemini.mrchecker.test.core.unit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.logger.BFLogger;

@Disabled
public class LoggerTest extends BaseTest {
	
	@BeforeAll
	public static void beforeClass() {
		System.out.println("LoggerTest.beforeClass()");
		BFLogger.logDebug("LoggerTest.beforeClass()");
	}
	
	@Test
	public void testPassed() throws InterruptedException {
		BFLogger.logDebug("Debug-TestPassed");
		BFLogger.logError("Error-TestPassed");
		BFLogger.logInfo("Info-TestPassed");
	}
	
	@Test
	public void testFailed() throws Exception {
		BFLogger.logDebug("Debug-TestFailed");
		BFLogger.logError("Error-TestFailed");
		BFLogger.logInfo("Info-TestFailed");
	}
	
	@AfterAll
	public static void afterClass() {
		System.out.println("LoggerTest.afterClass()");
		BFLogger.logDebug("LoggerTest.afterClass()");
	}
	
	@Override
	public void setUp() {
		System.out.println("LoggerTest.setUp()");
		BFLogger.logDebug("LoggerTest.setUp()");
	}
	
	@Override
	public void tearDown() {
		System.out.println("LoggerTest.tearDown()");
		BFLogger.logDebug("LoggerTest.tearDown()");
	}
	
}
