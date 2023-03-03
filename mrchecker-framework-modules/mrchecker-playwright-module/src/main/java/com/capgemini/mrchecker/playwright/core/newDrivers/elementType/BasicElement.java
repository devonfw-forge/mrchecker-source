package com.capgemini.mrchecker.playwright.core.newDrivers.elementType;

import com.capgemini.mrchecker.playwright.core.BasePage;
import com.microsoft.playwright.Locator;

import java.util.Objects;

public abstract class BasicElement implements IBasicElement {
    private final IElementType type;
    private final String selector;
    private Locator locator;

    public BasicElement(IElementType type, String selector) {
        this.type = type;
        this.selector = selector;
    }

    @Override
    public String getSelector() {
        return this.selector;
    }

    @Override
    public IElementType getElementType() {
        return this.type;
    }

    @Override
    public String getElementTypeName() {
        if (Objects.isNull(getElementType())) {
            return null;
        }
        return getElementType().getName();
    }

    @Override
    public Locator getLocator() {
        if (Objects.isNull(locator)) {
            locator = BasePage.getDriver().currentPage().locator(getSelector());
        }
        return locator;
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
}