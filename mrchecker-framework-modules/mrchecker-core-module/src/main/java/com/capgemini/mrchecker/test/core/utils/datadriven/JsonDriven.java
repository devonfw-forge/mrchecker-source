package com.capgemini.mrchecker.test.core.utils.datadriven;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import com.capgemini.mrchecker.test.core.logger.BFLogger;

import gherkin.deps.com.google.gson.GsonBuilder;
import gherkin.deps.com.google.gson.JsonArray;
import gherkin.deps.com.google.gson.JsonParser;

/**
 * @author is an utility to handle JSON deserialization boilerplate for JParamRunner data driven cases. It has no state,
 *         therefore is not instantiable nor extendible.
 */
public final class JsonDriven {
	
	private JsonDriven() {
		// NOP
	}
	
	/**
	 * @param <T>
	 *            is inferred from class, has no meaning, just suppresses warnings
	 * @param filename
	 *            of json file
	 * @param clazz
	 *            of deserialized object, typically some form of container (array) for test configuration
	 * @return deserialized object array
	 */
	public static <T> T provide(String filename, Class<T> clazz) {
		return new GsonBuilder().create()
				.fromJson(fileToJson(filename), clazz);
	}
	
	/**
	 * @param fileName
	 *            fileName
	 * @return json from file or empty object when parsing file failed
	 */
	private static JsonArray fileToJson(String fileName) {
		try {
			return readerToJson(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			BFLogger.logError("Json file not found: " + fileName);
		}
		return new JsonArray();
	}
	
	/**
	 * @param Reader
	 *            reader
	 * @return json from reader
	 */
	private static JsonArray readerToJson(Reader reader) {
		return new JsonParser().parse(reader)
				.getAsJsonArray();
	}
}
