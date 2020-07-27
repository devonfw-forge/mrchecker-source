package com.capgemini.mrchecker.selenium.core.tests.webElements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.enums.PageSubURLsEnum;
import com.capgemini.mrchecker.selenium.core.newDrivers.elementType.RadioButtonElement;
import com.capgemini.mrchecker.test.core.BaseTest;

/**
 * Created by LKURZAJ on 28.02.2017.
 */
@Disabled
public class RadioButtonTest extends BaseTest {
	QuickFixSeleniumPage				quickFixSeleniumPage	= new QuickFixSeleniumPage();
	private RadioButtonElement			maritalStatusRadioButton;
	private final static By				selectorMaritalStatus	= By.cssSelector("div[class='radio_wrap']");
	private final static int			radioElementsCount		= 3;
	private final static List<String>	possibleValues			= Arrays.asList("Single", "Married", "Divorced");
	
	@Override
	public void setUp() {
		BasePage.getDriver()
				.get(PageSubURLsEnum.WWW_FONT_URL.subURL() + PageSubURLsEnum.REGISTRATION.subURL());
		maritalStatusRadioButton = BasePage.getDriver()
				.elementRadioButton(selectorMaritalStatus);
	}
	
	@Test
	public void testRadioButtonElementsCount() {
		// check if appropriate number of radio button elements is displayed
		assertEquals(radioElementsCount, maritalStatusRadioButton.getItemsCount());
	}
	
	@Test
	public void testPossibleValues() {
		List<String> elementValues = this.maritalStatusRadioButton.getTextList();
		for (int i = 0; i < RadioButtonTest.possibleValues.size(); i++) {
			assertEquals(RadioButtonTest.possibleValues.get(i), elementValues.get(i));
		}
	}
	
	@Test
	public void testSelection() {
		// select and check by index
		maritalStatusRadioButton.selectItemByIndex(0);
		assertEquals(maritalStatusRadioButton.getSelectedItemIndex(), 0);
		assertTrue(maritalStatusRadioButton.isItemSelectedByIndex(0));
		
		// select and check by value
		maritalStatusRadioButton.selectItemByValue("married");
		assertEquals(maritalStatusRadioButton.getSelectedItemValue(), "married");
		assertTrue(maritalStatusRadioButton.isItemSelectedByValue("married"));
	}
	
	@Test
	public void testSelectionSpecifiedItem() {
		// example of usage Radio Button with other constructor's arguments
		maritalStatusRadioButton = BasePage.getDriver()
				.elementRadioButton(selectorMaritalStatus,
						By.cssSelector("input"), Arrays.asList("selected"));
		maritalStatusRadioButton.selectItemByIndex(2);
		assertEquals(maritalStatusRadioButton.getSelectedItemIndex(), 2);
		assertTrue(maritalStatusRadioButton.isItemSelectedByIndex(2));
	}
	
}
