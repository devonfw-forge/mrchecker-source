package com.capgemini.mrchecker.test.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.jupiter.api.Test;

public class StringTest extends BaseTest {
	
	@Test
	public void test1() throws InterruptedException {
		Thread.sleep(500);
		System.out.println("test1()");
		assertThat(true, is(equalTo(true)));
	}
	
	@Test
	public void test2() throws InterruptedException {
		Thread.sleep(500);
		System.out.println("test2()");
		assertThat(true, is(equalTo(true)));
	}
	
	@Test
	public void test3() throws InterruptedException {
		Thread.sleep(500);
		System.out.println("test3()");
		assertThat(true, is(equalTo(true)));
	}
	
	@Test
	public void test4() throws InterruptedException {
		Thread.sleep(500);
		System.out.println("test4()");
		assertThat(true, is(equalTo(true)));
	}
	
	@Test
	public void test5() throws InterruptedException {
		Thread.sleep(500);
		System.out.println("test5()");
		assertThat(true, is(equalTo(true)));
	}
	
	@Override
	public void tearDown() {
		System.out.println("tearDown()");
	}
	
	@Override
	public void setUp() {
		System.out.println("setUp()");
	}
	
}
