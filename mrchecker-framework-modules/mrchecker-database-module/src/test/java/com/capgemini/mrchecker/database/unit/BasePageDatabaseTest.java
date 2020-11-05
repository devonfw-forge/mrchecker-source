package com.capgemini.mrchecker.database.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import com.capgemini.mrchecker.database.core.BasePageDatabase;
import com.capgemini.mrchecker.database.tags.UnitTest;
import com.capgemini.mrchecker.test.core.ModuleType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static com.capgemini.mrchecker.database.mocks.PersistenceProviderMock.SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertTrue;

@UnitTest
public class BasePageDatabaseTest {

    private BasePageDatabase sut;

    @AfterEach
    public void cleanup() {
        if(!Objects.isNull(sut)) sut.onTestClassFinish();
    }

    @Test
    public void shouldCreateInstance() {
        sut = new TestBasePageDatabase();

        assertTrue(true);
    }

    @Test
    public void shouldCreateInstanceTwice() {
        sut = new TestBasePageDatabase();
        new TestBasePageDatabase();

        assertTrue(true);
    }

    @Test
    public void shouldReturnDatabaseModule() {
        sut = new TestBasePageDatabase();

        assertThat(sut.getModuleType(), is(equalTo(ModuleType.DATABASE)));
    }

    @Test
    public void shouldGetEntityManagerReturnReference() {
        sut = new TestBasePageDatabase();

        assertThat(sut.getEntityManager(), is(notNullValue()));
    }

    @Test
    public void shouldCallOnTestClassFinish() {
        sut = new TestBasePageDatabase();
        sut.onTestClassFinish();

        assertTrue(true);
    }

    @Test
    public void shouldCallOnTestClassFinishTwice() {
        sut = new TestBasePageDatabase();
        sut.onTestClassFinish();
        sut.onTestClassFinish();

        assertTrue(true);
    }

    @Test
    public void shouldGetAnalyticsReturnReference() {
        assertThat(BasePageDatabase.getAnalytics(), is(notNullValue()));
    }


    private static class TestBasePageDatabase extends BasePageDatabase {
        @Override
        public String getDatabaseUnitName() {
            return SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK;
        }
    }
}
