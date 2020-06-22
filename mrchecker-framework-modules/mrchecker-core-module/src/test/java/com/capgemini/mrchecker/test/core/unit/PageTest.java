package com.capgemini.mrchecker.test.core.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.tags.UnitTest;

@UnitTest
@ResourceLock(value = "SingleThread")
public class PageTest {
	
	private static final Page SUT = new Page() {
		
		@Override
		public ModuleType getModuleType() {
			return ModuleType.CORE;
		}
	};
	
	@Test
	public void shouldCallOnTestSuccess() {
		SUT.onTestSuccess();
	}
	
	@Test
	public void shouldCallOnTestFailure() {
		SUT.onTestFailure();
	}
	
	@Test
	public void shouldCallOnTestFinish() {
		SUT.onTestFinish();
	}
	
	@Test
	public void shouldCallOnTestClassFinish() {
		SUT.onTestClassFinish();
	}
	
	@Test
	public void shouldRegister() {
		SUT.addToTestExecutionObserver();
	}
}
