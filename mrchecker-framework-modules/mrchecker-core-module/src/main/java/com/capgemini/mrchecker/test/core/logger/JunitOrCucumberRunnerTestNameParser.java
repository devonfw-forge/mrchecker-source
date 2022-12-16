package com.capgemini.mrchecker.test.core.logger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

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
        String uniqueIdParsed = uniqueId.substring(uniqueId.indexOf("/") + 1)
                .replaceAll("([\\[\\]]|\\(.*\\))", "");
        String[] parts = uniqueIdParsed.split("/");

        StringBuilder sb = new StringBuilder();
        sb.append(parts[0].split(":")[1])
                .append(":");

        Method testMethod = context.getRequiredTestMethod();
        boolean isDDT = parts[1].contains("test-template");
        String displayName = context.getDisplayName();
        displayName = displayName.substring(displayName.indexOf("]") + 1)
                .replaceAll("\\(.*\\)", "");

        if (testMethod.isAnnotationPresent(DisplayName.class)) {
            sb.append(testMethod.getAnnotation(DisplayName.class)
                    .value());
            if (isDDT) {
                sb.append(":")
                        .append(displayName);
            }
        } else {
            if (isDDT) {
                sb.append(parts[1].split(":")[1])
                        .append(":");
            }
            sb.append(displayName);
        }

        return JunitRunnerTestName.parseString(sb.toString());
    }
}
