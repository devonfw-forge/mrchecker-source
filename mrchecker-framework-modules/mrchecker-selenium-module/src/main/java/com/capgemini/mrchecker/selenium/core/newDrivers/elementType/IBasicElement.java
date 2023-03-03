package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface IBasicElement {
    By getSelector();

    IElementType getElementType();

    String getElementTypeName();

    WebElement getWebElement();
}