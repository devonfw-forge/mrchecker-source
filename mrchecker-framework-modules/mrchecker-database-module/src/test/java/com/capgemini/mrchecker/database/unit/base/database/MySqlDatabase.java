package com.capgemini.mrchecker.database.unit.base.database;

import com.capgemini.mrchecker.database.core.BasePageDatabase;
import com.capgemini.mrchecker.database.tags.UnitTest;

@UnitTest
public class MySqlDatabase extends BasePageDatabase {

	@Override public String getDatabaseUnitName() {
		return "mySqlExample";
	}
}