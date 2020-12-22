package com.capgemini.mrchecker.cli.core;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.utils.PageFactory;
import org.junit.jupiter.api.Test;

public class BasePageCliTest extends BaseTest {

    CmdPage SUT = PageFactory.getPageInstance(CmdPage.class);

    @Test
    public void test() throws InterruptedException {
        SUT.runCmd();
        Thread.sleep(2000);
        BFLogger.logDebug(SUT.readResponse());

        SUT.dir();
        Thread.sleep(2000);
        BFLogger.logDebug(SUT.readResponse());

        SUT.exit();
        Thread.sleep(2000);
        BFLogger.logDebug(SUT.readResponse());

        BFLogger.logDebug("Alive?: " + SUT.isAlive());
    }


    public static class CmdPage extends BasePageCli {

        public CmdPage() {
            super("cmd");
        }

        public void runCmd() {
            start();
//            start("a=1", "b=2");
        }


        public void dir() {
            writeCommand("dir");
        }

        public void exit() {
            writeCommand("exit");
        }
    }
}
