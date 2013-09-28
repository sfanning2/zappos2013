package ZapposAPI;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import models.Product;
import models.Result;

public final class ZapposRequestConsumer {
	/** Singleton instance of the class */
	private static final ZapposRequestConsumer INSTANCE = 
			new ZapposRequestConsumer();
	
	/** Scheduler to restrict access to the Zappos API */
	private final ThreadPoolExecutor scheduler;
	
	/** Limits imposed by the Zappos API */
	static final long CALLS_PER_SECOND = 2;
	static final long CALLS_PER_DAY = 2500;
	static final long MILLISECONDS_DELAY = 1000 / CALLS_PER_SECOND;
	
	private ZapposRequestConsumer() {
		scheduler = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<Runnable>()); {
		}
	}
	
	public static ZapposRequestConsumer getInstance() {
		return INSTANCE;
	}
	
	public Future<Map<Long,Product>> requestProducts(long[] productIds){
		return scheduler.submit(new ProductRequest(productIds));
	}
	
	public Future<Map<Long,Product>> requestProducts(long[] productIds, int priority){
		return scheduler.submit(new ProductRequest(productIds, priority));
	}
	
	public Future<List<Result>> requestSearch(String query){
		return scheduler.submit(new SearchRequest(query));
	}
	
	public Future<List<Result>> requestSearch(String query, int priority){
		return scheduler.submit(new SearchRequest(query, priority));
	}
	
	private class ProductRequest extends ZapposRequest<Map<Long,Product>> {
		private long[] productIds;
		public ProductRequest(long[] productIds) {
			this.productIds = productIds;
		}
		public ProductRequest(long[] productIds, int priority) {
			super(priority);
			this.productIds = productIds;
		}
		@Override
		public Map<Long,Product> call() throws Exception {
			// Request the products
			return ZapposClient.getProducts(productIds);
		}
	}
	
	private class SearchRequest extends ZapposRequest<List<Result>> {
		private String query;
		public SearchRequest(String query) {
			this.query = query;
		}
		public SearchRequest(String query, int priority) {
			super(priority);
			this.query = query;
		}
		@Override
		public List<Result> call() throws Exception {
			// Search for matching styles
			return ZapposClient.search(query);
		}
	}
	
}
