package com.capgemini.mrchecker.selenium.core.tests.webElements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.enums.PageSubURLsEnum;
import com.capgemini.mrchecker.selenium.core.newDrivers.elementType.InputTextElement;
import com.capgemini.mrchecker.test.core.BaseTest;

/**
 * Created by LKURZAJ on 03.03.2017.
 */
@Disabled
public class InputTextTest extends BaseTest {
	QuickFixSeleniumPage	quickFixSeleniumPage	= new QuickFixSeleniumPage();
	private static By		firstNameInputText		= By.cssSelector("input[id='name_3_firstname']");
	
	@Override
	public void setUp() {
		BasePage.getDriver()
				.get(PageSubURLsEnum.WWW_FONT_URL.subURL() + PageSubURLsEnum.REGISTRATION.subURL());
	}
	
	@Test()
	public void testInputData() {
		InputTextElement inputElement = BasePage.getDriver()
				.elementInputText(InputTextTest.firstNameInputText);
		
		// verify if input text is displayed
		assertTrue(inputElement.isDisplayed());
		
		// clear current text and verify (what to call getValue() or getText() depends
		// on implementation)
		inputElement.clearInputText();
		assertEquals("", inputElement.getValue());
		
		// input some text into input text and verify value
		inputElement.setInputText("John");
		assertEquals("John", inputElement.getValue());
	}
}
