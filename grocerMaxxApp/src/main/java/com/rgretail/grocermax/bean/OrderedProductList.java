package com.rgretail.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class OrderedProductList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Expose
	private List<OrderDetailItem> items = new ArrayList<OrderDetailItem>();

	public List<OrderDetailItem> getItems() {
		return items;
	}

	public void setItems(List<OrderDetailItem> items) {
		this.items = items;
	}
	
	

}
