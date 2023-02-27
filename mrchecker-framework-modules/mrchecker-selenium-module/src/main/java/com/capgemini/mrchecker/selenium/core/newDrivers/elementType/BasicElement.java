package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;
import com.capgemini.mrchecker.selenium.core.utils.ScrollUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;

public abstract class BasicElement implements IBasicElement {
    private final ElementType type;
    private final By selector;
    private WebElement webElement;

    public BasicElement(ElementType type, By selector) {
        this.type = type;
        this.selector = selector;
    }

    @Override
    public final By getSelector() {
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

    private boolean checkWebElement() {
        if (Objects.isNull(webElement)) {
            return false;
        }
        try {
            // Calling any method forces a staleness check
            webElement.isEnabled();
            return true;
        } catch (StaleElementReferenceException expected) {
            return false;
        }
    }

    @Override
    public final WebElement getWebElement() throws BFElementNotFoundException {
        if (!checkWebElement()) {
            List<WebElement> elements = BasePage.getDriver().findElementDynamics(getSelector());
            if (elements.isEmpty()) {
                throw new BFElementNotFoundException(getSelector());
            }
            webElement = elements.get(0);
        }
        return webElement;
    }

    public final String getClassName() {
        return getWebElement().getAttribute("class");
    }

    public final String getValue() {
        return getWebElement().getAttribute("value");
    }

    public final String getText() {
        return getWebElement().getText();
    }

    public final Boolean isDisplayed() {
        return getWebElement().isDisplayed();
    }

    public final Boolean isEnabled() {
        return getWebElement().isEnabled();
    }

    public final void scrollElementIntoView() {
        ScrollUtils.scrollElementIntoView(getWebElement());
    }
}