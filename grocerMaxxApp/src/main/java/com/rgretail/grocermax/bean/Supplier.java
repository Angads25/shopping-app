package com.rgretail.grocermax.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class Supplier implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private Boolean IsSelected;
	@Expose
	private Object OrderStatus;
	@Expose
	private String SupplierId;
	@Expose
	private String SupplierName;

	/**
	 * 
	 * @return The IsSelected
	 */
	public Boolean getIsSelected() {
		return IsSelected;
	}

	/**
	 * 
	 * @param IsSelected
	 *            The IsSelected
	 */
	public void setIsSelected(Boolean IsSelected) {
		this.IsSelected = IsSelected;
	}

	/**
	 * 
	 * @return The OrderStatus
	 */
	public Object getOrderStatus() {
		return OrderStatus;
	}

	/**
	 * 
	 * @param OrderStatus
	 *            The OrderStatus
	 */
	public void setOrderStatus(Object OrderStatus) {
		this.OrderStatus = OrderStatus;
	}

	/**
	 * 
	 * @return The SupplierId
	 */
	public String getSupplierId() {
		return SupplierId;
	}

	/**
	 * 
	 * @param SupplierId
	 *            The SupplierId
	 */
	public void setSupplierId(String SupplierId) {
		this.SupplierId = SupplierId;
	}

	/**
	 * 
	 * @return The SupplierName
	 */
	public String getSupplierName() {
		return SupplierName;
	}

	/**
	 * 
	 * @param SupplierName
	 *            The SupplierName
	 */
	public void setSupplierName(String SupplierName) {
		this.SupplierName = SupplierName;
	}

}