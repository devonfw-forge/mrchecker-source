package com.capgemini.mrchecker.test.core.utils;

import java.io.*;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;

public class JsonReader {
	
	private JsonReader() {
		// NOP
	}
	
	/**
	 * Reads JSON file and return it's
	 * 
	 * @param jsonFile
	 *            jsonFile
	 * @return JSONObject
	 */
	public static JSONObject readJson(File jsonFile) {
		try {
			return readJson(new FileInputStream(jsonFile));
		} catch (FileNotFoundException e) {
			throw new BFInputDataException("Unable to read JSON file: " + jsonFile.getName());
		}
	}
	
	/**
	 * Reads JSON file and return it's
	 *
	 * @param jsonInputStream
	 *            jsonInputStream
	 * @return JSONObject
	 */
	public static JSONObject readJson(InputStream jsonInputStream) {
		try (InputStream jsonStream = jsonInputStream) {
			return new JSONObject(IOUtils.toString(jsonStream));
		} catch (IOException e) {
			throw new BFInputDataException("Unable to read JSON from the stream: " + jsonInputStream.toString());
		}
	}
}
