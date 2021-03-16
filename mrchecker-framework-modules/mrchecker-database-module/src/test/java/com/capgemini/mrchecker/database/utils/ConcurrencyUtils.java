package com.capgemini.mrchecker.database.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ConcurrencyUtils {
	
	private static final int THREAD_COUNT = 8;
	
	private ConcurrencyUtils() {
	}
	
	public static <T> List<T> getInstancesConcurrently(Supplier<T> instanceSupplier) throws InterruptedException {
		Collection<InstanceGetter<T>> tasks = new ArrayList<>();
		InstanceGetter<T> instanceGetter = new InstanceGetter<>(instanceSupplier);
		for (int i = 0; i < THREAD_COUNT; i++) {
			tasks.add(instanceGetter);
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		List<Future<T>> instances = executor.invokeAll(tasks);
		executor.shutdown();
		executor.awaitTermination(300, TimeUnit.MILLISECONDS);
		
		return instances.stream()
				.map(future -> {
					try {
						return future.get();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
					return null;
				})
				.collect(Collectors.toList());
	}
	
	private static class InstanceGetter<T> implements Callable<T> {
		
		private static final CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT);
		
		private final Supplier<T> instanceSupplier;
		
		public InstanceGetter(Supplier<T> instanceSupplier) {
			this.instanceSupplier = instanceSupplier;
		}
		
		@Override
		public T call() throws Exception {
			barrier.await();
			return instanceSupplier.get();
		}
	}
}