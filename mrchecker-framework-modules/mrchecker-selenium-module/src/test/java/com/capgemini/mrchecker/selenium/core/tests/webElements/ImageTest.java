package com.capgemini.mrchecker.selenium.core.tests.webElements;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.enums.PageSubURLsEnum;
import com.capgemini.mrchecker.test.core.BaseTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertTrue;

/**
 * Created by TTRZCINSKI on 19.10.2018.
 */
@Disabled
public class ImageTest extends BaseTest {
    QuickFixSeleniumPage quickFixSeleniumPage = new QuickFixSeleniumPage();
    private static By img1 = By.cssSelector("img");

    @Override
    public void setUp() {
        BasePage.getDriver()
                .get(PageSubURLsEnum.TOOLS_QA.subURL() + PageSubURLsEnum.AUTOMATION_PRACTICE_FORM.subURL());
    }

    @Test
    public void test() {
        // check if label is displayed
        assertTrue(BasePage.getDriver()
                .elementImage(ImageTest.img1)
                .isDisplayed());
    }
}
