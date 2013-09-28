package helpers;

import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

import models.ProductTask;

/**
 * Singleton class to consume and schedule tasks
 * @author Sarah
 *
 */
public class Consumer {

	private static final Consumer INSTANCE = 
			new Consumer();
	private final PriorityBlockingQueue<ProductTask> queue = 
			new PriorityBlockingQueue<ProductTask>();
	private final ScheduledExecutorService scheduler =
			Executors.newScheduledThreadPool(1);// more than one?
	// shutdown?
	private Consumer() {
	}
	
	public static Consumer getInstance() {
		return INSTANCE;
	}
	
	public PriorityBlockingQueue<ProductTask> getQueue() {
		return queue;
	}
	
	public void placeRequest() {
		// Callable Request 
	}
}
