package com.capgemini.mrchecker.test.core.unit;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestExecutionClassObserverRemoveTest extends TestExecutionObserverBaseTest {
	
	@BeforeAll
	public static void setUpClass() {
		SUT.addObserver(observerMock);
	}
	
	@BeforeEach
	public void setUp() {
		clearInvocations(observerMock);
	}
	
	@Test
	public void shouldRemoveObserver() {
		SUT.removeObserver(observerMock);
		
		SUT.testSuccessful(contextMock);
		
		verify(observerMock, times(0)).onTestSuccess();
		verify(observerMock, times(0)).onTestFinish();
	}
}
