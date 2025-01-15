package com.capgemini.mrchecker.jemmy.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ObjectPool<T> {
	private final BlockingQueue<T> pool;
	private static ObjectPool instance;

	private ObjectPool(int size, ObjectFactory<T> factory) {
		this.pool = new LinkedBlockingQueue<>(size);
		for (int i = 0; i < size; i++) {
			pool.offer(factory.createObject(i));
		}
	}

	public static synchronized <T> ObjectPool<T> getInstance(int size, ObjectFactory<T> factory) {
		if (instance == null) {
			instance = new ObjectPool<>(size, factory);
		}
		return instance;
	}

	public T borrowObject() throws InterruptedException {
		return pool.take();
	}

	public void returnObject(T obj) {
		pool.offer(obj);
	}

	public interface ObjectFactory<T> {
		T createObject(int index);
	}
}
