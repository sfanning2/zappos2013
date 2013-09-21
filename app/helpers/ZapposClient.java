package helpers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.sun.jersey.api.client.*;

import models.Product;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

public final class ZapposClient {
	private static final String site = "http://api.zappos.com/";
	private static final String key = "52ddafbe3ee659bad97fcce7c53592916a6bfd73";
	
	private ZapposClient() {
		
	}
	
	/**
	 * 
	 * @param ids	And array of ten or fewer item ids
	 * @return
	 * @throws IOException 
	 * @throws JsonParseException 
	 */
	public static Set<Product> getProducts(long[] ids) throws IOException {
		if (ids.length > 10)
			throw new IllegalArgumentException("The number of ids may not exceed ten.");
		
		StringBuilder builder = new StringBuilder(site);
		builder.append("Product/");
		// Add ids
		for (long id : ids) {
			builder.append(id).append(",");
		}
		// Remove excess comma
		int extraComma;
		if ((extraComma = builder.lastIndexOf(",")) != -1) {
			builder.deleteCharAt(extraComma);
		}
		builder.append("?includes=[%22styles%22]");
		builder.append("&key=").append(key);
		
		String req = makeRequest(builder.toString());
		return convertToProducts(req);
	}
	
	public static Product getProduct(long id) throws IOException {
		long[] ids = new long[1];
		ids[0] = id;
		Set<Product> products = getProducts(ids);
		if (products.size() != 1) {
			throw new IllegalStateException("There should be exactly one product.");
		}
		Product product = null;
		for (Product p : products) {
			product = p;
		}		
		return product;
	}
	
	public static String search(String query) throws IOException {
		// /Search?term=<SEARCH_TERM> or /Search/term/<SEARCH_TERM>
		StringBuilder builder = new StringBuilder(site);
		builder.append("Search?term=").append(query);
		builder.append("&key=").append(key);
		return makeRequest(builder.toString());
	}
	
	/**
	 * Make a request to the specified url
	 * @param resource	The resource to make the request to
	 * @return			The json result of the request
	 * @throws IOException 
	 */
	public static String makeRequest(String resource) throws IOException {
		Client client = Client.create();
		WebResource webResource = client.resource(resource);

		ClientResponse response = webResource.accept("application/json")
				.get(ClientResponse.class);
		
		int status = response.getStatus();
		if (status < 200 || status >= 300) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}
		//InputStream stream = response.getEntityInputStream();
		//String str = "UTF-8";
		//String output = IOUtils.toString(stream, str);
		String output = response.getEntity(String.class);
		//stream.close();
		response.close();
		return output;
	}
	
	public static Set<Product> convertToProducts(String json) throws IOException {
		Set<Product> products = new HashSet<Product>();
		JsonFactory f = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		JsonParser parser = f.createJsonParser(json);
		
		// Get the root node
		JsonNode root = mapper.readTree(parser);
		JsonNode productRoot = root.get("product");
		
		Product product;
		if (productRoot.isArray()) {
			for (JsonNode node : productRoot) {
				product = mapper.readValue(node, Product.class);
				products.add(product);
			}
		} else {
			product = mapper.readValue(productRoot, Product.class);
			products.add(product);
		}
		return products;
	}
}
