package com.capgemini.mrchecker.selenium.core.tests.webElements;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.enums.PageSubURLsEnum;
import com.capgemini.mrchecker.selenium.core.newDrivers.elementType.DropdownListElement;
import com.capgemini.mrchecker.test.core.BaseTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by LKURZAJ on 06.03.2017.
 */
@Disabled
public class DropdownListTest extends BaseTest {
    QuickFixSeleniumPage quickFixSeleniumPage = new QuickFixSeleniumPage();

    private static final By dropdownSelector = By.cssSelector("select#dropdown_7");
    private DropdownListElement dropdownObject;

    @Override
    public void setUp() {
        BasePage.getDriver()
                .get(PageSubURLsEnum.WWW_FONT_URL.subURL() + PageSubURLsEnum.REGISTRATION.subURL());
        dropdownObject = BasePage.getDriver()
                .elementDropdownList(DropdownListTest.dropdownSelector);
    }

    @AfterAll
    public static void tearDownAll() {
    }

    @Test
    public void testPossibleOptionsNumber() {
        assertTrue(dropdownObject.isDisplayed());
        assertEquals(dropdownObject.getAmountOfPossibleValues(), 204);
    }

    @Test
    public void testSelectOptionByIndex() {
        dropdownObject.selectDropdownByIndex(0);
        assertTrue(dropdownObject.isDropdownElementSelectedByIndex(0));
    }

    @Test
    public void testSelectOptionByValue() {
        dropdownObject.selectDropdownByValue("Vietnam");
        assertTrue(dropdownObject.isDropdownElementSelectedByValue("Vietnam"));
    }

    @Test
    public void testSelectOptionByText() {
        dropdownObject.selectDropdownByVisibleText("Vietnam");
        assertEquals(dropdownObject.getFirstSelectedOptionText(), "Vietnam");
    }

    @Test
    public void testAllSelectedOptions() {
        dropdownObject.selectDropdownByIndex(5);
        assertEquals(dropdownObject.getAllSelectedOptionsText()
                .size(), 1);
    }
}
