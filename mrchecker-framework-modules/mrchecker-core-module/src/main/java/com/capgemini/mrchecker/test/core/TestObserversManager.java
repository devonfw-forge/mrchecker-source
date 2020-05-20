package com.capgemini.mrchecker.test.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.capgemini.mrchecker.test.core.base.encryption.providers.DataEncryptionService;

public class TestObserversManager {
	private static TestObserversManager instance;
	
	// TODO: introduce an object pool?
	private final ThreadLocal<List<ITestObserver>>	observers		= ThreadLocal.withInitial(ArrayList::new);
	private final ThreadLocal<List<ITestObserver>>	classObservers	= ThreadLocal.withInitial(ArrayList::new);
	
	public static TestObserversManager getInstance() {
		if (Objects.isNull(instance)) {
			synchronized (DataEncryptionService.class) {
				if (Objects.isNull(instance)) {
					instance = new TestObserversManager();
				}
			}
		}
		
		return instance;
	}
	
	public void addObserver(ITestObserver observer) {
		observers.get()
				.add(observer);
	}
	
	public void removeObserver(ITestObserver observer) {
		observers.get()
				.remove(observer);
	}
	
	public void removeAllObservers() {
		observers.get()
				.clear();
	}
	
	public List<ITestObserver> getAllObservers() {
		return new ArrayList<>(observers.get());
	}
}
