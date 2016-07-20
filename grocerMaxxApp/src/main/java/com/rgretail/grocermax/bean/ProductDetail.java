package com.rgretail.grocermax.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("product_name")
	@Expose
	private String productName;
	@SerializedName("product_id")
	@Expose
	private String productId;
	@SerializedName("product_price")
	@Expose
	private String productPrice;
	@Expose
	private String sale_price;
	@Expose
	private String Status;
	@SerializedName("product_thumbnail")
	@Expose
	private String productThumbnail;
	@SerializedName("product_qty")
	@Expose
	private String productQty;
	@SerializedName("product_description")
	@Expose
	private String productDescription;

	@SerializedName("p_brand")
	@Expose
	private String productBrand;

	@Expose
	private String categoryid;

    @SerializedName("promotion_level")
    @Expose
    private String productPromotion;

	
	@Expose
	private String p_name;
	@SerializedName("p_pack")
	@Expose
	private String productPack;


	public String getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}

	public String getProductPromotion() {
        return productPromotion;
    }

    public void setProductPromotion(String productPromotion) {
        this.productPromotion = productPromotion;
    }

    public String getProductBrand() {
		return productBrand;
	}

	public void setProductBrand(String prodbrand) {
		this.productBrand = prodbrand;
	}

	public String getProductSingleName() {
		return p_name;
	}

	public void setProductSingleName(String prodsinglename) {
		this.p_name = prodsinglename;
	}
	
	public String getProductPack() {
		return productPack;
	}

	public void setProductPack(String prodpack) {
		this.productPack = prodpack;
	}
	
	
	
	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getSale_price() {
		return sale_price;
	}

	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
	}

	/**
	 * 
	 * @return The productName
	 */
	public String getProductName() {
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
	 * @return The productPrice
	 */
	public String getProductPrice() {
		return productPrice;
	}

	/**
	 * 
	 * @param productPrice
	 *            The product_price
	 */
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
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
	 * @return The productDescription
	 */
	public String getProductDescription() {
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

}
