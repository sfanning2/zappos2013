package controllers;

import helpers.ZapposClient;

import java.io.IOException;
import java.util.List;

import com.avaje.ebean.Ebean;

import models.Product;
import models.ProductWatch;
import play.*;
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
    
    public static Result index(List<Product> list) {
    	// Pass arguments
    	
        return ok(index.render(list));
    }
    
    public static Result addWatch() {
    	Form<ProductWatch> watchForm = Form.form(ProductWatch.class);
    	Form<ProductWatch> submission = watchForm.bindFromRequest();
    	ProductWatch watch = submission.get();
    	int errors = 0;
    	// Get the product from Zappos
    	try {
			Product product = ZapposClient.getProduct(watch.productId);
			watch.setPercentsOff(product);
		} catch (IOException e) {
			e.printStackTrace();
			errors++;
		}
    	// Store
    	// Validation logic goes in this method
    	// Verify the productId is valid
//    	String errors = watch.validate();
    	if (errors == 0) {
    		Ebean.save(watch);
    		watch.save();
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
    
    public static Result search() {
    	return redirect(routes.Application.index());
    }
  
}
