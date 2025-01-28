package com.capgemini.mrchecker.jemmy.logging;

import com.capgemini.mrchecker.test.core.logger.BFLogger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class BFLoggerDebugPrintStream extends PrintStream {

	public BFLoggerDebugPrintStream() {
		super(new BFLoggerDebugOutputStream(), true);
	}

	private static class BFLoggerDebugOutputStream extends OutputStream {
		private final StringBuffer sb = new StringBuffer();

		@Override public void write(int b) throws IOException {
			if ('\n' == b) {
				BFLogger.logDebug(sb.toString());
				sb.delete(0, sb.length());
			} else {
				sb.append((char) b);
			}
		}
	}

}
