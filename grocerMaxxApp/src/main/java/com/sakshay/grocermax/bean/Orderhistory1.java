package com.sakshay.grocermax.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class Orderhistory1 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private Order order;

	/**
	 * 
	 * @return The order
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * 
	 * @param order
	 *            The order
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

}