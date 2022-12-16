package com.capgemini.mrchecker.selenium.core.utils;

import com.capgemini.mrchecker.selenium.core.enums.ColorsEnum;
import org.openqa.selenium.WebElement;

public class ColorsUtils {

    private ColorsUtils() {
    }

    /**
     * @param element - WebElement
     * @param color   from ColorsEnum
     * @return True if element text is displayed in black, false otherwise
     */
    public static boolean isElementTextInGivenColor(WebElement element, ColorsEnum color) {
        String elementColor = element.getCssValue("color");
        return elementColor != null && elementColor.contains(color.toString());
    }

    /**
     * @param element   - WebElement
     * @param validator - IColorValidator
     * @return True if element has correct color coresponding to IColorValidator, false otherwise
     */
    public static boolean isColorCorrectForValue(WebElement element, IColorValidator<WebElement> validator) {
        return validator.isValid(element);
    }

}