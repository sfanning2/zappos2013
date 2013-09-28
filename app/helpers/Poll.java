package helpers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

import models.ProductTask;
import models.ProductWatch;
import play.Logger;
import play.db.ebean.Model;

public final class Poll implements Runnable{
	
	private static volatile boolean stopRequested;
	
	@Override
	public void run() {
		stopRequested = false;
		Logger.debug("Poller has started");
		while(!stopRequested) {
			// Spawn collection of Callable objects
			Set<ProductTask> jobChunks = new HashSet<ProductTask>();
			// Get watches from the database
			List<ProductWatch> watches = getWatches();
		
			// Chunk watches into groups and make tasks
			Set<ProductWatch> watchGroup = new HashSet<ProductWatch>();;
			int groupSize = 10;
			int c = 0;
			Logger.debug("About to iterate watches...");
			Logger.debug("Number: " + watches.size());
			for (ProductWatch w : watches) {
				// Add a watch to the current group
				watchGroup.add(w);
				c++;
				if (c % groupSize == 0) {
					// Add the group
					ProductTask pt = new ProductTask(watchGroup);
					jobChunks.add(pt);
					// Create the next group
					watchGroup = new HashSet<ProductWatch>();
				}
			}
			if (watchGroup.size() > 0) {
				// Add the group
				ProductTask pt = new ProductTask(watchGroup);
				jobChunks.add(pt);
			}
			Logger.debug("Completed iterating watches..." + watchGroup.size());

			Executor e = Executors.newFixedThreadPool(1);
			CompletionService<Object> ecs = new ExecutorCompletionService<Object>(e);

			for (ProductTask jobChunk : jobChunks) {
				Logger.debug("Submitting job...");
				ecs.submit(jobChunk);
			}
			int n = jobChunks.size();
			for (int i = 0; i < n; ++i) {
				try {
					ecs.take();
				} catch (InterruptedException e1) {
					Logger.error("Poller interrupted during taking.", e1);
				}
			}

			// Wait a certain amount of time before making another db call
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e1) {
				Logger.error("Poller interrupted during sleep.", e1);
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
