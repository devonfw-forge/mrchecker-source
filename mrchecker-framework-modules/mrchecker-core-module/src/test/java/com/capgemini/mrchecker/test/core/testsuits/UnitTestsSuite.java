package com.capgemini.mrchecker.test.core.testsuits;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import com.googlecode.junittoolbox.SuiteClasses;

// @RunWith(ParallelTestClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuiteClasses({ "../unit/*.class", "../unit/**/*.class", "!../testsuits/*.class" })
public class UnitTestsSuite {
	
}
