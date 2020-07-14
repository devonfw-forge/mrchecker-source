package com.capgemini.mrchecker.test.core.unit.utils;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import com.capgemini.mrchecker.test.core.utils.JsonReader;

@UnitTest
public class JsonReaderTest {
	private static final String	KEY_1			= "KEY_1";
	private static final String	KEY_2			= "KEY_2";
	private static final String	VALUE_1			= "VALUE_1";
	private static final String	VALUE_2			= "VALUE_2";
	private static final String	NO_SUCH_FILE	= "NO_SUCH_FILE";
	
	private static JSONObject	jo;
	private static final File	tempFile	= new File(System.getProperty("user.dir") + "/json_test.txt");
	
	@BeforeAll
	public static void setUpClass() throws IOException {
		jo = new JSONObject();
		jo.put(KEY_1, VALUE_1);
		jo.put(KEY_2, VALUE_2);
		
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
	public void shouldReadInputStream() throws IOException {
		try (InputStream jsonStream = new ByteArrayInputStream(jo.toString()
				.getBytes())) {
			JSONObject joFromReader = JsonReader.readJson(jsonStream);
			verifyResult(joFromReader);
		}
	}
	
	@Test
	public void shouldReadFile() throws IOException {
		try (FileOutputStream out = new FileOutputStream(tempFile)) {
			out.write(jo.toString()
					.getBytes());
		}
		
		JSONObject joFromReader = JsonReader.readJson(tempFile);
		
		verifyResult(joFromReader);
	}
	
	private static void verifyResult(JSONObject joFromReader) {
		assertThat(joFromReader.getString(KEY_1), is(equalTo(jo.getString(KEY_1))));
		assertThat(joFromReader.getString(KEY_2), is(equalTo(jo.getString(KEY_2))));
	}
	
	@Test
	public void shouldReadInputStreamThrowException() throws IOException {
		InputStream mockStream = mock(InputStream.class);
		when(mockStream.available()).thenThrow(new IOException());
		
		assertThrows(BFInputDataException.class, () -> JsonReader.readJson(mockStream));
	}
	
	@Test
	public void shouldReadFileThrowBFInputDataExceptionWhenNoFile() {
		assertThrows(BFInputDataException.class, () -> JsonReader.readJson(new File(NO_SUCH_FILE)));
	}
}
