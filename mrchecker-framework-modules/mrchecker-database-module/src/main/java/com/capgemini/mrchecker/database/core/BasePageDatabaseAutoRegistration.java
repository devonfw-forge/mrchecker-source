package com.capgemini.mrchecker.database.core;

/**
 * The instances of BasePageDatabaseAutoRegistration class are added to the test execution observer automatically in
 * constructor.
 * Although this operation is unsafe, it's been added to support migration from MrChecker Junit4 to Junit5.
 */
@Deprecated
abstract public class BasePageDatabaseAutoRegistration extends BasePageDatabase {
	public BasePageDatabaseAutoRegistration() {
		addToTestExecutionObserver();
	}
}