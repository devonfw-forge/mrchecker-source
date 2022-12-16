package com.capgemini.mrchecker.mobile.testsuites;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@IncludeTags({"UnitTest"})
@SelectPackages("com.capgemini.mrchecker.mobile")
public class UnitTestsSuite {
}
