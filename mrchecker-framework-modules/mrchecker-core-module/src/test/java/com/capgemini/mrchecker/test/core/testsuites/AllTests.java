package com.capgemini.mrchecker.test.core.testsuites;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@IncludeTags({"UnitTest", "IntegrationTest"})
@SelectPackages("com.capgemini.mrchecker.test.core")
public class AllTests {
}
