package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;
import com.capgemini.mrchecker.selenium.core.utils.ScrollUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;

public abstract class BasicElement implements IBasicElement {
    private final IElementType type;
    private final By selector;
    private WebElement webElement;

    public BasicElement(IElementType type, By selector) {
        this.type = type;
        this.selector = selector;
    }

    @Override
    public By getSelector() {
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

    private boolean checkWebElement() {
        if (Objects.isNull(webElement)) {
            return false;
        }
        try {
            // Calling any method forces a WebElement check
            webElement.isEnabled();
            return true;
        } catch (Throwable throwable) {
            return false;
        }
    }

    @Override
    public WebElement getWebElement() throws BFElementNotFoundException {
        if (!checkWebElement()) {
            List<WebElement> elements = BasePage.getDriver().findElementDynamics(getSelector());
            if (elements.isEmpty()) {
                throw new BFElementNotFoundException(getSelector());
            }
            webElement = elements.get(0);
        }
        return webElement;
    }

    public String getClassName() {
        return getWebElement().getAttribute("class");
    }

    public String getValue() {
        return getWebElement().getAttribute("value");
    }

    public String getText() {
        return getWebElement().getText();
    }

    public Boolean isDisplayed() {
        return getWebElement().isDisplayed();
    }

    public Boolean isEnabled() {
        return getWebElement().isEnabled();
    }

    public void scrollElementIntoView() {
        ScrollUtils.scrollElementIntoView(getWebElement());
    }
}