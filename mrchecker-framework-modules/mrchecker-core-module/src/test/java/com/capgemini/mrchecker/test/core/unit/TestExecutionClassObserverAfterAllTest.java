package com.capgemini.mrchecker.test.core.unit;

import com.capgemini.mrchecker.test.core.utils.FileTestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;

public class TestExecutionClassObserverAfterAllTest extends TestExecutionObserverBaseTest {

    @BeforeAll
    public static void setUpClass() {
        SUT.addObserver(observerMock);
    }

    @BeforeEach
    public void setUp() {
        clearInvocations(observerMock);
    }

    @Test
    public void shouldCallAfterAll() throws IOException {
        SUT.afterAll(contextMock);

        verify(observerMock, times(1)).onTestClassFinish();
        assertThat(FileTestUtils.getAllLinesInFile(FileTestUtils.getLogFilePath()), containsString("All observers cleared."));

        SUT.testSuccessful(contextMock);

        verify(observerMock, times(0)).onTestSuccess();
        verify(observerMock, times(0)).onTestFinish();
    }
}
