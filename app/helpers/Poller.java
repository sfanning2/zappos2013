package helpers;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

import models.ProductTask;
import models.ProductWatch;
import play.db.ebean.Model;

// Should be run once only?
// RequestProducer
public class Poller implements Runnable{
	
	private static volatile boolean stopRequested;
	
	@Override
	public void run() {
		while(!stopRequested) {
			// Spawn collection of Callable objects
			Set<ProductTask> jobChunks = new HashSet<ProductTask>();
			// Get stuff from the database
			List<ProductWatch> watches = Poller.getWatches();
			int c = 0;
			int groupSize = 10;
			Set<ProductWatch> watchGroup = null;
			// Chunk watches into groups and make tasks
			for (ProductWatch w : watches) {
				if (c % groupSize == 0) {
					// Add the finished group
					if ( watchGroup != null) {
						ProductTask pt = new ProductTask(watchGroup);
						jobChunks.add(pt);
					}
					// Create the next group
					watchGroup = new HashSet<ProductWatch>();
				}
				watchGroup.add(w);
			}

			Executor e = Executors.newFixedThreadPool(4);
			CompletionService<Object> ecs = new ExecutorCompletionService<Object>(e);

			for (ProductTask jobChunk : jobChunks)
				ecs.submit(jobChunk);
			int n = jobChunks.size();
			for (int i = 0; i < n; ++i) {
				try {
					Object r = ecs.take();//.get();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			// 1
			// Wait a certain amount of time before making another db call
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	//checkout findPagingList for Model.Finder
	public static List<ProductWatch> getWatches() {
		List<ProductWatch> watches = 
				new Model.Finder<String,ProductWatch>(String.class, ProductWatch.class)
				.all();
		return watches;
	}
	
	public static void requestStop() {
		stopRequested = true;
	}

}
