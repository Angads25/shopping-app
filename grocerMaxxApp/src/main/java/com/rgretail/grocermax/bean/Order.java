package com.rgretail.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("order_id")
	@Expose
	private String orderId;
	@Expose
	private List<Item> items = new ArrayList<Item>();
	@SerializedName("order_date")
	@Expose
	private String orderDate;
	@Expose
	private String email;
	@Expose
	private String total;
	@Expose
	private String status;
	@Expose
	private String name;

	/**
	 * 
	 * @return The orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * 
	 * @param orderId
	 *            The order_id
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * 
	 * @return The items
	 */
	public List<Item> getItems() {
		return items;
	}

	/**
	 * 
	 * @param items
	 *            The items
	 */
	public void setItems(List<Item> items) {
		this.items = items;
	}

	/**
	 * 
	 * @return The orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}

	/**
	 * 
	 * @param orderDate
	 *            The order_date
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * 
	 * @return The email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email
	 *            The email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return The total
	 */
	public String getTotal() {
		return total;
	}

	/**
	 * 
	 * @param total
	 *            The total
	 */
	public void setTotal(String total) {
		this.total = total;
	}

	/**
	 * 
	 * @return The status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 *            The status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	public void setName(String name) {
		this.name = name;
	}

}