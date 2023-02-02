package com.capgemini.mrchecker.playwright.core.newDrivers.elementType;

public class Button extends BasicElement {

    public Button(String selector) {
        super(ElementType.BUTTON, selector);
    }

    public void click() {
        getLocator().click();
    }
}