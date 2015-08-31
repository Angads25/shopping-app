package com.sakshay.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.annotations.Expose;

public class OrderReviewBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 706601391776181200L;
	
	@Expose
	private List<CartDetail> Product = new ArrayList<CartDetail>();
	@Expose
	private String SubTotal;
	@Expose
	private String GrandTotal;
	@Expose
	private String Shipping_ammount;
	@Expose
	private String discount_amount;               //contains int value with minus sign
	@Expose
	private String Tax_ammount;
	@Expose
	private JSONObject shipping;
	@Expose
	private JSONObject billing;
	@Expose
	private String date;
	@Expose
	private String timeSlot;
	
	@Expose
	private String saving;
	@Expose
	private String coupon_code;
	@Expose
	private String subtotal_with_discount;
	@Expose
	private String you_save;

	public String getCouponDiscount(){
		return  you_save;
	}
	public void setCouponDiscount(String you_save){
		this.you_save = you_save;
	}

	public String getCouponCode(){
		return coupon_code;
	}
	public void setCouponCode(String coupon_code){
		this.coupon_code = coupon_code;
	}
	public void setCouponSubtotalWithDiscount(String subtotal_with_discount){
		this.subtotal_with_discount = subtotal_with_discount;
	}
	public String getCouponSubtotalWithDsicount(){
		return subtotal_with_discount;
	}

	public String getSaving() {
		return saving;
	}
	public void setSaving(String saving) {
		this.saving = saving;
	}
	public String getDiscount_amount() {
		return discount_amount;
	}
	public void setDiscount_amount(String discount_amount) {
		this.discount_amount = discount_amount;
	}
	public List<CartDetail> getProduct() {
		return Product;
	}
	public void setProduct(List<CartDetail> product) {
		Product = product;
	}
	public String getSubTotal() {
		return SubTotal;
	}
	public void setSubTotal(String subTotal) {
		SubTotal = subTotal;
	}
	public String getGrandTotal() {
		return GrandTotal;
	}
	public void setGrandTotal(String grandTotal) {
		GrandTotal = grandTotal;
	}
	public String getShipping_ammount() {
		return Shipping_ammount;
	}
	public void setShipping_ammount(String shipping_ammount) {
		Shipping_ammount = shipping_ammount;
	}
	public String getTax_ammount() {
		return Tax_ammount;
	}
	public void setTax_ammount(String tax_ammount) {
		Tax_ammount = tax_ammount;
	}
	public JSONObject getShipping() {
		return shipping;
	}
	public void setShipping(JSONObject shipping) {
		this.shipping = shipping;
	}
	public JSONObject getBilling() {
		return billing;
	}
	public void setBilling(JSONObject billing) {
		this.billing = billing;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}
	
	
	

}
