package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface IBasicElement {
    By getSelector();

    ElementType getElementType();

    String getElementTypeName();

    WebElement getWebElement();
}