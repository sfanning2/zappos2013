package models;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import models.Product.Style;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@Table(name="e_watchtable")
public class ProductWatch extends Model{
	
	private static final long serialVersionUID = -1106982547808954964L;

	@Id
	public long id;
	
	@Required
	public long productId;
	
	@Required
	public String emailAddress;
	
	public Map<Long,String> percentsOff;

	public Map<Long,String> getPercentsOff() {
		return percentsOff;
	}

	public void setPercentsOff(Map<Long,String> percentsOff) {
		this.percentsOff = percentsOff;
	}
	
	public void setPercentsOff(Product product) {
		Map<Long,String> percentsOff = new HashMap<Long,String>();
		for (Style style : product.getStyles()) {
			percentsOff.put(style.getStyleId(), style.getPercentOff());
		}
		this.percentsOff = percentsOff;
	}
	
//	public String validate() {
//		String errors = null;
//		ZapposClient client = new ZapposClient();
//    	try {
//			setProduct(client.getProduct(Long.parseLong(productId)));
//		} catch (NumberFormatException e) {
//			// Invalid id format
//			// Add error to response
//			errors = "Please enter a valid productId. User numerical characters only";
//		} catch (IOException e) {
//			// This is probably bad on my side.
//			e.printStackTrace();
//			errors = "Something went wrong. Please try again.";
//		} catch (RuntimeException e) {
//			// Invalid status code
//			// The product you entered was not found
//			// Try again
//			// Or I hit a rate limit or something
//			errors = "Product not found.";
//			e.printStackTrace();
//		}
//		return errors;
//	}

//	public Product getProduct() {
//		return product;
//	}
//
//	public void setProduct(Product product) {
//		this.product = product;
//	}
}
