package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import org.openqa.selenium.By;

public class InputTextElement extends BasicElement implements IBasicElement {
    public InputTextElement(By selector) {
        super(ElementType.INPUT_TEXT, selector);
    }

    public void setInputText(String text) {
        getWebElement().sendKeys(text);
    }

    public void clearInputText() {
        getWebElement().clear();
    }

    public void submit() {
        getWebElement().submit();
    }

    public void click() {
        getWebElement().click();
    }
}