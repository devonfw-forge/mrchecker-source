package com.capgemini.mrchecker.jemmy.logging;

import com.capgemini.mrchecker.test.core.logger.BFLogger;

import java.io.OutputStream;
import java.io.PrintStream;

public class BFLoggerDebugPrintStream extends PrintStream {

	public BFLoggerDebugPrintStream() {
		super(new BFLoggerDebugOutputStream(), true);
	}

	private static class BFLoggerDebugOutputStream extends OutputStream {
		private final StringBuilder sb = new StringBuilder();

		@Override
		public void write(int b) {
			if ('\n' == b) {
				if (sb.indexOf("Error") > 0) {
					BFLogger.logError(sb.toString());
				} else {
					BFLogger.logDebug(sb.toString());
				}
				sb.delete(0, sb.length());
			} else {
				sb.append((char) b);
			}
		}

		@Override
		public void write(byte[] b, int off, int len) {
			if (b == null) {
				throw new NullPointerException();
			} else if ((off < 0) || (off > b.length) || (len < 0) ||
					((off + len) > b.length) || ((off + len) < 0)) {
				throw new IndexOutOfBoundsException();
			} else if (len == 0) {
				return;
			}

			for (int i = 0; i < len; i++) {
				sb.append((char) b[off + i]);
			}
			int newLineIndex = -1;
			while ((newLineIndex = sb.indexOf("\n")) > 0) {
				String line = sb.substring(0, newLineIndex);
				sb.delete(0, newLineIndex + 1);
				if (line.contains("Error")) {
					BFLogger.logError(line);
				} else {
					BFLogger.logDebug(line);
				}
			}
		}
	}
}