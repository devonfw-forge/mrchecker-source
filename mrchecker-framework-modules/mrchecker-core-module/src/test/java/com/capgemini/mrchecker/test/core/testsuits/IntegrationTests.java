package com.capgemini.mrchecker.test.core.testsuits;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@IncludeTags({ "IntegrationTest" })
@SelectPackages("com.capgemini.mrchecker.test.core")
public class IntegrationTests {
}
