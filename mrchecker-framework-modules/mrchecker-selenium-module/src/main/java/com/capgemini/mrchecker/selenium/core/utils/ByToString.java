package com.capgemini.mrchecker.selenium.core.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.support.locators.RelativeLocator.RelativeBy;

public final class ByToString {

    public static String getReadableByName(By selector) {
        if (selector instanceof RelativeBy) {
            return getReadableRelativeByName((RelativeBy) selector);
        }
        return selector.toString();
    }

    private static String getReadableRelativeByName(RelativeBy relativeLocator) {
        return relativeLocator.getRemoteParameters()
                              .toString()
                              .replaceAll("\\[\\[\\[\\[New\\w+Driver.*?->[^]]+]", "WebElement");
    }
}
