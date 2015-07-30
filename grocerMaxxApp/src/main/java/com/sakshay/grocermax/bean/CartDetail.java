package com.sakshay.grocermax.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class CartDetail extends BaseResponseBean implements Serializable,Parcelable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private String product_id;
	@Expose
	private String name;
	@Expose
	private String sku;
	@Expose
	private Integer qty;
	@Expose
	private String price;
	@Expose
	private String product_thumbnail;
	
	@Expose
	private String mrp;
	
	@Expose
	private String p_brand_cart;
	@Expose
	private String p_name_cart;
	@Expose
	private String p_pack_cart;
	@Expose
	private String promotionLevel;
	

	public String getBrand(){
		return p_brand_cart;
	}
	public void setBrand(String brand){
		this.p_brand_cart = brand;
	}
	
	public String getProductName(){
		return p_name_cart;
	}
	public void setProductName(String name){
		this.p_name_cart = name;
	}
	
	public String getGramsORml(){
		return p_pack_cart;
	}
	public void setGramsORml(String gramsorml){
		this.p_pack_cart = gramsorml;
	}

	public String getPromotionLevel(){
		return promotionLevel;
	}
	public void setPromotionLevel(String promotionLevel){
		this.promotionLevel = promotionLevel;
	}
	
	public String getMrp() {
		return mrp;
	}

	public void setMrp(String mrp) {
		this.mrp = mrp;
	}

	public String getItem_id() {
		return product_id;
	}

	public void setItem_id(String item_id) {
		this.product_id = item_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getProduct_thumbnail() {
		return product_thumbnail;
	}

	public void setProduct_thumbnail(String product_thumbnail) {
		this.product_thumbnail = product_thumbnail;
	}

	public CartDetail()
	{
		
	}
	
	protected CartDetail(Parcel in) {
		p_brand_cart = in.readString();
		p_name_cart = in.readString();
		p_pack_cart = in.readString();
		promotionLevel = in.readString();
		product_id = in.readString();
		name = in.readString();
		price = in.readString();
		sku = in.readString();
		qty = in.readInt();
		mrp = in.readString();
		product_thumbnail = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	dest.writeString(p_brand_cart);
    	dest.writeString(p_name_cart);
    	dest.writeString(p_pack_cart);
    	dest.writeString(promotionLevel);
        dest.writeString(product_id);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(sku);
        dest.writeInt(qty);
        dest.writeString(mrp);
        dest.writeString(product_thumbnail);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CartDetail> CREATOR = new Parcelable.Creator<CartDetail>() {
        @Override
        public CartDetail createFromParcel(Parcel in) {
            return new CartDetail(in);
        }

        @Override
        public CartDetail[] newArray(int size) {
            return new CartDetail[size];
        }
    };

}
