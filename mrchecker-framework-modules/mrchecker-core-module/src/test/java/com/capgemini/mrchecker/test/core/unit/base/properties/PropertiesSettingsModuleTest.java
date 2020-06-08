package com.capgemini.mrchecker.test.core.unit.base.properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import org.junit.After;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import com.capgemini.mrchecker.test.core.base.properties.PropertiesCoreTest;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import com.google.inject.Guice;

@UnitTest
@ResourceLock(value = "SingleThread")
public class PropertiesSettingsModuleTest {
	public static final String[][]	TEST_PROPERTIES_ARRAY	= { { "core.isAnalyticsEnabled", "false" },
					{ "core.isEncryptionEnabled", "false" },
					{ "core.defaultEnvironmentName", "TEST_ENV" }
	};
	public static final String		TEST_PROPERTIES;
	
	static {
		final StringBuilder sb = new StringBuilder();
		{
			Arrays.stream(TEST_PROPERTIES_ARRAY)
					.forEach(row -> sb.append(row[0])
							.append("=")
							.append(row[1])
							.append('\n'));
			TEST_PROPERTIES = sb.toString();
		}
	}
	
	@BeforeAll
	public static void setUpClass() {
		PropertiesSettingsModule.delInstance();
	}
	
	@After
	public void tearDown() {
		PropertiesSettingsModule.delInstance();
	}
	
	@Test
	public void shouldInitWithDefaultInputSource() {
		assertThat(PropertiesSettingsModule.init(), is(notNullValue()));
	}
	
	@Test
	public void shouldInitWithCustomInputSource() {
		PropertiesSettingsModule propertiesSettingsModule = PropertiesSettingsModule.init(new ByteArrayInputStream(TEST_PROPERTIES.getBytes()));
		
		assertThat(propertiesSettingsModule, is(notNullValue()));
		
		PropertiesCoreTest propertiesCoreTest = Guice.createInjector(propertiesSettingsModule)
				.getInstance(PropertiesCoreTest.class);
		
		assertThat(propertiesCoreTest.isAnalyticsEnabled(), is(equalTo(Boolean.parseBoolean(TEST_PROPERTIES_ARRAY[0][1]))));
		assertThat(propertiesCoreTest.isEncryptionEnabled(), is(equalTo(Boolean.parseBoolean(TEST_PROPERTIES_ARRAY[1][1]))));
		assertThat(propertiesCoreTest.getDefaultEnvironmentName(), is(equalTo(TEST_PROPERTIES_ARRAY[2][1])));
		
	}
	
	@Test
	public void shouldInitSecondTimeAfterDelete() {
		PropertiesSettingsModule firstInstance = PropertiesSettingsModule.init();
		PropertiesSettingsModule.delInstance();
		PropertiesSettingsModule secondInstance = PropertiesSettingsModule.init();
		
		assertThat(firstInstance, is(not(equalTo(secondInstance))));
	}
	
	@Test
	@Disabled
	public void shouldInitMultiThread() {
		// TODO: implement multi thread check
	}
	
	// TODO: implement that
	@Disabled
	@Test
	public void shouldInitThrowExceptionWhenUnparsableInput() {
	}
	
	// TODO: implement that
	@Disabled
	@Test
	public void shouldCreateThrowExceptionWhenFileCouldNotBeRead() {
		
	}
	
}
