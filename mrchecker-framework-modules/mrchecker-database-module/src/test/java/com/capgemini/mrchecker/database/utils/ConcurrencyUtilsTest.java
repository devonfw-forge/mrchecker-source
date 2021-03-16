package com.capgemini.mrchecker.database.utils;

import static com.capgemini.mrchecker.database.utils.IsCollectionDistinct.isCollectionDistinct;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ConcurrencyUtilsTest {
	private static final Object[] distinctObjects = { new Object(), new Object(), new Object() };
	
	@Test
	void shouldReturnTrue() {
		assertThat(isCollectionDistinct().matches(Arrays.asList(distinctObjects)), is(equalTo(true)));
	}
	
	@Test
	void shouldReturnFalse() {
		List<Object> collectionWithDuplicate = new ArrayList<>(Arrays.asList(distinctObjects));
		collectionWithDuplicate.add(distinctObjects[0]);
		assertThat(isCollectionDistinct().matches(collectionWithDuplicate), is(equalTo(false)));
	}
}