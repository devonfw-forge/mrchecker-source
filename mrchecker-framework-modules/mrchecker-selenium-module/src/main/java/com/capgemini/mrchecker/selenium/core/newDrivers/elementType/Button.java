package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import com.capgemini.mrchecker.selenium.core.utils.ScrollUtils;
import org.openqa.selenium.By;

public class Button extends BasicElement {

    public Button(By cssSelector) {
        super(ElementType.BUTTON, cssSelector);
    }

    public void click() {
        ScrollUtils.scrollElementIntoView(getElement());
        getElement().click();
    }
}
