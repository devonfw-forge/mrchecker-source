package com.capgemini.mrchecker.selenium.core.utils;

import static com.capgemini.mrchecker.selenium.core.newDrivers.DriverManager.getDriver;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.enums.PageSubURLsEnum;
import com.capgemini.mrchecker.selenium.core.tests.webElements.QuickFixSeleniumPage;
import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.logger.BFLogger;

public class LocalFileDetectorTest extends BaseTest {
	QuickFixSeleniumPage quickFixSeleniumPage = new QuickFixSeleniumPage();
	
	private String	testFileName	= "testFile";
	private String	testFilePath;
	
	@Override
	public void setUp() {
		try {
			testFilePath = FileHelper.getAbsoluteFilePathFromResources(testFileName);
		} catch (FileNotFoundException e) {
			BFLogger.logDebug("Test file " + testFileName + "not found in resources.");
		}
		BasePage.getDriver()
				.get(PageSubURLsEnum.TOOLS_QA.subURL() + PageSubURLsEnum.AUTOMATION_PRACTICE_FORM.subURL());
	}
	
	@Override
	public void tearDown() {
		
	}
	
	@Test
	// it is enough that no exception is thrown
	public void testCurrentDriverCanAccessLocalFile() throws FileNotFoundException, InterruptedException, URISyntaxException {
		WebElement we = getDriver().findElementDynamic(By.id("photo"));
		we.sendKeys(testFilePath);
		getDriver().elementButton(By.id("submit"))
				.click();
	}
	
}
