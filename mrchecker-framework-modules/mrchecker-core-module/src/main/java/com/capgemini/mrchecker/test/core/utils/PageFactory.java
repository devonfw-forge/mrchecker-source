package com.capgemini.mrchecker.test.core.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;

/**
 * This class supports pages creation. It instantiates a page of the given class using a no-arguments constructor
 * or a constructor with arguments. After the requested class instance is created it's automatically added to
 * test execution observer for notifications.
 * <p/>
 * Primitive types in constructors are not allowed. Use object types instead e.g. Integer
 */
public class PageFactory {
	private PageFactory() {
	}
	
	public static <T extends Page> T getPageInstance(Class<T> pageClass) {
		try {
			T pageInstance = pageClass.newInstance();
			pageInstance.addToTestExecutionObserver();
			return pageInstance;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new BFInputDataException("Could not instantiate class: " + pageClass.getName());
		}
	}
	
	public static <T extends Page> T getPageInstance(Class<T> pageClass, Object... constructorArgs) {
		Class<?>[] argsClassArray = new Class<?>[constructorArgs.length];
		argsClassArray = Arrays.stream(constructorArgs)
				.map(Object::getClass)
				.collect(Collectors.toList())
				.toArray(argsClassArray);
		
		try {
			T pageInstance = pageClass.getConstructor(argsClassArray)
					.newInstance(constructorArgs);
			pageInstance.addToTestExecutionObserver();
			return pageInstance;
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new BFInputDataException("Could not instantiate class: " + pageClass.getName());
		}
	}
	
	public static <T extends Page> T getPageInstance(String pageClassName) {
		try {
			Class<T> pageClass = (Class<T>) Class.forName(pageClassName);
			return getPageInstance(pageClass);
		} catch (ClassNotFoundException e) {
			throw new BFInputDataException("Could not find a class of the name: " + pageClassName);
		}
	}
	
	public static <T extends Page> T getPageInstance(String pageClassName, Object... constructorArgs) {
		try {
			Class<T> pageClass = (Class<T>) Class.forName(pageClassName);
			return getPageInstance(pageClass, constructorArgs);
		} catch (ClassNotFoundException e) {
			throw new BFInputDataException("Could not find a class of the name: " + pageClassName);
		}
	}
}
