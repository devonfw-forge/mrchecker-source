package com.capgemini.mrchecker.playwright.core.newDrivers.elementType;

public class TooltipElement extends BasicElement {
    public TooltipElement(String selector) {
        super(ElementType.TOOLTIP, selector);
    }

    public boolean isTextContains(String text) {
        return getText().contains(text);
    }
}