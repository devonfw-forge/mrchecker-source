package com.capgemini.mrchecker.test.core;

import com.capgemini.mrchecker.test.core.logger.*;
import com.capgemini.mrchecker.test.core.utils.StepLogger;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.capgemini.mrchecker.test.core.utils.ExceptionUtils.sneakyThrow;

public class TestExecutionObserver implements ITestExecutionObserver {
    private TestExecutionObserver() {
    }

    private static class HOLDER {
        private static TestExecutionObserver INSTANCE = new TestExecutionObserver();
    }

    public static TestExecutionObserver getInstance() {
        return HOLDER.INSTANCE;
    }

    private final ThreadLocal<Long> stopwatch = ThreadLocal.withInitial(() -> 0L);
    private final ThreadLocal<List<ITestObserver>> testObservers = ThreadLocal.withInitial(ArrayList::new);
    private final ThreadLocal<ITestName> testNames = ThreadLocal.withInitial(EmptyTestName::new);
    private final ITestNameParser testNameParser = new JunitOrCucumberRunnerTestNameParser();
    private final ThreadLocal<ExtensionContext> extensionContext = new ThreadLocal<>();

    public static final boolean DONT_CONSUME_EXCEPTION_IN_AFTERALL = false;

    private void forEachObserver(Consumer<ITestObserver> action) {
        testObservers.get().forEach(action);
    }

    private void setExtensionContext(ExtensionContext context) {
        extensionContext.set(context);
        for (ITestObserver obs : testObservers.get()) {
            obs.setExtensionContext(context);
        }
    }

