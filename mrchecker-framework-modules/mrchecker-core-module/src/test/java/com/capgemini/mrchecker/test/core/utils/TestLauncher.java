package com.capgemini.mrchecker.test.core.utils;

import com.capgemini.mrchecker.test.core.BaseTest;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.util.Collections;
import java.util.List;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public final class TestLauncher {

    private TestLauncher() {
    }

    public static TestExecutionSummary launch(Class<? extends BaseTest> testClass) {
        return launch(Collections.singletonList(testClass));
    }

    public static TestExecutionSummary launch(String className) throws ClassNotFoundException {

        return launch((Class<? extends BaseTest>) Class.forName(className));
    }

    public static TestExecutionSummary launch(List<Class<? extends BaseTest>> testClasses) {
        SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherDiscoveryRequestBuilder requestBuilder = LauncherDiscoveryRequestBuilder.request();
        testClasses
                .forEach(testClass -> requestBuilder.selectors(selectClass(testClass)));

        LauncherDiscoveryRequest request = requestBuilder.build();
        Launcher launcher = LauncherFactory.create();
        TestPlan testPlan = launcher.discover(request);
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        return listener.getSummary();
    }
}
