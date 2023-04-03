package com.capgemini.mrchecker.test.core.base.driver;

public enum DriverCloseLevel {
    CLASS,
    TEST;

    public static DriverCloseLevel fromText(String text) {
        for (DriverCloseLevel level : values()) {
            if (level.name().equalsIgnoreCase(text)) {
                return level;
            }
        }
        return CLASS;
    }
}