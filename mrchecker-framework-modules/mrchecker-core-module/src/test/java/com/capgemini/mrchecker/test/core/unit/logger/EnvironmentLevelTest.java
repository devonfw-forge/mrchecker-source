package com.capgemini.mrchecker.test.core.unit.logger;

import static com.capgemini.mrchecker.test.core.logger.EnvironmentLevel.ENVIRONMENT_INT;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.apache.log4j.Level;
import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.test.core.logger.EnvironmentLevel;
import com.capgemini.mrchecker.test.core.tags.UnitTest;

@UnitTest
public class EnvironmentLevelTest {
	
	public static final String	ENVIRONMENT				= "environment";
	public static final String	UNKNOWN_STRING_LEVEL	= "unknown_level";
	public static final int		UNKNOWN_VALUE_LEVEL		= ENVIRONMENT_INT + 1;
	
	@Test
	public void shouldToLevelStringArgReturnEnvironment() {
		assertThat(EnvironmentLevel.toLevel(ENVIRONMENT), is(equalTo(EnvironmentLevel.ENVIRONMENT)));
	}
	
	@Test
	public void shouldToLevelStringArgReturnDebugWhenUnknownLevel() {
		assertThat(EnvironmentLevel.toLevel(UNKNOWN_STRING_LEVEL), is(equalTo(Level.DEBUG)));
	}
	
	@Test
	public void shouldToLevelStringAndDefaultArgsReturnEnvironment() {
		assertThat(EnvironmentLevel.toLevel(ENVIRONMENT, Level.INFO), is(equalTo(EnvironmentLevel.ENVIRONMENT)));
	}
	
	@Test
	public void shouldToLevelIntAndDefaultArgsReturnGivenDefaultWhenUnknownNull() {
		assertThat(EnvironmentLevel.toLevel(null, Level.INFO), is(equalTo(Level.INFO)));
	}
	
	@Test
	public void shouldToLevelIntAndDefaultArgsReturnGivenDefaultWhenUnknownLevel() {
		assertThat(EnvironmentLevel.toLevel(UNKNOWN_STRING_LEVEL, Level.INFO), is(equalTo(Level.INFO)));
	}
	
	@Test
	public void shouldToLevelIntArgReturnEnvironment() {
		assertThat(EnvironmentLevel.toLevel(ENVIRONMENT_INT), is(equalTo(EnvironmentLevel.ENVIRONMENT)));
	}
	
	@Test
	public void shouldToLevelIntArgReturnDebugWhenUnknownValue() {
		assertThat(EnvironmentLevel.toLevel(UNKNOWN_VALUE_LEVEL), is(equalTo(EnvironmentLevel.DEBUG)));
	}
	
	@Test
	public void shouldToLevelIntAndDefaultArgsReturnEnvironment() {
		assertThat(EnvironmentLevel.toLevel(ENVIRONMENT_INT, Level.INFO), is(equalTo(EnvironmentLevel.ENVIRONMENT)));
	}
	
	@Test
	public void shouldToLevelIntAndDefaultArgsReturnGivenDefaultWhenUnknownValue() {
		assertThat(EnvironmentLevel.toLevel(UNKNOWN_VALUE_LEVEL, Level.INFO), is(equalTo(Level.INFO)));
	}
	
}
