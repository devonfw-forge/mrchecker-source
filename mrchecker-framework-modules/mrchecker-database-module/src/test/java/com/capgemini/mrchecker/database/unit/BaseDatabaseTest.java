package com.capgemini.mrchecker.database.unit;

import com.capgemini.mrchecker.database.tags.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
public class BaseDatabaseTest {

	@Test
	public void test() {
		MyDatabase myDatabase = new MyDatabase();
		myDatabase.myMethod();
	}

	public static class MyDatabase extends BaseDatabaseTest {

		public String myMethod() {
			return "Welcome";
		}
	}

}
