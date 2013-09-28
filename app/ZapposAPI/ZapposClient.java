package ZapposAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.WebApplicationException;

import com.sun.jersey.api.client.*;

import models.Product;
import models.Result;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

final class ZapposClient {
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
	public static Map<Long, Product> getProducts(long[] ids) throws IOException {
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
		return convertToProductMap(req);
	}
	
	public static Product getProduct(long id) throws IOException {
		long[] ids = new long[1];
		ids[0] = id;
		Map<Long, Product> products = getProducts(ids);
		if (products.size() != 1) {
			throw new IllegalStateException("There should be exactly one product.");
		}
		return products.get(id);		
	}
	
	public static List<Result> search(String query) throws IOException {
		// /Search?term=<SEARCH_TERM> or /Search/term/<SEARCH_TERM>
		StringBuilder builder = new StringBuilder(site);
		builder.append("Search?term=").append(query);
		builder.append("&key=").append(key);
		String req = makeRequest(builder.toString());
		return convertToResults(req);
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
			throw new WebApplicationException(
					response.getClientResponseStatus().getStatusCode());
		}
		String output = response.getEntity(String.class);
		response.close();
		return output;
	}
	
	public static Set<Product> convertToProductSet(String json) throws IOException {
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
	
	public static Map<Long,Product> convertToProductMap(String json) throws IOException {
		Map<Long,Product> products = new HashMap<Long,Product>();
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
				products.put(product.getProductId(), product);
			}
		} else {
			product = mapper.readValue(productRoot, Product.class);
			products.put(product.getProductId(), product);
		}
		return products;
	}
	
	public static List<Result> convertToResults(String json) throws IOException {
		List<Result> results = new ArrayList<Result>();
		JsonFactory f = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		JsonParser parser = f.createJsonParser(json);

		// Get the root node
		JsonNode root = mapper.readTree(parser);
		JsonNode resultRoot = root.get("results");
		
		Result result;
		if (resultRoot.isArray()) {
			for (JsonNode node : resultRoot) {
				result = mapper.readValue(node, Result.class);
				results.add(result);
			}
		} else {
			result = mapper.readValue(resultRoot, Result.class);
			results.add(result);
		}
		return results;
	}
}
