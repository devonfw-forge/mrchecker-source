package com.capgemini.mrchecker.test.core.unit;

import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.TestExecutionObserver;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import com.capgemini.mrchecker.test.core.utils.FileTestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@UnitTest
@ResourceLock(value = "SingleThread")
public class PageTest {

    private static final Page SUT = new TestPage();
    private static final String logFilePath = FileTestUtils.getLogFilePath();

    @AfterAll
    public static void cleanUpSuite() {
        TestExecutionObserver.getInstance()
                .afterAll(null);
    }

    @Test
    public void shouldCallOnTestSuccess() throws IOException {
        SUT.onTestSuccess();

        assertThat(FileTestUtils.getLastLineInFile(logFilePath), containsString("Page.onTestSuccess    " + SUT.getClass()
                .getSimpleName()));
    }

    @Test
    public void shouldCallOnTestFailure() throws IOException {
        SUT.onTestFailed();

        assertThat(FileTestUtils.getLastLineInFile(logFilePath), containsString("Page.onTestFailed    " + SUT.getClass()
                .getSimpleName()));
    }

    @Test
    public void shouldCallOnTestFinish() throws IOException {
        SUT.onTestFinish();

        assertThat(FileTestUtils.getLastLineInFile(logFilePath), containsString("Removed observer: " + SUT.toString()));
    }

    @Test
    public void shouldCallOnTestClassFinish() throws IOException {
        SUT.onTestClassFinish();

        assertThat(FileTestUtils.getLastLineInFile(logFilePath), containsString("Page.onTestClassFinish    " + SUT.getClass()
                .getSimpleName()));
    }

    @Test
    public void shouldRegister() throws IOException {
        SUT.addToTestExecutionObserver();

        assertThat(FileTestUtils.getLastLineInFile(logFilePath), containsString("Added observer: " + SUT.toString()));
    }

    public static class TestPage extends Page {
        @Override
        public ModuleType getModuleType() {
            return ModuleType.CORE;
        }
    }

}