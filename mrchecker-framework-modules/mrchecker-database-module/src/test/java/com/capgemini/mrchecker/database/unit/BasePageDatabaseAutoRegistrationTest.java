package com.capgemini.mrchecker.database.unit;

import com.capgemini.mrchecker.database.core.BasePageDatabaseAutoRegistration;
import com.capgemini.mrchecker.database.tags.UnitTest;
import org.junit.jupiter.api.Test;

import static com.capgemini.mrchecker.database.mocks.PersistenceProviderMock.SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UnitTest
public class BasePageDatabaseAutoRegistrationTest {

    @Test
    public void shouldCreateInstance() {
        new BasePageDatabaseAutoRegistration() {
            @Override
            public String getDatabaseUnitName() {
                return SUPPORTED_PERSISTENCE_UNIT_NAME_FOR_MOCK;
            }
        };

        assertTrue(true);
    }
}