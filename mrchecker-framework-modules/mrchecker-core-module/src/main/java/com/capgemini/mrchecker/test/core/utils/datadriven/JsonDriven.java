package com.capgemini.mrchecker.test.core.utils.datadriven;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * @author is an utility to handle JSON deserialization boilerplate for JParamRunner data driven cases. It has no state,
 * therefore is not instantiable nor extendible.
 */
public final class JsonDriven {

    private JsonDriven() {
        // NOP
    }

    /**
     * @param <T>      is inferred from class, has no meaning, just suppresses warnings
     * @param filename of json file
     * @param clazz    of deserialized object, typically some form of container (array) for test configuration
     * @return deserialized object array
     */
    public static <T> T provide(String filename, Class<T> clazz) {
        return new GsonBuilder().create()
                .fromJson(fileToJson(filename), clazz);
    }

    public static <T> T provide(Reader reader, Class<T> clazz) {
        return new GsonBuilder().create()
                .fromJson(readerToJson(reader), clazz);
    }

    /**
     * @param fileName fileName
     * @return json from file or empty object when parsing file failed
     */
    private static JsonArray fileToJson(String fileName) {
        try (Reader reader = new FileReader(fileName)) {
            return readerToJson(reader);
        } catch (FileNotFoundException e) {
            BFLogger.logError("Json file not found: " + fileName);
        } catch (IOException e) {
            BFLogger.logError("Json file could not be read: " + fileName);
        }
        return new JsonArray();
    }

    /**
     * @param reader reader
     * @return json from reader
     */
    private static JsonArray readerToJson(Reader reader) {
        return new JsonParser().parse(reader)
                .getAsJsonArray();
    }
}
