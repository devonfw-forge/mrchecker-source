package com.capgemini.mrchecker.test.core.utils;

public class ExceptionUtils {
    private ExceptionUtils() {
    }

    //SneakyThrow
    public static <Ex extends Throwable, R> R sneakyThrow(Throwable t) throws Ex {
        throw (Ex) t;
    }
}