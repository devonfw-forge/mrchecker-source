package com.capgemini.mrchecker.test.core.unit.utils.datadriven;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.io.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import com.capgemini.mrchecker.test.core.utils.datadriven.JsonDriven;

import gherkin.deps.com.google.gson.GsonBuilder;

@UnitTest
public class JsonDrivenTest {
	
	private static final String	TEST_STRING		= "TEST_STRING";
	private static final int	TEST_INTEGER	= Integer.MAX_VALUE;
	private static final String	NO_SUCH_FILE	= "NO_SUCH_FILE";
	
	private static SimpleObject	so;
	private static String		jsonString;
	
	private static final File tempFile = new File(System.getProperty("user.dir") + "/json_test.txt");
	
	@BeforeAll
	public static void setUpClass() throws IOException {
		so = new SimpleObject(TEST_STRING, TEST_INTEGER);
		jsonString = new GsonBuilder().create()
				.toJson(new SimpleObject[] { so });
		if (tempFile.exists()) {
			if (tempFile.delete()) {
				BFLogger.logDebug(tempFile.getName() + " file deleted ");
			}
		}
		
		if (tempFile.createNewFile()) {
			BFLogger.logDebug(tempFile.getName() + " file created ");
		}
	}
	
	@AfterAll
	public static void tearDownClass() {
		if (tempFile.exists()) {
			if (tempFile.delete()) {
				BFLogger.logDebug(tempFile.getName() + " file deleted ");
			}
		}
	}
	
	@Test
	public void shouldProvideSimpleObjectArrayFromReader() throws IOException {
		try (Reader in = new StringReader(jsonString)) {
			verifyResult(JsonDriven.provide(in, SimpleObject[].class));
		}
	}
	
	@Test
	public void shouldProvideSimpleObjectArrayFromFile() throws IOException {
		try (FileOutputStream out = new FileOutputStream(tempFile)) {
			out.write(jsonString.getBytes());
		}
		
		verifyResult(JsonDriven.provide(tempFile.getName(), SimpleObject[].class));
	}
	
	private static void verifyResult(SimpleObject[] soaFromReader) {
		assertThat(soaFromReader[0].equals(so), is(equalTo(true)));
	}
	
	@Test
	public void shouldProvideEmptyArrayWhenNoFile() {
		assertThat(JsonDriven.provide(NO_SUCH_FILE, SimpleObject[].class).length, is(equalTo(0)));
	}
}

@SuppressWarnings("unused")
class SimpleObject {
	private String	string;
	private int		integer;
	
	public SimpleObject(String string, int integer) {
		this.string = string;
		this.integer = integer;
	}
	
	public String getString() {
		return string;
	}
	
	public void setString(String string) {
		this.string = string;
	}
	
	public int getInteger() {
		return integer;
	}
	
	public void setInteger(int integer) {
		this.integer = integer;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		SimpleObject rhs = (SimpleObject) obj;
		return new EqualsBuilder()
				.append(string, rhs.string)
				.append(integer, rhs.integer)
				.isEquals();
	}
}
