package com.capgemini.mrchecker.test.core.testsuits;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages("com.capgemini.mrchecker.test.core.unit")
public class UnitTestsSuite {
}
