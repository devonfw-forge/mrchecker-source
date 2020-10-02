package com.capgemini.mrchecker.test.core.logger;

public class CucumberRunnerTestName implements ITestName {
	
	private final String junitOrAllureName;
	
	private CucumberRunnerTestName(String junitOrAllureName) {
		this.junitOrAllureName = junitOrAllureName;
	}
	
	@Override
	public String getJunitName() {
		return junitOrAllureName;
	}
	
	@Override
	public String getAllureName() {
		return junitOrAllureName;
	}
	
	public static CucumberRunnerTestName parseString(String originalName) {
		return new CucumberRunnerTestName(originalName);
	}
}
