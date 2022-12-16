package com.capgemini.mrchecker.test.core.unit;

import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

@UnitTest
public class ModuleTypeTest {

    @Test
    public void shouldGetEnum() {
        ModuleType sut = ModuleType.DATABASE;

        assertThat(sut, is(notNullValue()));
    }
}
