package com.rgretail.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class CartDetailBean extends BaseResponseBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private ArrayList<CartDetail> items = new ArrayList<CartDetail>();
	@Expose
	private String subtotal;
	@Expose
	private String grand_total;
	
	@Expose
	private String items_count;
	
	@Expose
	private String items_qty;
	
	@Expose
	private String mrp;
	

	

	
	public String getMrp() {
		return mrp;
	}

	public void setMrp(String mrp) {
		this.mrp = mrp;
	}
	
	
	
	/**
	 * 
	 * @return The CartDetail
	 */
	

	public String getItems_qty() {
		return items_qty;
	}

	public void setItems_qty(String items_qty) {
		this.items_qty = items_qty;
	}

	/**
	 * 
	 * @return The SubTotal
	 */
	
	
	public String getSubTotal() {
		return subtotal;
	}

	public String getItems_count() {
		return items_count;
	}

	public void setItems_count(String items_count) {
		this.items_count = items_count;
	}

	public ArrayList<CartDetail> getItems() {
		return items;
	}

	public void setItems(ArrayList<CartDetail> items) {
		this.items = items;
	}

	/**
	 * 
	 * @param SubTotal
	 *            The SubTotal
	 */
	public void setSubTotal(String SubTotal) {
		this.subtotal = SubTotal;
	}

	/**
	 * 
	 * @return The GrandTotal
	 */
	public String getGrandTotal() {
		return grand_total;
	}

	/**
	 * 
	 * @param GrandTotal
	 *            The GrandTotal
	 */
	public void setGrandTotal(String GrandTotal) {
		this.grand_total = GrandTotal;
	}

	

}
