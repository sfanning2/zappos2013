package helpers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;

import models.ProductWatch;
import play.db.ebean.Model;

// Should be run once only?
// RequestProducer
public class Poller implements Runnable{

	@Override
	public void run() {
		Executor e = Consumer.getInstance();
		CompletionService<T> ecs = new ExecutorCompletionService<T>();
		// Spawn collection of Callable objects
		Set<Callable<T>> jobChunks = new HashSet<Callable<T>>();
		// Get stuff from db
		List<ProductWatch> watches = Poller.getWatches();
		for (ProductWatch w : watches) {
			
		}
		// Chunk it into groups of 10
		for (Callable<T> jobChunk : jobChunks)
	           ecs.submit(jobChunk);
	       int n = jobChunks.size();
	       for (int i = 0; i < n; ++i) {
	           T r = ecs.take().get();
	           // Logic goes below
	           if (r != null)
	               use(r);
	       }
		
		
		// Send out jobs to the PriorityBlockingQueue for api
		// Job are include the old and the new
		
		// 1
		// Wait a certain amount of time before making another db call
		// Repeat
		
		
		// 2
		// Need to know if finished....
		// Is it okay to get it randomly
		// As they finish, check the percent offs (another job?)
		// Write where necessary.
		// Once they all finish ask db again
		
		
		
	}
	
	//checkout findPagingList for Model.Finder
	public static List<ProductWatch> getWatches() {
		List<ProductWatch> watches = 
    			new Model.Finder<String,ProductWatch>(String.class, ProductWatch.class)
    			.all();
		return watches;
	}
	
}
