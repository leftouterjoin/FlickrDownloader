package flickrdownloader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class Test3 {
	@Test
	public void test1() {
		ExecutorService es = Executors.newFixedThreadPool(10);
		List<CompletableFuture<Void>> futures = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			final int n = i;
			futures.add(CompletableFuture.runAsync(() -> System.out.println("task + " + n), es));
		}
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[] {}));
	}

	class Item {
	}

	private Item search(int i) {
		return new Item();
	}

	@Test
	public void test2() {
		ExecutorService pool = Executors.newFixedThreadPool(10);
		List<CompletableFuture<Item>> futures = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			final int number = i;
			CompletableFuture<Item> f = CompletableFuture.supplyAsync(() -> {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				System.out.println("task + " + number);
				return search(number);
			}, pool);
			futures.add(f);
		}

		try {
			List<Item> summaryItem = new ArrayList<>();
			for (CompletableFuture<Item> f : futures) {
				summaryItem.add(f.get());
			}
			CompletableFuture.allOf(futures.toArray(new CompletableFuture[] {}));
		} catch (ExecutionException | InterruptedException ex) {
			throw new RuntimeException(ex);//適当に書いてます。
		}
	}

	@Test
	public void test3() {
		ExecutorService pool = Executors.newFixedThreadPool(10);
		try {
			for (int i = 0; i < 100; i++) {
				final int number = i;
				CompletableFuture.supplyAsync(() -> {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					System.out.println("task + " + number);
					return search(number);
				}, pool);
			}
		} finally {
			pool.shutdown();
			try {
				pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			} catch (InterruptedException ex) {
				throw new RuntimeException(ex);
			}
		}
	}
}
