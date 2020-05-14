package com.capgemini.mrchecker.selenium.testsuits;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import com.googlecode.junittoolbox.ParallelSuite;
import com.googlecode.junittoolbox.SuiteClasses;

@RunWith(ParallelSuite.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuiteClasses({ "../**/*.class", "!../testsuits/*.class" })
public class AllTestsSuite {
}
