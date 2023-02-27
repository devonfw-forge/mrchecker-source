package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import org.openqa.selenium.By;

public class IFrame extends BasicElement implements IBasicElement {
    public IFrame(By selector) {
        super(ElementType.IFRAME, selector);
    }

    public void switchTo() {
        BFLogger.logInfo("Switching to iFrame");
        BasePage.getDriver()
                .switchTo()
                .frame(getWebElement());
    }

    public void switchToDefaultContent() {
        BFLogger.logInfo("Switching to default content");
        BasePage.getDriver()
                .switchTo()
                .defaultContent();
    }
}