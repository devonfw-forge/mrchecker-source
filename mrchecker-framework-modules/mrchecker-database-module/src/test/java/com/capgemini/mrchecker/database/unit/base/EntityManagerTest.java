package com.capgemini.mrchecker.database.unit.base;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.database.tags.UnitTest;
import com.capgemini.mrchecker.database.unit.base.database.MySqlDatabase;
import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.utils.PageFactory;

@UnitTest
public class EntityManagerTest extends BaseTest {
	
	private MySqlDatabase mySqlDatabase;
	
	@BeforeEach
	public void init() {
		mySqlDatabase = PageFactory.getPageInstance(MySqlDatabase.class);
	}
	
	@Test
	public void testShouldValidateDatabaseUnitName() {
		// given
		String exampleDatabaseUnitName = "mySqlExample";
		
		// when
		// then
		assertTrue(mySqlDatabase.getDatabaseUnitName()
				.equals(exampleDatabaseUnitName));
	}
	
	@Test
	public void testShouldValidateEntityManagerCreation() {
		// given
		// when
		// then
		assertNotNull(mySqlDatabase.getEntityManager());
	}
	
}
