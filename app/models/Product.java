package models;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Product implements Serializable{
	
	private static final long serialVersionUID = -4305347155029455608L;

	@Id
	public long id;
	
	private long productId;
	private long brandId;
	private String brandName;
	private String productName;
	private String defaultProductUrl;
	private String defaultImageUrl;
	private Set<Style> styles;


	public long getBrandId() {
		return brandId;
	}
	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getDefaultProductUrl() {
		return defaultProductUrl;
	}
	public void setDefaultProductUrl(String defaultProductUrl) {
		this.defaultProductUrl = defaultProductUrl;
	}
	public String getDefaultImageUrl() {
		return defaultImageUrl;
	}
	public void setDefaultImageUrl(String defaultImageUrl) {
		this.defaultImageUrl = defaultImageUrl;
	}

	@OneToMany (cascade = CascadeType.ALL)
	public Set<Style> getStyles() {
		return styles;
	}
	public void setStyles(Set<Style> styles) {
		this.styles = styles;
	}

}
