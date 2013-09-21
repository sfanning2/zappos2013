package models;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Product extends Model{

	private static final long serialVersionUID = -2931587834427892120L;

	// id 
	@Id
	public long myId;
	

	// name
	// currentPrice
	// originalPrice
	
	
//	 * product: (list)
	
//	 * brandId
	private long brandId;
//	 * brandName
	private String brandName;
//	 * productId
	private long productId;
//	 * productName
	private String productName;
	private String defaultProductUrl;
	private String defaultImageUrl;
//	 * 
//	 * list of styles:
	private Style[] styles;
	
	 
	public Product() {
		
	}
	public Product(int id, String name, BigDecimal currentPrice, BigDecimal originalPrice) {
	
		
	}
	
	public Product(String json) {
		
	}


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
	public Style[] getStyles() {
		return styles;
	}
	public void setStyles(Style[] styles) {
		this.styles = styles;
	}
	



	//	 * styleId:
//	 * imageUrl:
//	 * price:
//	 * percentOff:
//	 * color:
//	 * originalPrice:
//	 * productUrl:
	public static class Style {

		private long styleId;
		private String imageUrl;
		private String price;
		private String percentOff;
		private String color;
		private String originalPrice;
		private String productUrl;
		
		public Style() {
			
		}
		
		public long getStyleId() {
			return styleId;
		}
		public void setStyleId(long styleId) {
			this.styleId = styleId;
		}
		public String getImageUrl() {
			return imageUrl;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getPercentOff() {
			return percentOff;
		}
		public void setPercentOff(String percentOff) {
			this.percentOff = percentOff;
		}
		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
		public String getOriginalPrice() {
			return originalPrice;
		}
		public void setOriginalPrice(String originalPrice) {
			this.originalPrice = originalPrice;
		}
		public String getProductUrl() {
			return productUrl;
		}
		public void setProductUrl(String productUrl) {
			this.productUrl = productUrl;
		}
		
	}

}
