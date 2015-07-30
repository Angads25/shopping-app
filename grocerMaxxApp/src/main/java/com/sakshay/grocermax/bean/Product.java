package com.sakshay.grocermax.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class Product implements Serializable, Parcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private String productid;
	@Expose
	private String Name;
	@Expose
	private String Price;
	@Expose
	private String sale_price;
	@Expose
	private String Image;
	
	@Expose
	private String Status;
	
	private String quantity;
	
	@Expose
	private String p_brand;
	@Expose
	private String p_name;
	@Expose
	private String p_pack;
	@Expose
	private String promotion_level;
	
	
	public String getBrand(){
		return p_brand;
	}
	public void setBrand(String brand){
		this.p_brand = brand;
	}
	
	public String getProductName(){
		return p_name;
	}
	public void setProductName(String name){
		this.p_name = name;
	}
	
	public String getGramsORml(){
		return p_pack;
	}
	public void setGramsORml(String gramsorml){
		this.p_pack = gramsorml;
	}
	
	public String getPromotionLevel(){
		return promotion_level;
	}
	public void setPromotionLevel(String promotionLevel){
		this.promotion_level = promotion_level;
	}
	
	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	/**
	 * 
	 * @return The productid
	 */
	
	
	
	
	public Product(String name)
	{
		this.Name=name;
	}
	
	public String getProductid() {
		return productid;
	}

	/**
	 * 
	 * @param productid
	 *            The productid
	 */
	public void setProductid(String productid) {
		this.productid = productid;
	}

	/**
	 * 
	 * @return The Name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * 
	 * @param Name
	 *            The Name
	 */
	public void setName(String Name) {
		this.Name = Name;
	}

	/**
	 * 
	 * @return The Price
	 */
	public String getPrice() {
		return Price;
	}
	
	/**
	 * 
	 * @param Price
	 *            The Price
	 */
	public void setPrice(String Price) {
		this.Price = Price;
	}

	/**
	 * 
	 * @param sale_price
	 *            The Sale Price
	 */
	public void setSalePrice(String sale_price) {
		this.sale_price = sale_price;
	}
	
	/**
	 * 
	 * @return The Sale Price
	 */
	public String getSalePrice() {
		return sale_price;
	}

	/**
	 * 
	 * @return The Image
	 */
	public String getImage() {
		return Image;
	}

	/**
	 * 
	 * @param Image
	 *            The Image
	 */
	public void setImage(String Image) {
		this.Image = Image;
	}

	protected Product(Parcel in) {
		p_brand = in.readString();;
		p_name = in.readString();
		p_pack = in.readString();
		promotion_level = in.readString();
		productid = in.readString();
		Name = in.readString();
		Price = in.readString();
		Image = in.readString();
		sale_price = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(p_brand);
		dest.writeString(p_name);
		dest.writeString(p_pack);
		dest.writeString(promotion_level);
		dest.writeString(productid);
		dest.writeString(Name);
		dest.writeString(Price);
		dest.writeString(Image);
		dest.writeString(sale_price);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
		@Override
		public Product createFromParcel(Parcel in) {
			return new Product(in);
		}

		@Override
		public Product[] newArray(int size) {
			return new Product[size];
		}
	};

}
