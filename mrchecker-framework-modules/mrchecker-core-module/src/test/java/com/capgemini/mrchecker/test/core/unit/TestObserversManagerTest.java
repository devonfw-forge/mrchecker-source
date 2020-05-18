package com.capgemini.mrchecker.test.core.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.capgemini.mrchecker.test.core.ITestObserver;
import com.capgemini.mrchecker.test.core.TestObserversManager;

public class TestObserversManagerTest {
	
	private static TestObserversManager sut;
	
	@BeforeAll
	public static void setUpClass() {
		sut = TestObserversManager.getInstance();
	}
	
	@AfterEach
	public void tearDown() {
		sut.removeAllObservers();
	}
	
	@Test
	public void shouldGetInstanceReturnSingleton() {
		assertThat(TestObserversManager.getInstance(), is(sameInstance(sut)));
	}
	
	@Test
	public void shouldGetAllPluginsInitiallyReturnNoPlugins() {
		assertThat(TestObserversManager.getInstance()
				.getAllObservers()
				.size(), is(equalTo(0)));
	}
	
	@Test
	public ITestObserver shouldAddPlugin() {
		ITestObserver mockObserver = mock(ITestObserver.class);
		
		sut.addObserver(mockObserver);
		
		assertThat(TestObserversManager.getInstance()
				.getAllObservers(), hasItem(mockObserver));
		
		return mockObserver;
	}
	
	@Test
	public void shouldRemovePlugin() {
		ITestObserver mockObserver = shouldAddPlugin();
		
		sut.removeObserver(mockObserver);
		
		assertThat(TestObserversManager.getInstance()
				.getAllObservers(), not((hasItem(mockObserver))));
	}
}
