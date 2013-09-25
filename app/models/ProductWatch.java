package models;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;



//import models.Product.Style;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@Table(name="e_watchtable")
public class ProductWatch extends Model{
	
	private static final long serialVersionUID = -1106982547808954964L;

	@Id
	public long id;
	
	@Required
	private long productId;
	
	@Required
	private String emailAddress;
	
	private Product theProduct;

	@OneToOne (cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	public Product getTheProduct() {
		return theProduct;
	}

	public void setTheProduct(Product product) {
		this.theProduct = product;
	}
	
	public ProductWatch() {
	}
	
	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}



}
