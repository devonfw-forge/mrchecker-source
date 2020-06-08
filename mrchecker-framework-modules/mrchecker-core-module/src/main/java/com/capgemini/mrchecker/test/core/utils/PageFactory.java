package com.capgemini.mrchecker.test.core.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;

/**
 * This class supports pages creation. It instantiates a page of the given class using a no-arguments constructor
 * or a constructor with arguments.
 * Primitive types in constructors are not allowed. Use object types instead e.g. Integer
 */
public class PageFactory {
	private PageFactory() {
	}
	
	public static <T extends Page> T getPageInstance(Class<T> clazz) {
		try {
			T basePage = clazz.newInstance();
			basePage.register();
			return basePage;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new BFInputDataException("Could not instantiate class: " + clazz.getName());
		}
	}
	
	public static <T extends Page> T getPageInstance(Class<T> clazz, Object... args) {
		Class<?>[] argsClass = new Class<?>[args.length];
		argsClass = Arrays.stream(args)
				.map(Object::getClass)
				.collect(Collectors.toList())
				.toArray(argsClass);
		
		try {
			T basePage = clazz.getConstructor(argsClass)
					.newInstance(args);
			basePage.register();
			return basePage;
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new BFInputDataException("Could not instantiate class: " + clazz.getName());
		}
	}
}
