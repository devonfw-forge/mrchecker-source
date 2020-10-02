package com.capgemini.mrchecker.test.core.logger;

import org.junit.jupiter.api.extension.ExtensionContext;

public class JunitOrCucumberRunnerTestNameParser implements ITestNameParser {
	
	@Override
	public ITestName parseFromContext(ExtensionContext context) {
		return context.getRequiredTestClass()
				.getName()
				.contains("BaseHook") ? parseCucumber(context) : parseJUnit(context);
	}
	
	private static ITestName parseCucumber(ExtensionContext context) {
		return CucumberRunnerTestName.parseString(context.getDisplayName());
	}
	
	private static ITestName parseJUnit(ExtensionContext context) {
		String uniqueId = context.getUniqueId();
		// [engine:junit-jupiter]/[class:com.capgemini.mrchecker.selenium.mts.MyThaiStarTest]/[method:Test_orderMenu()]
		String uniqueIdParsed = uniqueId.substring(uniqueId.indexOf("/") + 1)
				.replaceAll("([\\[\\]]|\\(.*\\))", "");
		StringBuilder sb = new StringBuilder();
		String[] parts = uniqueIdParsed.split("/");
		
		for (int i = 0; i < 2; i++) {
			sb.append(parts[i].split(":")[1])
					.append(":");
			if (parts[i].contains("test-template")) {
				sb.append(context.getDisplayName())
						.append(":");
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		return JunitRunnerTestName.parseString(sb.toString());
	}
}
