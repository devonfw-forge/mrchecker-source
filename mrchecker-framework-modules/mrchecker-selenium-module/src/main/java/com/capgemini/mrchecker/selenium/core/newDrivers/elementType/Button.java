package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import org.openqa.selenium.By;

public class Button extends BasicElement {
    public Button(By selector) {
        super(ElementType.BUTTON, selector);
    }

    public void click() {
        getWebElement().click();
    }
}