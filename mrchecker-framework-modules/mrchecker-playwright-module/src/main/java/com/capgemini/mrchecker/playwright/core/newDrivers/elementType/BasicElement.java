package com.capgemini.mrchecker.playwright.core.newDrivers.elementType;

import com.capgemini.mrchecker.playwright.core.BasePage;
import com.microsoft.playwright.Locator;

public abstract class BasicElement implements IBasicElement {

    private final ElementType type;
    private String selector;

    public BasicElement(ElementType type, String selector) {
        this.type = type;
        this.setSelector(selector);
        load();
    }

    @Override
    public Locator load() {
        return getLocator();
    }

    @Override
    public String getElementTypeName() {
        return type.toString();
    }

    public Locator getLocator() {
        return BasePage.getDriver().currentPage().locator(getSelector());
    }

    public String getClassName() {
        return getLocator().getAttribute("class");
    }

    public String getValue() {
        return getLocator().getAttribute("value");
    }

    public String getText() {
        return getLocator().textContent();
    }

    public Boolean isVisible() {
        return getLocator().isVisible();
    }

    public Boolean isEnabled() {
        return getLocator().isEnabled();
    }

    private String getSelector() {
        return selector;
    }

    private void setSelector(String selector) {
        this.selector = selector;
    }
}