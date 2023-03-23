package com.capgemini.mrchecker.test.core.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

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

        verify(observerMock, times(1)).onTestSuccess();
        verify(observerMock, times(1)).onTestFinish();
    }
}