package com.capgemini.mrchecker.test.core.testsuites;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@SuppressWarnings("deprecation")
@RunWith(JUnitPlatform.class)
@IncludeTags({"UnitTest"})
@SelectPackages("com.capgemini.mrchecker.test.core")
public class UnitTests {
}
