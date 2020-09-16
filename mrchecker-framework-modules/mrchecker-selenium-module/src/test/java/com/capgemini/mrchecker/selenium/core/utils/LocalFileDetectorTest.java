package com.capgemini.mrchecker.selenium.core.utils;

import static com.capgemini.mrchecker.selenium.core.newDrivers.DriverManager.getDriver;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.base.environment.GetEnvironmentParam;
import com.capgemini.mrchecker.selenium.core.enums.PageSubURLsEnum;
import com.capgemini.mrchecker.selenium.core.tests.webElements.QuickFixSeleniumPage;
import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.utils.FileUtils;

public class LocalFileDetectorTest extends BaseTest {
	QuickFixSeleniumPage quickFixSeleniumPage = new QuickFixSeleniumPage();
	
	private String	testFileName	= "testFile";
	private String	testFilePath;
	
	@Override
	public void setUp() {
		try {
			testFilePath = FileUtils.getAbsoluteFilePathFromResources(testFileName);
		} catch (FileNotFoundException e) {
			BFLogger.logDebug("Test file " + testFileName + "not found in resources.");
		}
		BasePage.getDriver()
				.get(GetEnvironmentParam.WWW_FONT_URL.getValue() + PageSubURLsEnum.AUTOMATION_PRACTICE_FORM.subURL());
	}
	
	@Test
	// it is enough that no exception is thrown
	public void testCurrentDriverCanAccessLocalFile() {
		WebElement we = getDriver().findElementDynamic(By.id("firstName"));
		
		we.sendKeys(testFilePath);
		we.submit();
	}
}
