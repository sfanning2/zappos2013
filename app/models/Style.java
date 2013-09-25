package models;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;

@Entity
public class Style implements Serializable{	
	private static final long serialVersionUID = -7316803510782756529L;
	
	@Id
	public long id;
	
	@Required
	private long styleId;

	private String imageUrl;
	private String price;
	private String percentOff;
	private String color;
	private String originalPrice;
	private String productUrl;
	
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
	
	/**
	 * Compare by id to get matching style
	 * @param styles
	 * @return
	 */
	public Style getMatchingStyle(Set<Style> styles) {
		for (Style s : styles) {
			if (s.styleId == this.styleId) {
				return s;
			}
		}
		return null;
	}
	
}
