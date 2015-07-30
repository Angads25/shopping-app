package com.sakshay.grocermax.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class OrderDetailItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Expose
	private String name;
	
	@Expose
	private String qty_ordered;
	
	@Expose
	private String price;
	
	@Expose
	private String row_total;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQty_ordered() {
		return qty_ordered;
	}

	public void setQty_ordered(String qty_ordered) {
		this.qty_ordered = qty_ordered;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRow_total() {
		return row_total;
	}

	public void setRow_total(String row_total) {
		this.row_total = row_total;
	}
	
	
	

}
