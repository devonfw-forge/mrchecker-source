package com.capgemini.mrchecker.selenium.core.tests.webElements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.enums.PageSubURLsEnum;
import com.capgemini.mrchecker.test.core.BaseTest;

/**
 * Created by LKURZAJ on 03.03.2017.
 */
@Disabled
public class LabelTest extends BaseTest {
	QuickFixSeleniumPage quickFixSeleniumPage = new QuickFixSeleniumPage();
	
	private static By text1Label = By.cssSelector("span.bcd");
	
	@Override
	public void setUp() {
		BasePage.getDriver()
				.get(PageSubURLsEnum.TOOLS_QA.subURL() + PageSubURLsEnum.AUTOMATION_PRACTICE_FORM.subURL());
	}
	
	@Test
	public void test() {
		// check if label is displayed
		assertTrue(BasePage.getDriver()
				.elementLabel(LabelTest.text1Label)
				.isDisplayed());
		
		// check if label has properly text
		assertEquals("Text1", BasePage.getDriver()
				.elementLabel(LabelTest.text1Label)
				.getText());
		
		// check if label has properly class field
		assertEquals("bcd", BasePage.getDriver()
				.elementLabel(LabelTest.text1Label)
				.getClassName());
	}
}
