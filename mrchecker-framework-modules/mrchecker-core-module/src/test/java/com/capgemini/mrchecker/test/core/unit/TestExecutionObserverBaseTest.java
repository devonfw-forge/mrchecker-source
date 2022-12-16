package com.capgemini.mrchecker.test.core.unit;

import com.capgemini.mrchecker.test.core.ITestObserver;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.TestExecutionObserver;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
@ResourceLock(value = "TestExecutionObserver")
public class TestExecutionObserverBaseTest {
    protected static final TestExecutionObserver SUT = TestExecutionObserver.getInstance();
    protected static final ExtensionContext contextMock;
    protected static final ITestObserver observerMock;

    static {
        Method mockMethod;
        try {
            mockMethod = Object.class.getMethod("toString");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        contextMock = mock(ExtensionContext.class);
        when(contextMock.getTestClass()).thenReturn(Optional.of(TestExecutionObserverBaseTest.class));
        when(contextMock.getRequiredTestClass()).thenCallRealMethod();
        when(contextMock.getRequiredTestMethod()).thenReturn(mockMethod);
        when(contextMock.getDisplayName()).thenReturn("Test_name");
        when(contextMock.getUniqueId()).thenReturn("[engine:junit-jupiter]/[class:com.capgemini.mrchecker.selenium.mts.MyThaiStarTest]/[method:Test_orderMenu()]");
        try {
            when(contextMock.getRequiredTestMethod()).thenReturn(Object.class.getMethod("toString"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        observerMock = mock(ITestObserver.class);
        when(observerMock.getModuleType()).thenReturn(ModuleType.CORE);
    }
}
