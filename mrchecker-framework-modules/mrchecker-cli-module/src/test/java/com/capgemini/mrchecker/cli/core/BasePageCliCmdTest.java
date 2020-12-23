package com.capgemini.mrchecker.cli.core;

import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.utils.PageFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class BasePageCliCmdTest extends BaseTest {

    @Test
    public void testCmdPage() {
        CmdPage SUT = PageFactory.getPageInstance(CmdPage.class);

        SUT.dir();
        SUT.waitToFinish();
        assertThat(SUT.isAlive(), is(equalTo(false)));

        BFLogger.logDebug(SUT.readResponse());
    }

    public static class CmdPage extends BasePageCli {

        public CmdPage() {
            super("cmd.exe", "/c");
        }

        public void dir() {
            executeCommand("dir");
        }
    }
}