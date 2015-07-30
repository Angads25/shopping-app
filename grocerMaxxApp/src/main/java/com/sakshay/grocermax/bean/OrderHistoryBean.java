package com.sakshay.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class OrderHistoryBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private List<Orderhistory> orderhistory = new ArrayList<Orderhistory>();

	/**
	 * 
	 * @return The orderhistory
	 */
	public List<Orderhistory> getOrderhistory() {
		return orderhistory;
	}

	/**
	 * 
	 * @param orderhistory
	 *            The orderhistory
	 */
	public void setOrderhistory(List<Orderhistory> orderhistory) {
		this.orderhistory = orderhistory;
	}

}