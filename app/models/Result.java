package models;

import java.io.Serializable;

import javax.persistence.Id;

import play.data.validation.Constraints.Required;

public class Result implements Serializable{

	private static final long serialVersionUID = -2705814752444791706L;
	
	@Id
	public long id;
	@Required
	private long styleId;
	@Required
	private long productId;
	private long colorId;
	private String price;
	private String originalPrice;
	private String productUrl;
	private String productName;
	private String brandName;
	private String thumbnailImageUrl;
	private String percentOff;
	
	public long getStyleId() {
		return styleId;
	}
	public void setStyleId(long styleId) {
		this.styleId = styleId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
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
	public long getColorId() {
		return colorId;
	}
	public void setColorId(long colorId) {
		this.colorId = colorId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getThumbnailImageUrl() {
		return thumbnailImageUrl;
	}
	public void setThumbnailImageUrl(String thumbnailImageUrl) {
		this.thumbnailImageUrl = thumbnailImageUrl;
	}
	public String getPercentOff() {
		return percentOff;
	}
	public void setPercentOff(String percentOff) {
		this.percentOff = percentOff;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
}
