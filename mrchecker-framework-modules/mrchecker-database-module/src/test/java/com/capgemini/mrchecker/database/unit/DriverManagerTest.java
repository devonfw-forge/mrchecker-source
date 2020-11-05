package com.capgemini.mrchecker.database.unit;

import static com.capgemini.mrchecker.database.mocks.PersistenceProviderMock.SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Objects;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.database.core.DriverManager;
import com.capgemini.mrchecker.database.tags.UnitTest;

@UnitTest
public class DriverManagerTest {
	public static EntityManager	test_em1;
	public static EntityManager	test_em2;
	
	@BeforeEach
	public void init() {
		DriverManager.closeDriver();
	}
	
	@AfterEach
	public void cleanup() {
		if (!Objects.isNull(test_em1)) {
			test_em1.close();
		}
		
		if (!Objects.isNull(test_em2)) {
			test_em2.close();
		}
	}
	
	@AfterAll
	public static void cleanupSuite() {
		DriverManager.closeDriver();
	}
	
	@Test
	public void shouldCreateEntityManager() {
		test_em1 = DriverManager.createEntityManager(SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK);
		
		assertThat(test_em1, is(notNullValue()));
	}
	
	@Test
	public void shouldCreateEntityManagerCalledTwiceReturnDifferentInstances() {
		test_em1 = DriverManager.createEntityManager(SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK);
		test_em2 = DriverManager.createEntityManager(SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK);
		
		assertThat(test_em1, is(notNullValue()));
		assertThat(test_em1, is(not(equalTo(test_em2))));
	}
	
	@Test
	public void shouldCloseDriver() {
		test_em1 = DriverManager.createEntityManager(SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK);
		assertThat(test_em1, is(notNullValue()));
		
		DriverManager.closeDriver();
		
		assertThrows(IllegalStateException.class, () -> test_em1.flush());
	}
	
	@Test
	public void shouldCloseDriverTwice() {
		test_em1 = DriverManager.createEntityManager(SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK);
		assertThat(test_em1, is(notNullValue()));
		
		DriverManager.closeDriver();
		DriverManager.closeDriver();
		
		assertThrows(IllegalStateException.class, () -> test_em1.flush());
	}
	
	@Test
	@Disabled
	// TODO: implement that
	public void shouldCreateEntityManagerMultithreaded() {
	}
}
