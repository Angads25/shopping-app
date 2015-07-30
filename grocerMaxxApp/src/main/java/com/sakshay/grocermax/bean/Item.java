package com.sakshay.grocermax.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("product_id")
	@Expose
	private String productId;
	@SerializedName("product_thumbnail")
	@Expose
	private String productThumbnail;
	@SerializedName("product_name")
	@Expose
	private String productName;
	@Expose
	private String sku;
	@SerializedName("product_description")
	@Expose
	private String productDescription;
	@SerializedName("product_qty")
	@Expose
	private String productQty;
	@SerializedName("Basecalculation_price")
	@Expose
	private String BasecalculationPrice;
	@SerializedName("item_total")
	@Expose
	private String itemTotal;
	@SerializedName("product_type")
	@Expose
	private String productType;

	/**
	 * 
	 * @return The productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * 
	 * @param productId
	 *            The product_id
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * 
	 * @return The productThumbnail
	 */
	public String getProductThumbnail() {
		return productThumbnail;
	}

	/**
	 * 
	 * @param productThumbnail
	 *            The product_thumbnail
	 */
	public void setProductThumbnail(String productThumbnail) {
		this.productThumbnail = productThumbnail;
	}

	/**
	 * 
	 * @return The productName
	 */
	public Object getProductName() {
		return productName;
	}

	/**
	 * 
	 * @param productName
	 *            The product_name
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * 
	 * @return The sku
	 */
	public String getSku() {
		return sku;
	}

	/**
	 * 
	 * @param sku
	 *            The sku
	 */
	public void setSku(String sku) {
		this.sku = sku;
	}

	/**
	 * 
	 * @return The productDescription
	 */
	public Object getProductDescription() {
		return productDescription;
	}

	/**
	 * 
	 * @param productDescription
	 *            The product_description
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	/**
	 * 
	 * @return The productQty
	 */
	public String getProductQty() {
		return productQty;
	}

	/**
	 * 
	 * @param productQty
	 *            The product_qty
	 */
	public void setProductQty(String productQty) {
		this.productQty = productQty;
	}

	/**
	 * 
	 * @return The BasecalculationPrice
	 */
	public String getBasecalculationPrice() {
		return BasecalculationPrice;
	}

	/**
	 * 
	 * @param BasecalculationPrice
	 *            The Basecalculation_price
	 */
	public void setBasecalculationPrice(String BasecalculationPrice) {
		this.BasecalculationPrice = BasecalculationPrice;
	}

	/**
	 * 
	 * @return The itemTotal
	 */
	public String getItemTotal() {
		return itemTotal;
	}

	/**
	 * 
	 * @param itemTotal
	 *            The item_total
	 */
	public void setItemTotal(String itemTotal) {
		this.itemTotal = itemTotal;
	}

	/**
	 * 
	 * @return The productType
	 */
	public Object getProductType() {
		return productType;
	}

	/**
	 * 
	 * @param productType
	 *            The product_type
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}

}