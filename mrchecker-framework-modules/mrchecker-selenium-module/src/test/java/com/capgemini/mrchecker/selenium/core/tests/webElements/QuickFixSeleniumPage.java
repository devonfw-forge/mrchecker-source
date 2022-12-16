package com.capgemini.mrchecker.selenium.core.tests.webElements;

import com.capgemini.mrchecker.selenium.core.BasePage;
import org.junit.jupiter.api.Disabled;

@Disabled
public class QuickFixSeleniumPage extends BasePage {
    // This page is created to fix Selenium Session Exception from tes.WebElements test cases.
    // When the test cases are refactored, this page should be deleted.

    @Override
    public boolean isLoaded() {
        getDriver().waitForPageLoaded();
        return true;
    }

    @Override
    public void load() {

    }

    @Override
    public String pageTitle() {
        return getActualPageTitle();
    }
}