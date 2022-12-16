package com.capgemini.mrchecker.test.core.unit.logger;

import com.capgemini.mrchecker.test.core.logger.EmptyTestName;
import com.capgemini.mrchecker.test.core.logger.ITestName;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@UnitTest
public class EmptyTestNameTest {
    private static final ITestName SUT = new EmptyTestName();

    @Test
    public void shouldReturnJunitName() {
        assertThat(SUT.getJunitName(), is(equalTo("")));
    }

    @Test
    public void shouldReturnAllureName() {
        assertThat(SUT.getAllureName(), is(equalTo("")));
    }
}
