package com.capgemini.mrchecker.security.core;

/**
 * The instances of BasePageAutoRegistration class are added to the test execution observer automatically in
 * constructor.
 * Although this operation is unsafe, it's been added to support migration from MrChecker Junit4 to Junit5.
 */
@Deprecated
abstract public class BasePageAutoRegistration extends BasePage {
	public BasePageAutoRegistration() {
		addToTestExecutionObserver();
	}
}
