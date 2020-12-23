package com.capgemini.mrchecker.cli.core;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.utils.PageFactory;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class BasePageCliJavaTest extends BaseTest {

    @Test
    public void testJava() {
        JavaPage SUT = PageFactory.getPageInstance(JavaPage.class);
        SUT.runVersion();
        SUT.waitToFinish();
        assertThat(SUT.isAlive(), is(equalTo(false)));

        BFLogger.logDebug(SUT.readError());
    }


    public static class JavaPage extends BasePageCli {
        public JavaPage() {
            super("java.exe");
        }

        public void runVersion() {
            executeCommand("-version");
        }
    }
}