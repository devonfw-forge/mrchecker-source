package com.capgemini.mrchecker.webapi.core;

/**
 * The instances of BasePageWebAPIAutoRegistration class are added to the test execution observer automatically in
 * constructor.
 * Although this operation is unsafe, it's been added to support migration from MrChecker Junit4 to Junit5.
 */
@Deprecated
public abstract class BaseEndpointAutoRegistration extends BaseEndpoint {
    public BaseEndpointAutoRegistration() {
        addToTestExecutionObserver();
    }
}