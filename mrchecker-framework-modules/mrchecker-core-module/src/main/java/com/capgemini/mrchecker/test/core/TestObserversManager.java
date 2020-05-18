package com.capgemini.mrchecker.test.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.capgemini.mrchecker.test.core.base.encryption.providers.DataEncryptionService;

public class TestObserversManager {
	private static TestObserversManager	instance;
	private final List<ITestObserver>	observers	= new ArrayList<>();
	
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
		observers.add(observer);
	}
	
	public void removeObserver(ITestObserver observer) {
		observers.remove(observer);
	}
	
	public void removeAllObservers() {
		observers.clear();
	}
	
	public List<ITestObserver> getAllObservers() {
		return new ArrayList<>(observers);
	}
}
