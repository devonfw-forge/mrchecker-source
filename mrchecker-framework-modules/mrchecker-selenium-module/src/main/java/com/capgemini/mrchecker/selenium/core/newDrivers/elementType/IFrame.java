package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import org.openqa.selenium.By;

public class IFrame extends BasicElement implements IBasicElement {

    public IFrame(By cssSelector) {
        super(ElementType.IFRAME, cssSelector);

        BasePage.getDriver()
                .switchTo()
                .frame(getElement());
        BFLogger.logInfo("Switching to iFrame");
    }

    public void switchToDefaultContent() {
        BasePage.getDriver()
                .switchTo()
                .defaultContent();
    }
}
