package com.capgemini.mrchecker.database.integration.base;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.EntityNotFoundException;

import com.capgemini.mrchecker.database.tags.IntegrationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.database.core.BasePageDatabase;
import com.capgemini.mrchecker.database.core.DriverManager;
import com.capgemini.mrchecker.database.core.base.IDao;
import com.capgemini.mrchecker.database.integration.entity.Employee;

@IntegrationTest
public class AbstractDaoTest {
	
	private static final BasePageDatabase PARAMETRIZED_BASE_PAGE_DATABASE = new BasePageDatabase() {
		@Override
		public String getDatabaseUnitName() {
			return DriverManager.PARAMETRIZED_PERSISTENCE_UNIT;
		}
	};
	
	private static final Employee	TEST_EMPLOYEE_1		= new Employee(1, "name1", "surname1", 100);
	private static final Employee	TEST_EMPLOYEE_2		= new Employee(2, "name2", "surname2", 200);
	private static final Employee	TEST_EMPLOYEE_3		= new Employee(3, "name3", "surname3", 300);
	private static final Employee	NEW_EMPLOYEE_ENTITY	= new Employee(10, "name10", "surname10", 1000);
	private static final int		NO_SUCH_ID			= Integer.MAX_VALUE;
	
	private static final IDao<Employee, Integer> SUT = getSut();
	
	@BeforeEach
	public void init() {
		TEST_EMPLOYEE_1.setName("name1");
		SUT.deleteAll();
		SUT.save(TEST_EMPLOYEE_1);
		SUT.save(TEST_EMPLOYEE_2);
		SUT.save(TEST_EMPLOYEE_3);
	}
	
	@AfterAll
	public static void cleanUpSuite() {
		PARAMETRIZED_BASE_PAGE_DATABASE.onTestClassFinish();
	}
	
	private static IDao<Employee, Integer> getSut() {
		return PARAMETRIZED_BASE_PAGE_DATABASE.createDao(Employee.class, Integer.class);
	}
	
	@Test
	public void shouldConstructInstance() {
		IDao<Employee, Integer> sut = getSut();
		assertThat(sut, is(notNullValue()));
	}
	
	@Test
	public void shouldSave() {
		assertThat(SUT.save(NEW_EMPLOYEE_ENTITY), is(sameInstance(NEW_EMPLOYEE_ENTITY)));
		assertThat(SUT.findOne(NEW_EMPLOYEE_ENTITY.getId()), is(equalTo(NEW_EMPLOYEE_ENTITY)));
	}
	
	@Test
	public void shouldGetOne() {
		assertThat(SUT.getOne(TEST_EMPLOYEE_1.getId()), is(equalTo(TEST_EMPLOYEE_1)));
	}
	
	@Test
	public void shouldGetOneNoObjectExists() {
		assertThrows(EntityNotFoundException.class, () -> SUT.getOne(NO_SUCH_ID)
				.getName());
	}
	
	@Test
	public void shouldFindOne() {
		assertThat(SUT.findOne(TEST_EMPLOYEE_1.getId()), is(equalTo(TEST_EMPLOYEE_1)));
	}

	@Test
	public void shouldFindOneNoObjectExists() {
		assertThat(SUT.findOne(NO_SUCH_ID), is(nullValue()));
	}
	
	@Test
	public void shouldFindAll() {
		assertThat(SUT.findAll(), is(hasItems(TEST_EMPLOYEE_1, TEST_EMPLOYEE_2, TEST_EMPLOYEE_3)));
	}
	
	@Test
	public void shouldUpdate() {
		TEST_EMPLOYEE_1.setName("name1Altered");
		SUT.update(TEST_EMPLOYEE_1);

		assertThat(SUT.findOne(TEST_EMPLOYEE_1.getId())
				.getName(), is(equalTo("name1Altered")));
	}

	@Test
	public void shouldDeleteByEntity() {
		SUT.delete(TEST_EMPLOYEE_1);

		assertThat(SUT.findOne(TEST_EMPLOYEE_1.getId()), is(nullValue()));
	}

	@Test
	public void shouldDeleteById() {
		SUT.delete(TEST_EMPLOYEE_1.getId());
		assertThat(SUT.findOne(TEST_EMPLOYEE_1.getId()), is(nullValue()));
	}

	@Test
	public void shouldDeleteByIdNoObjectExists() {
		SUT.delete(NO_SUCH_ID);
		assertTrue(true);
	}

	@Test
	public void shouldDeleteAll() {
		SUT.deleteAll();
		assertThat(SUT.findAll()
				.size(), is(equalTo(0)));
	}

	@Test
	public void shouldCount() {
		assertThat(SUT.count(), is(equalTo(3L)));
	}

	@Test
	public void shouldExistsReturnTrue() {
		assertThat(SUT.exists(TEST_EMPLOYEE_1.getId()), is(equalTo(true)));
	}
	
	@Test
	public void shouldExistsReturnFalse() {
		assertThat(SUT.exists(Integer.MAX_VALUE), is(equalTo(false)));
	}
}