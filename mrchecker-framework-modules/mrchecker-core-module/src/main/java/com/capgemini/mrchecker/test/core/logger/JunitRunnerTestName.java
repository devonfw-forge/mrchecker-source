package com.capgemini.mrchecker.test.core.logger;

public class JunitRunnerTestName implements ITestName {
	
	private final String	junitName;
	private final String	allureName;
	
	private JunitRunnerTestName(String junitName, String allureName) {
		this.junitName = junitName;
		this.allureName = allureName;
	}
	
	@Override
	public String getJunitName() {
		return junitName;
	}
	
	@Override
	public String getAllureName() {
		return allureName;
	}
	
	public static JunitRunnerTestName parseString(String originalName) {
		return new JunitRunnerTestName(originalName, originalName.substring(originalName.indexOf(":") + 1));
	}
}
