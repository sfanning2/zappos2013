package test.helpers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import helpers.ZapposClient;
import models.Product;

import org.codehaus.jackson.JsonParseException;
import org.junit.*;

import play.mvc.*;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class ZapposClientTest {
	
	public ZapposClient client;
	public static long[] validIds;
	public static long[] invalidIds;
	
	@BeforeClass
	public static void beforeClass() {
		validIds = new long[4];
		validIds[0] = 8234462;
		validIds[1] = 7534148;
		validIds[2] = 8025371;
		validIds[3] = 7634257;
		invalidIds = new long[4];
		for (int i = 0; i < invalidIds.length; i++) {
			invalidIds[i] = i;
		}
	}
	@Before
	public void before() throws InterruptedException {
		Thread.sleep(500);
	}
	
	// No items
	@Test (expected = RuntimeException.class)
	public void noItems() throws JsonParseException, IOException {
		ZapposClient.getProducts(new long[0]);
	}
	// Single valid item
	@Test
	public void singleItem() throws JsonParseException, IOException {
		Product result = ZapposClient.getProduct(validIds[0]);
	}
	// Single invalid item
	@Test (expected = RuntimeException.class)
	public void singleItemInvalid() throws JsonParseException, IOException {
		ZapposClient.getProduct(invalidIds[0]);
	}
	// Multiple valid items
	@Test
	public void multipleItems() throws JsonParseException, IOException {
		Set<Product> result = ZapposClient.getProducts(validIds);
	}
	
	// Valid items and invalid items
	@Test
	public void mixedItems() throws JsonParseException, IOException {
		long[] mix = Arrays.copyOf(validIds, Math.min(validIds.length + invalidIds.length, 10));
		for (int i = validIds.length; i < mix.length; i++) {
			mix[i] = invalidIds[i - validIds.length];
		}
		ZapposClient.getProducts(mix);
	}
	
	// Ten items
	
	// Eleven items
	@Test (expected = IllegalArgumentException.class)
	public void elevenItems() throws JsonParseException, IOException {
		long[] eleven = new long[11];
		Arrays.fill(eleven, 1234567);
		ZapposClient.getProducts(eleven);
	}
	
	// Search
	@Test
	public void search() throws IOException {
		String query = "boots";
		ZapposClient.search(query);
	}
}
