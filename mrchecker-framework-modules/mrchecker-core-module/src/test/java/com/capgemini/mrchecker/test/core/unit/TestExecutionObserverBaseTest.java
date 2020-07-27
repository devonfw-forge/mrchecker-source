package com.capgemini.mrchecker.test.core.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.parallel.ResourceLock;

import com.capgemini.mrchecker.test.core.ITestObserver;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.TestExecutionObserver;
import com.capgemini.mrchecker.test.core.tags.UnitTest;

@UnitTest
@ResourceLock(value = "TestExecutionObserver")
public class TestExecutionObserverBaseTest {
	protected static final TestExecutionObserver	SUT	= TestExecutionObserver.getInstance();
	protected static final ExtensionContext			contextMock;
	protected static final ITestObserver			observerMock;
	
	static {
		contextMock = mock(ExtensionContext.class);
		when(contextMock.getTestClass()).thenReturn(Optional.of(TestExecutionObserverBaseTest.class));
		when(contextMock.getRequiredTestClass()).thenCallRealMethod();
		when(contextMock.getDisplayName()).thenReturn("Test_name");
		
		observerMock = mock(ITestObserver.class);
		when(observerMock.getModuleType()).thenReturn(ModuleType.CORE);
	}
}
