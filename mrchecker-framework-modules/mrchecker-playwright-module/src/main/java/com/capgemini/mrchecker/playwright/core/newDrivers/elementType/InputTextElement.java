package com.capgemini.mrchecker.playwright.core.newDrivers.elementType;

public class InputTextElement extends BasicElement implements IBasicElement {

    public InputTextElement(String selector) {
        super(ElementType.INPUT_TEXT, selector);
    }

    public void fillInputText(String text) {
        getLocator().fill(text);
    }

    public void typeInputText(String text) {
        getLocator().type(text);
    }

    public void clearInputText() {
        getLocator().clear();
    }
}