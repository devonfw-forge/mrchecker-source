package com.capgemini.mrchecker.database.integration;

import com.capgemini.mrchecker.database.core.DriverManager;
import com.capgemini.mrchecker.database.tags.IntegrationTest;
import com.capgemini.mrchecker.database.tags.UnitTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.Objects;

import static com.capgemini.mrchecker.database.mocks.PersistenceProviderMock.SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
public class DriverManagerTest {
	public static EntityManager	test_em1;
	
	@BeforeEach
	public void init() {
		DriverManager.closeDriver();
	}
	
	@AfterEach
	public void cleanup() {
		if (!Objects.isNull(test_em1)) {
			test_em1.close();
		}
	}
	
	@AfterAll
	public static void cleanupSuite() {
		DriverManager.closeDriver();
	}

	@Test
	public void shouldCreateEntityManagerParametrized() {
		test_em1 = DriverManager.createEntityManager(DriverManager.PARAMETRIZED_PERSISTENCE_UNIT);

		assertThat(test_em1, is(notNullValue()));
	}
}
