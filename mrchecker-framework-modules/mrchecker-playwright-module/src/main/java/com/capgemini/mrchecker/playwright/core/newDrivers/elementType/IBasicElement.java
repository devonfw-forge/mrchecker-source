package com.capgemini.mrchecker.playwright.core.newDrivers.elementType;

import com.microsoft.playwright.Locator;

public interface IBasicElement {

    String getElementTypeName();

    Locator load();
}