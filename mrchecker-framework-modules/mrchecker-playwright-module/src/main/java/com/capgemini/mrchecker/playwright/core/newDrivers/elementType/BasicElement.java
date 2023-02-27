package com.capgemini.mrchecker.playwright.core.newDrivers.elementType;

import com.capgemini.mrchecker.playwright.core.BasePage;
import com.microsoft.playwright.Locator;

import java.util.Objects;

public abstract class BasicElement implements IBasicElement {
    private final ElementType type;
    private final String selector;
    private Locator locator;

    public BasicElement(ElementType type, String selector) {
        this.type = type;
        this.selector = selector;
    }

    @Override
    public final String getSelector() {
        return this.selector;
    }

    @Override
    public final ElementType getElementType() {
        return this.type;
    }

    @Override
    public final String getElementTypeName() {
        return getElementType().toString();
    }


    @Override
    public final Locator getLocator() {
        if (Objects.isNull(locator)) {
            locator = BasePage.getDriver().currentPage().locator(getSelector());
        }
        return locator;
    }

    public final String getClassName() {
        return getLocator().getAttribute("class");
    }

    public final String getValue() {
        return getLocator().getAttribute("value");
    }

    public final String getText() {
        return getLocator().textContent();
    }

    public final Boolean isVisible() {
        return getLocator().isVisible();
    }

    public final Boolean isEnabled() {
        return getLocator().isEnabled();
    }
}