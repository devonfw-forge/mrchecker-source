package com.capgemini.mrchecker.database.unit;

import com.capgemini.mrchecker.database.tags.UnitTest;
import com.capgemini.mrchecker.test.core.BaseTest;
import org.junit.jupiter.api.Test;

@UnitTest
public class BaseDatabaseTest extends BaseTest {

	@Test
	public void test() {
		new MyDatabase();
	}

	public static class MyDatabase extends BaseDatabaseTest {

	}
}
