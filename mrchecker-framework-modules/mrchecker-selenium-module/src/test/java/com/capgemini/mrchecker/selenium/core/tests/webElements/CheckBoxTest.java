package com.capgemini.mrchecker.selenium.core.tests.webElements;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.enums.PageSubURLsEnum;
import com.capgemini.mrchecker.selenium.core.newDrivers.elementType.CheckBox;
import com.capgemini.mrchecker.test.core.BaseTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.*;

/**
 * Created by LKURZAJ on 06.03.2017.
 */
@Disabled
public class CheckBoxTest extends BaseTest {
    private QuickFixSeleniumPage quickFixSeleniumPage = new QuickFixSeleniumPage();
    private static final By hobbyCheckBoxSelector = By
            .cssSelector("li.fields.pageFields_1:nth-child(3) div.radio_wrap");
    CheckBox checkBoxElement;

    @Override
    public void setUp() {
        BasePage.getDriver()
                .get(PageSubURLsEnum.WWW_FONT_URL.subURL() + PageSubURLsEnum.REGISTRATION.subURL());
        checkBoxElement = BasePage.getDriver()
                .elementCheckbox(CheckBoxTest.hobbyCheckBoxSelector);
    }

    @Test
    public void testCheckBoxByIndex() {
        checkBoxElement.setCheckBoxByIndex(0);
        assertTrue(checkBoxElement.isCheckBoxSetByIndex(0));
        checkBoxElement.unsetCheckBoxByIndex(0);
        assertFalse(checkBoxElement.isCheckBoxSetByIndex(0));
    }

    @Test
    public void testCheckBoxByValue() {
        checkBoxElement.setCheckBoxByValue("reading");
        assertTrue(checkBoxElement.isCheckBoxSetByValue("reading"));
        checkBoxElement.unsetCheckBoxByValue("reading");
        assertFalse(checkBoxElement.isCheckBoxSetByValue("reading"));
    }

    @Test
    public void testCheckBoxByText() {
        checkBoxElement.setCheckBoxByText("Cricket");
        assertTrue(checkBoxElement.isCheckBoxSetByText("Cricket"));
        checkBoxElement.unsetCheckBoxByText("Cricket");
        assertFalse(checkBoxElement.isCheckBoxSetByText("Cricket"));

    }

    @Test
    public void testNumberOfCheckBoxes() {
        assertEquals(checkBoxElement.getTextList()
                .size(), 3);
    }

    @Test
    public void testCheckBoxAllValues() {
        checkBoxElement.setAllCheckBoxes();
        assertTrue(checkBoxElement.isAllCheckboxesSet());
        checkBoxElement.unsetAllCheckBoxes();
        assertFalse(checkBoxElement.isAllCheckboxesSet());
    }

}
