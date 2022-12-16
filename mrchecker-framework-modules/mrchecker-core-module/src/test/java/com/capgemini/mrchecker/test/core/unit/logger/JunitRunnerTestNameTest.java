package com.capgemini.mrchecker.test.core.unit.logger;

import com.capgemini.mrchecker.test.core.logger.ITestName;
import com.capgemini.mrchecker.test.core.logger.JunitRunnerTestName;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@UnitTest
public class JunitRunnerTestNameTest {
    private static final String TEST_CLASS = "com.TestClass";
    private static final String TEST_METHOD = "method()";

    private static final String TEST_NAME = TEST_CLASS + ":" + TEST_METHOD;

    private static final ITestName SUT = JunitRunnerTestName.parseString(TEST_NAME);

    @Test
    public void shouldReturnJunitName() {
        assertThat(SUT.getJunitName(), is(equalTo(TEST_NAME)));
    }

    @Test
    public void shouldReturnAllureName() {
        assertThat(SUT.getAllureName(), is(equalTo(TEST_METHOD)));
    }
}
