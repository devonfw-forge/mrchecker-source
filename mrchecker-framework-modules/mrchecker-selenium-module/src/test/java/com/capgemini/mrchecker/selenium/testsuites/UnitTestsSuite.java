package com.capgemini.mrchecker.selenium.testsuites;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@IncludeTags({ "UnitTest" })
@SelectPackages("com.capgemini.mrchecker.selenium")
public class UnitTestsSuite {
}
