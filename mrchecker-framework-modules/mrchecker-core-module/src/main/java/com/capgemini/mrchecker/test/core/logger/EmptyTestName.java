package com.capgemini.mrchecker.test.core.logger;

public class EmptyTestName implements ITestName {
	
	public EmptyTestName() {
		
	}
	
	@Override
	public String getJunitName() {
		return "";
	}
	
	@Override
	public String getAllureName() {
		return "";
	}
}
