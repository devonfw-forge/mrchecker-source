package com.capgemini.mrchecker.jemmy.logging;

import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.TestOut;

import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicBoolean;

public final class JemmyLogConfig {

	private static final AtomicBoolean configured = new AtomicBoolean(false);

	private JemmyLogConfig() {

	}

	public static synchronized void configure() {
		if (!configured.get()) {
			PrintStream ps = new BFLoggerDebugPrintStream();
			JemmyProperties.setCurrentOutput(new TestOut(System.in, ps, ps));
			configured.set(true);
		}
	}
}