    //TestWatcher - after complete test execution (befores + test + afters)
    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        afterTest("DISABLED", ITestObserver::onTestDisabled);
        reason.ifPresent(s -> BFLogger.logInfo("Reason: " + s));
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        afterTest("PASSED", ITestObserver::onTestSuccess);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        afterTest("ABORTED", ITestObserver::onTestAborted);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        afterTest("FAILED", ITestObserver::onTestFailed);
    }

    private void afterTest(String testInfo, Consumer<ITestObserver> action) {
        logTestInfo(testInfo);
        forEachObserver(action);
        forEachObserver(ITestObserver::onTestFinish);
        StepLogger.saveTextAttachmentToLog("Log file", BFLogger.RestrictedMethods.dumpSeparateLog());
    }

    //BeforeAllCallback
    @Override
    public void beforeAll(ExtensionContext context) {
        String testName = context.getRequiredTestClass().getName();
        setExtensionContext(context);
        BFLogger.logInfo("\"" + testName + "\"" + " - CLASS STARTED.");
    }

    //BeforeTestExecutionCallback
    @Override
    public void beforeTestExecution(ExtensionContext context) {
        BFLogger.RestrictedMethods.startSeparateLog();
        testNames.set(testNameParser.parseFromContext(context));
        setExtensionContext(context);
        Allure.getLifecycle().updateTestCase(testResult -> testResult.setName(testNames.get().getAllureName()));
        BaseTest.getAnalytics().sendClassName();
        logTestInfo("STARTED");
        stopwatch.set(System.currentTimeMillis());
        try {
            validateTestClassAndCallHook(context, BaseTest::setUp);
        } catch (Throwable throwable) {
            handleExecutionException(context, throwable, "setup", ITestObserver::onSetupFailure);
        }
    }

    //AfterTestExecutionCallback
    @Override
    public void afterTestExecution(ExtensionContext inputContext) {
        ExtensionContext context = inputContext == null ? extensionContext.get() : inputContext;
        setExtensionContext(context);
        stopwatch.set(System.currentTimeMillis() - stopwatch.get()); // end timing
        logTestInfo("FINISHED");
        printTimeExecutionLog();
        try {
            validateTestClassAndCallHook(context, BaseTest::tearDown);
        } catch (Throwable throwable) {
            handleExecutionException(context, throwable, "teardown", ITestObserver::onTeardownFailure);
        }
    }

    private static void validateTestClassAndCallHook(ExtensionContext context, Consumer<BaseTest> hook) {
        Object testClass = context.getRequiredTestInstance();
        if (testClass instanceof BaseTest) {
            hook.accept((BaseTest) testClass);
        }
    }

    //AfterAllCallback
    @Override
    synchronized public void afterAll(ExtensionContext extensionContext) {
        BFLogger.logDebug(getClass().getName() + ".observers: " + testObservers.get());
        forEachObserver(ITestObserver::onTestClassFinish);
        testObservers.get().clear();
        BFLogger.logDebug("All observers cleared.");
    }

    //LifecycleMethodExecutionExceptionHandler
    private void handleExecutionException(ExtensionContext inputContext, Throwable throwable, String annotationName, Consumer<ITestObserver> action, boolean throwException) {
        forEachObserver(action);
        ExtensionContext context = inputContext == null ? extensionContext.get() : inputContext;
        setExtensionContext(context);
        if (context != null) {
            String className = context.getRequiredTestClass().getName();
            String testName = context.getDisplayName();
            BFLogger.logError("\"" + className + "#" + testName + "\"" + " - EXCEPTION in " + annotationName + ": " + throwable.getMessage());
        }
        if (throwException) {
            sneakyThrow(throwable);
        }
    }

    private void handleExecutionException(ExtensionContext context, Throwable throwable, String annotationName, Consumer<ITestObserver> action) {
        handleExecutionException(context, throwable, annotationName, action, true);
    }

    @Override
    public void handleBeforeAllMethodExecutionException(ExtensionContext context, Throwable throwable) {
        handleExecutionException(context, throwable, "@BeforeAll", ITestObserver::onBeforeAllException);
    }

    @Override
    public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) {
        handleExecutionException(context, throwable, "@BeforeEach", ITestObserver::onBeforeEachException);
    }

    @Override
    public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable) {
        handleExecutionException(context, throwable, "@AfterEach", ITestObserver::onAfterEachException);
    }

    @Override
    public void handleAfterAllMethodExecutionException(ExtensionContext context, Throwable throwable) {
        try {
            handleExecutionException(context, throwable, "@AfterAll", ITestObserver::onAfterAllException);
        } catch (Throwable e) {
            if (DONT_CONSUME_EXCEPTION_IN_AFTERALL)
                sneakyThrow(e);
        }
    }

    //TestExecutionExceptionHandler
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) {
        handleTestExecutionException(context, throwable, true);
    }

    public void handleTestExecutionException(ExtensionContext context, Throwable throwable, boolean throwException) {
        handleExecutionException(context, throwable, "@Test", ITestObserver::onTestExecutionException, throwException);
    }

    //ITestObservable
    @Override
    public void addObserver(ITestObserver observer) {
        boolean anyMatchMethodObservers = isObserverAlreadyAdded(testObservers.get(), observer);
        if (!anyMatchMethodObservers) {
            testObservers.get().add(observer);
            BFLogger.logDebug("Added observer: " + observer);
        }
    }

    private boolean isObserverAlreadyAdded(List<ITestObserver> observers, ITestObserver observer) {
        return observers.stream().anyMatch(x -> x.getModuleType().equals(observer.getModuleType()));
    }

    @Override
    public void removeObserver(ITestObserver observer) {
        boolean anyMatchMethodObservers = isObserverAlreadyAdded(testObservers.get(), observer);
        if (!anyMatchMethodObservers) {
            testObservers.get().remove(observer);
            BFLogger.logDebug("Removed observer: " + observer);
        }
    }

    private void logTestInfo(String testStatus) {
        BFLogger.logInfo("\"" + testNames.get()
                .getJunitName() + "\"" + " - " + testStatus + ".");
    }

    private void printTimeExecutionLog() {
        BFLogger.logInfo("\"" + testNames.get().getJunitName() + "\"" + getFormattedTestDuration());
    }

    private String getFormattedTestDuration() {
        return String.format(" - DURATION: %1.2f min", (float) stopwatch.get() / (60 * 1000));
    }
}