package com.capgemini.mrchecker.test.core.logger;

import org.junit.jupiter.api.extension.ExtensionContext;

public interface ITestNameParser {
	ITestName parseFromContext(ExtensionContext context);
}
