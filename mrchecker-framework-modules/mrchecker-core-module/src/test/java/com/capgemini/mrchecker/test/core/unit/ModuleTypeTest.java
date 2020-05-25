package com.capgemini.mrchecker.test.core.unit;

import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.tags.UnitTest;

@UnitTest
public class ModuleTypeTest {
	
	@SuppressWarnings("unused")
	@Test
	public void shouldGetEnum() {
		ModuleType sut = ModuleType.DATABASE;
	}
}
