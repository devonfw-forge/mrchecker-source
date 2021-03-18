package com.capgemini.mrchecker.cli.core.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.cli.core.BasePageCli;
import com.capgemini.mrchecker.cli.core.tags.IntegrationTest;
import com.capgemini.mrchecker.test.core.TestExecutionObserver;
import com.capgemini.mrchecker.test.core.utils.PageFactory;

@IntegrationTest
public class BasePageCliTest {

    private static BaseShellPage sut;

    @Test
    public void shouldCreatePageInstance() {
        sut = getSut();

        assertThat(sut, is(notNullValue()));
    }

    @Test
    public void shouldCreatePageInstanceWithReadErrorParam() {
        sut = getSutWithErrorRead();

        assertThat(sut, is(notNullValue()));
    }

    @Test
    public void shouldExecuteCommand() {
        sut = getSut();

        sut.dir();

        assertThat(sut.isAlive(), is(equalTo(true)));
        sut.waitToFinish();
        assertThat(sut.isAlive(), is(equalTo(false)));
    }

    @Test
    public void shouldExecuteCommandOnceWhenCommandCalledTwice() {
        sut = getSut();

        sut.dir();
        sut.dir();

        assertThat(sut.isAlive(), is(equalTo(true)));
        sut.waitToFinish();
        assertThat(sut.isAlive(), is(equalTo(false)));
    }

	@Test
	public void shouldExecuteDefaultCommand() {
		sut = getSut();

		sut.defaultCommand();
        sut.dirWithInputStream();

		assertThat(sut.isAlive(), is(equalTo(true)));
		sut.exitWithInputStream();
		sut.waitToFinish();
		assertThat(sut.isAlive(), is(equalTo(false)));
	}

    @Test
    public void shouldExecuteCommandAndCloseImmediatelly() {
        sut = getSut();

        sut.dir();
        sut.onTestClassFinish();

        assertThat(sut.isAlive(), is(equalTo(false)));
    }

    @Test
    public void shouldHasResponseReturnTrue() {
        sut = getSut();

        sut.dir();

        sut.waitToFinish();
        assertThat(sut.hasResponse(), is(equalTo(true)));
    }

    @Test
    public void shouldHasResponseReturnFalse() {
        sut = getSut();

        assertThat(sut.hasResponse(), is(equalTo(false)));
    }

    @Test
    public void shouldReadResponse() {
        sut = getSut();

        sut.dir();

        sut.waitToFinish();
        assertThat(sut.readResponse().length(), is(greaterThan(0)));
    }

    @Test
    public void shouldHasErrorReturnTrue() {
        sut = getSut();

        sut.errorCommand();

        sut.waitToFinish();
        assertThat(sut.hasErrors(), is(equalTo(true)));
    }

    @Test
    public void shouldHasErrorReturnFalse() {
        sut = getSut();

        sut.dir();

        sut.waitToFinish();
        assertThat(sut.hasErrors(), is(equalTo(false)));
    }

    @Test
    public void shouldReadError() {
        sut = getSut();

        sut.errorCommand();

        sut.waitToFinish();
        assertThat(sut.readError().length(), is(greaterThan(0)));
    }

    @Test
    public void shouldHasErrorReturnTrueWhenRunWithErrorWatcher() {
        sut = getSutWithErrorRead();

        sut.errorCommand();

        sut.waitToFinish();
        assertThat(sut.hasErrors(), is(equalTo(true)));
    }

    @Test
    public void shouldHasErrorReturnFalseWhenRunWithErrorWatcher() {
        sut = getSutWithErrorRead();

        sut.dir();

        sut.waitToFinish();
        assertThat(sut.hasErrors(), is(equalTo(false)));
    }

    @Test
    public void shouldReadErrorWhenRunWithErrorWatcher() {
        sut = getSutWithErrorRead();

        sut.errorCommand();

        sut.waitToFinish();
        assertThat(sut.readError().length(), is(greaterThan(0)));
    }

    @Test
    public void shouldExecuteCommandWithInputStream() {
        sut = getSutWithErrorRead();

        sut.defaultCommand();
        sut.dirWithInputStream();
        sut.exitWithInputStream();
        sut.waitToFinish();

        assertThat(sut.hasResponse(), is(equalTo(true)));
    }


    public static BaseShellPage getSut() {
        if(System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
            return PageFactory.getPageInstance(CmdPage.class);
        } else {
            return PageFactory.getPageInstance(BashPage.class);
        }
    }

    public static BaseShellPage getSutWithErrorRead() {
        if(System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
            return PageFactory.getPageInstance(CmdPage.class, Boolean.TRUE);
        } else {
            return PageFactory.getPageInstance(BashPage.class, Boolean.TRUE);
        }
    }


    public static abstract class BaseShellPage extends BasePageCli {

        public BaseShellPage(String... defaultCommand) {
            super(defaultCommand);
        }

        public BaseShellPage(Boolean isReadError, String... defaultCommand) {
            super(isReadError, defaultCommand);
        }

        public abstract void dir();

        public abstract void defaultCommand();

        public abstract void errorCommand();

        public abstract void dirWithInputStream();

        public abstract void exitWithInputStream();

    }

    public static class CmdPage extends BaseShellPage {

        public CmdPage() {
            super("cmd.exe");
        }

        public CmdPage(Boolean isReadError) {
            super(isReadError, "cmd.exe");
        }

        public void dir() {
            executeCommand("/c", "dir");
        }

        public void errorCommand() {
            executeCommand("/c", "dirr");
        }

        @Override
        public void dirWithInputStream() {
            writeCommand("dir");
        }

        @Override
        public void exitWithInputStream() {
            writeCommand("exit");
        }

        public void defaultCommand() {
            executeCommand();
        }
    }

    public static class BashPage extends BaseShellPage {
        public BashPage() {
            super("bash");
        }

        public BashPage(Boolean isReadError) {
            super(isReadError, "bash");
        }

        public void dir() {
            executeCommand("ls","2>&1");
        }

        public void errorCommand() {
            executeCommand("lss");
        }

        @Override
        public void dirWithInputStream() {
            writeCommand("ls");
        }

        @Override
        public void exitWithInputStream() {
            writeCommand("exit");
        }

        public void defaultCommand() {
            executeCommand();
        }
    }


    @AfterEach
    public void cleanUp() throws InterruptedException {
        sut.onTestClassFinish();
        TestExecutionObserver.getInstance()
                .removeObserver(sut);
    }
}