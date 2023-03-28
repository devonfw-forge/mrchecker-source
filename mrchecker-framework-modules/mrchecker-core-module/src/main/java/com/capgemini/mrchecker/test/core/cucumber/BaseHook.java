package com.capgemini.mrchecker.test.core.cucumber;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.TestExecutionObserver;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExecutableInvoker;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstances;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public abstract class BaseHook {
    private final CucumberExtensionContext context;

    public BaseHook(BaseTest baseTest) {
        // Initializes environment
        // Include any global actions before Scenario
        // If no global action needed. Please create separate Hook class with Cucumber Before
        // Include any global actions after Scenario.
        // If no global action needed. Please create separate Hook class with Cucumber After
        context = new CucumberExtensionContext(baseTest);
    }

    public BaseHook() {
        this(new BaseTest() {
        });
    }

    public void setup(Scenario scenario) {
        context.setDisplayName(scenario.getName());
        Allure.suite(getFeatureFileNameFromId(scenario.getId()));
        TestExecutionObserver.getInstance().beforeTestExecution(context);
    }

    private String getFeatureFileNameFromId(String id) {
        int slashIndex = id.lastIndexOf("/");
        if (slashIndex > -1) {
            id = id.substring(slashIndex + 1);
        }
        int dotIndex = id.indexOf(".");
        if (dotIndex > -1) {
            id = id.substring(0, dotIndex);
        }
        return id;
    }

    public void tearDown(Scenario scenario) {
        switch (scenario.getStatus()) {
            case SKIPPED:
                TestExecutionObserver.getInstance().testAborted(context, null);
                break;
            case PASSED:
                TestExecutionObserver.getInstance().testSuccessful(context);
                break;
            case FAILED:
                TestExecutionObserver.getInstance().testFailed(context, null);
                break;
            case UNUSED:
            case PENDING:
            case AMBIGUOUS:
            case UNDEFINED:
            default:
                TestExecutionObserver.getInstance().testDisabled(context, null);
                break;
        }
        TestExecutionObserver.getInstance().afterTestExecution(context);
    }

    public static class CucumberExtensionContext implements ExtensionContext {
        private String testName;
        private final BaseTest testInstance;

        private CucumberExtensionContext(BaseTest testInstance) {
            this.testInstance = testInstance;
        }

        @Override
        public Optional<ExtensionContext> getParent() {
            return Optional.empty();
        }

        @Override
        public ExtensionContext getRoot() {
            return null;
        }

        @Override
        public String getUniqueId() {
            return null;
        }

        @Override
        public String getDisplayName() {
            return testName;
        }

        public void setDisplayName(String testName) {
            this.testName = testName;
        }

        @Override
        public Set<String> getTags() {
            return null;
        }

        @Override
        public Optional<AnnotatedElement> getElement() {
            return Optional.empty();
        }

        @Override
        public Optional<Class<?>> getTestClass() {
            return Optional.of(testInstance.getClass());
        }

        @Override
        public Optional<TestInstance.Lifecycle> getTestInstanceLifecycle() {
            return Optional.empty();
        }

        @Override
        public Optional<Object> getTestInstance() {
            return Optional.of(testInstance);
        }

        @Override
        public Optional<TestInstances> getTestInstances() {
            return Optional.empty();
        }

        @Override
        public Optional<Method> getTestMethod() {
            return Optional.empty();
        }

        @Override
        public Optional<Throwable> getExecutionException() {
            return Optional.empty();
        }

        @Override
        public Optional<String> getConfigurationParameter(String s) {
            return Optional.empty();
        }

        @Override
        public void publishReportEntry(Map<String, String> map) {
        }

        @Override
        public Store getStore(Namespace namespace) {
            return null;
        }

        @Override
        public ExecutionMode getExecutionMode() {
            return null;
        }

        @Override
        public ExecutableInvoker getExecutableInvoker() {
            return null;
        }

        @Override
        public <T> Optional<T> getConfigurationParameter(String key, Function<String, T> transformer) {
            // TODO Auto-generated method stub
            return null;
        }
    }
}