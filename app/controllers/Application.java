package controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ZapposAPI.ZapposRequestConsumer;

import com.avaje.ebean.Ebean;

import models.Product;
import models.ProductWatch;
import play.data.Form;
import play.data.validation.ValidationError;
import play.db.ebean.Model;
import play.mvc.*;
import views.html.*;
import views.html.helper.form;
import static play.libs.Json.toJson;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render(null));
	}

	public static Result index(List<models.Result> list) {
		return ok(index.render(list));
	}

	public static Result addWatch() {
		Form<ProductWatch> watchForm = Form.form(ProductWatch.class);
		Form<ProductWatch> submission = watchForm.bindFromRequest();
		ProductWatch watch = submission.get();
		int errors = 0;
		// Get the product from Zappos
		Product product = new Product();
		product.setProductId(watch.getProductId());
		watch.setTheProduct(product);

		// Validation logic goes in this method
		// Verify the productId is non-blank and has a positive long
		// Verify the email is non-blank and has @ and .
		if (errors == 0) {
			Ebean.save(product);
			Ebean.save(watch);
		}

		return redirect(routes.Application.index());
	}

	public static Result getWatches() {
		// 1st arg primary key type
		List<ProductWatch> watches = 
				new Model.Finder<String,ProductWatch>(String.class, ProductWatch.class)
				.all();
		return ok(toJson(watches));
	}

	public static Result getWatchesTest() {
		// 1st arg primary key type
		List<ProductWatch> watches = 
				new Model.Finder<String,ProductWatch>(String.class, ProductWatch.class)
				.all();
		for (ProductWatch watch : watches) {
			System.out.println(watch.getProductId() + " = " + watch.getTheProduct().getProductId());
		}
		return ok(toJson(watches));
	}

	public static Result search() throws IOException {
		List<models.Result> results = null;
		Map<String, String[]> formData = request().body().asFormUrlEncoded();
		String[] data = formData.get("searchTerms");
		if (data.length > 0) {
			try {
				String query = data[0];
				results = ZapposRequestConsumer.getInstance().requestSearch(query, 0).get();
			} catch (InterruptedException e) {

			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return index(results);
	}

}
