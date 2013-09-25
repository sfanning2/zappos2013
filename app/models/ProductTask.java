package models;

import helpers.EmailClient;
import helpers.ZapposClient;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.avaje.ebean.Ebean;

public class ProductTask implements Callable<Object>, Comparable<ProductTask> {

	private final int priority;
	private long[] productIds;
	private Set<Product> resultProducts;
	private Set<ProductWatch> originalWatches;
	
	/**
	 * Creates a ProductTask with the specified watches 
	 * at the lowest priority.
	 * @param originalWatches	The product watches to save back to.
	 */
	ProductTask(Set<ProductWatch> originalWatches) {
		this(originalWatches, Integer.MAX_VALUE);
	}
	
	/**
	 * Creates a ProductTask with the specified watches 
	 * at the specified priority.
	 * @param originalWatches	The product watches to save back to.
	 * @param priority			The priority of the task. 
	 * 							A lower number represents a higher priority.
	 */
	ProductTask(Set<ProductWatch> originalWatches, int priority) {
		this.priority = priority;
		this.originalWatches = originalWatches;
		productIds = new long[originalWatches.size()];
		int i = 0;
		for (ProductWatch w : originalWatches) {
			productIds[i] = w.getProductId();
			i++;
		}
	}
	
	@Override
	public Object call() throws Exception {
		
		resultProducts = null;
		try {
			resultProducts = ZapposClient.getProducts(productIds);
		} catch (IOException e) {
			// Figure out if needs to be removed from db
			// TODO: Fill in IOException stuff
			// TODO: Change client to use WebApp Exceptions
		} catch (WebApplicationException e) {
			// remove from db unless timeout
			// figure out what needs to be removed in mixed response
			// in all bad response, remove all?
			Response r = e.getResponse();
			int status = r.getStatus();
			// TODO: Process bad responses
			if (status >= 300) {
				
			} else if (status < 200) {
				
			} else {
				// Why are we here
				// Something is very weird
				// TODO: Log it!
			}
			
		}

		// TODO: what if null?
		if (resultProducts != null) {
			for (ProductWatch w: originalWatches) {
				// TODO: Get corresponding product and update it
				Product p = this.getCorrespondingResult(w);
				Set<Style> pStyles = p.getStyles();
				Set<Style> wStyles = w.getTheProduct().getStyles();
				for (Style ps: pStyles) {
					double off = -1;
					try {
						off = Double.parseDouble(
								ps.getPercentOff()
								.replaceAll("%", ""));
					} catch (NumberFormatException e) {
						// TODO: should this ever happen?
						// Log it at least
					}
					if (off >= 20) {
						// Check if changed
						Style ws = ps.getMatchingStyle(wStyles);
						if (ps != ws) {
							// Notify if it has
							EmailClient eClient = new EmailClient(null, null, null);
							String[] to = {w.getEmailAddress()};
							// TODO: email stuff
							// Possibly in thread?
							eClient.sendEmail(to, "subject", "content");
						}
						
					}
					// Save either way
					// Could cause locking
					// TODO: Add synchronized method to do db deletes/saves/reads?
					Ebean.delete(w.getTheProduct());
					w.setTheProduct(p);
					Ebean.save(w);
				}
			}
		}
		
		return null;
	}

	private Product getCorrespondingResult(ProductWatch w) {
		long id = w.getProductId();
		for (Product p : resultProducts) {
			if (p.getProductId() == id) {
				return p;
			}
		}
		return null;
	}

	@Override
	public int compareTo(ProductTask otherTask) {
		return this.priority - otherTask.priority;
	}
	
}
