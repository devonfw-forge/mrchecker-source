package com.capgemini.mrchecker.selenium.testsuits;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages("com.capgemini.mrchecker.selenium.unit")
public class UnitTestsSuite {
}
