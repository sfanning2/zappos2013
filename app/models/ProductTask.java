package models;

import helpers.EmailClient;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.mail.MessagingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import play.Logger;
import ZapposAPI.ZapposRequestConsumer;

import com.avaje.ebean.Ebean;

public class ProductTask implements Callable<Object>, Comparable<ProductTask> {
	
	private final int priority;
	private long[] productIds;
	private Map<Long,Product> resultProducts;
	private Set<ProductWatch> originalWatches;
	public static final String CONTENT_FORMAT = "Hello! The product you followed \"%s\" in \"%s\" is on sale for %s off or more!\n"
			+ "Click the following link to be taken to the product page: %s\n"
			+ "Thank you!\n"
			+ "Sarah Fanning 2013 Mindsumo Submission";
	public static final String SUBJECT = "";
	/**
	 * Creates a ProductTask with the specified watches 
	 * at the lowest priority.
	 * @param originalWatches	The product watches to save back to in the database.
	 */
	public ProductTask(Set<ProductWatch> originalWatches) {
		this(originalWatches, Integer.MAX_VALUE);
	}

	/**
	 * Creates a ProductTask with the specified watches 
	 * at the specified priority.
	 * @param originalWatches	The product watches to save back to in the database.
	 * @param priority			The priority of the task. 
	 * 							A lower number represents a higher priority.
	 */
	public ProductTask(Set<ProductWatch> originalWatches, int priority) {
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
	public int compareTo(ProductTask otherTask) {
		return this.priority - otherTask.priority;
	}

	@Override
	public Object call() throws Exception {
		Logger.debug("Product Task is running...");
		resultProducts = getProducts();
		Logger.debug("Got Products: " + resultProducts.size());
		if (resultProducts != null) {
			for (ProductWatch w: originalWatches) {
				// Get corresponding product and update it
				Product newProduct = resultProducts.get(w.getProductId());
				Logger.debug("Saving new product info...");
				w.setTheProduct(newProduct);
				Ebean.save(newProduct);
				Ebean.save(w);
				Logger.debug("Completed saving product info.");
				Set<Style> newStyles = newProduct.getStyles();
				for (Style newStyle: newStyles) {
					double off = newStyle.getPercentOffAsDouble();
					if (off >= 20.0 - 0.000001) {
						Logger.debug("20%+");
						EmailClient eClient = new EmailClient();
						String[] to = {w.getEmailAddress()};
						try {
							Logger.debug("Sending email...");
							eClient.sendEmail(to, "Zappos Sale!", String.format(
									CONTENT_FORMAT, 
									newProduct.getProductName(),
									newStyle.getColor(),
									newStyle.getPercentOff(),
									newProduct.getDefaultProductUrl()));
							// If sent, then delete items
							Logger.debug("Deleting watch...");
							Ebean.delete(w);
							Logger.debug("Completed deleting watch.");
						} catch (MessagingException e) {
							Logger.error("Problem sending message.", e);
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Get the products from the Zappos API, 
	 * and respond to any status issues.
	 * @return	A Map of the products from the remote call
	 * 			or null if there was a problem
	 */
	private Map<Long, Product> getProducts() {
		Map<Long, Product> products = null;
		try {
			products = 
					ZapposRequestConsumer.getInstance()
					.requestProducts(productIds).get();
		} catch (WebApplicationException e) {
			// remove from db unless timeout
			// figure out what needs to be removed in mixed response
			// in all bad response, remove all?
			Response r = e.getResponse();
			int status = r.getStatus();
			// TODO: Process bad responses
			Logger.error("Non 2xx response: " + status, e);
			if (status >= 300) {

			} else if (status < 200) {

			} else {
			}	
		} catch (InterruptedException e) {
			Logger.error("API call interrupted", e);
		} catch (ExecutionException e) {
			Logger.error("API call had a problem while executing", e);
		}

		return products;
	}
}
