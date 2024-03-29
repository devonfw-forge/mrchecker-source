package com.capgemini.mrchecker.selenium.core.tests.webElements;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.enums.PageSubURLsEnum;
import com.capgemini.mrchecker.test.core.BaseTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Disabled
public class HorizontalSliderTest extends BaseTest {
    QuickFixSeleniumPage quickFixSeleniumPage = new QuickFixSeleniumPage();
    private static final By sliderParent = By.cssSelector("div#tabs-2");
    private static final By sliderSelector = By.cssSelector("div#slider-range-max");
    private static final By valueSelector = By.cssSelector("#amount1");

    private static final String HORIZONTAL_SLIDER_INITIAL_VALUE = "2";

    @Override
    public void setUp() {
        BasePage.getDriver()
                .get(PageSubURLsEnum.WWW_FONT_URL.subURL() + PageSubURLsEnum.SLIDER.subURL());
    }

    @Test
    public void test() {
        // check if element is displayed
        assertTrue(BasePage.getDriver()
                .elementHorizontalSlider(sliderParent)
                .isDisplayed());

        // check if element type equals HoizontalSlider
        assertEquals("Horizontal Slider", BasePage.getDriver()
                .elementHorizontalSlider(sliderParent, sliderSelector, valueSelector)
                .getElementTypeName());

        // check if element's initial value is equal to 2
        assertEquals(new BigDecimal(HORIZONTAL_SLIDER_INITIAL_VALUE), BasePage.getDriver()
                .elementHorizontalSlider(sliderParent, sliderSelector, valueSelector)
                .getCurrentSliderValue());
    }

}
