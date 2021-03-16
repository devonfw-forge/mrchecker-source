package com.capgemini.mrchecker.database.unit;

import static com.capgemini.mrchecker.database.mocks.PersistenceProviderMock.SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK;
import static com.capgemini.mrchecker.database.utils.IsCollectionDistinct.isCollectionDistinct;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.database.core.DriverManager;
import com.capgemini.mrchecker.database.tags.UnitTest;
import com.capgemini.mrchecker.database.utils.ConcurrencyUtils;

@UnitTest
public class DriverManagerTest {
	private static EntityManager	test_em1;

	private final List<EntityManager> managers = new ArrayList<>();

	@BeforeEach
	public void init() {
		DriverManager.closeDriver();
	}
	
	@AfterEach
	public void cleanup() {
		managers.forEach(EntityManager::close);
		managers.clear();
	}
	
	@AfterAll
	public static void cleanupSuite() {
		DriverManager.closeDriver();
	}
	
	@Test
	public void shouldCreateEntityManager() {
		test_em1 = DriverManager.createEntityManager(SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK);
		managers.add(test_em1);

		assertThat(test_em1, is(notNullValue()));
	}
	
	@Test
	public void shouldCreateEntityManagerCalledTwiceReturnDifferentInstances() {
		test_em1 = DriverManager.createEntityManager(SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK);
		EntityManager test_em2 = DriverManager.createEntityManager(SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK);
		managers.add(test_em1);
		managers.add(test_em2);

		assertThat(test_em1, is(notNullValue()));
		assertThat(test_em1, is(not(equalTo(test_em2))));
	}
	
	@Test
	public void shouldCloseDriver() {
		test_em1 = DriverManager.createEntityManager(SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK);
		managers.add(test_em1);
		assertThat(test_em1, is(notNullValue()));
		
		DriverManager.closeDriver();
		
		assertThrows(IllegalStateException.class, () -> test_em1.flush());
	}
	
	@Test
	public void shouldCloseDriverTwice() {
		test_em1 = DriverManager.createEntityManager(SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK);
		managers.add(test_em1);
		assertThat(test_em1, is(notNullValue()));
		
		DriverManager.closeDriver();
		DriverManager.closeDriver();
		
		assertThrows(IllegalStateException.class, () -> test_em1.flush());
	}
	
	@Test
	public void shouldCreateEntityManagerMultithreaded() throws InterruptedException {
		managers.addAll(ConcurrencyUtils.getInstancesConcurrently(() -> DriverManager.createEntityManager(SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK)));
		assertThat(managers, isCollectionDistinct());
	}
}
