package com.capgemini.mrchecker.test.core.unit.base.properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.Before;
import org.junit.Test;

import com.capgemini.mrchecker.test.core.base.properties.PropertiesCoreTest;

public class PropertiesCoreTestTest {
	public static final String	DEFAULT_ENV			= "DEV";
	public static final boolean	DEFAULT_ANALYTICS	= true;
	public static final boolean	DEFAULT_ENCRYPTION	= false;
	
	private PropertiesCoreTest sut;
	
	@Before
	public void setUp() {
		sut = new PropertiesCoreTest();
	}
	
	@Test
	public void shouldInitiallyBeDefaultEnv() {
		assertThat(sut.getDefaultEnvironmentName(), is(equalTo(DEFAULT_ENV)));
	}
	
	@Test
	public void shouldInitiallyBeAnalyticsEnabled() {
		assertThat(sut.isAnalyticsEnabled(), is(equalTo(DEFAULT_ANALYTICS)));
	}
	
	@Test
	public void shouldInitiallyBeEncryptionDisabled() {
		assertThat(sut.isEncryptionEnabled(), is(equalTo(DEFAULT_ENCRYPTION)));
	}
}
