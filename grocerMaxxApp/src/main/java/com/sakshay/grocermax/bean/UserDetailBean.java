package com.sakshay.grocermax.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetailBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("Personal Info")
	@Expose
	private com.sakshay.grocermax.bean.PersonalInfo PersonalInfo;
	@Expose
	private com.sakshay.grocermax.bean.Address ShippingAddress;
	@Expose
	private com.sakshay.grocermax.bean.Address BillingAddress;

	/**
	 * 
	 * @return The PersonalInfo
	 */
	public com.sakshay.grocermax.bean.PersonalInfo getPersonalInfo() {
		return PersonalInfo;
	}

	/**
	 * 
	 * @param PersonalInfo
	 *            The Personal Info
	 */
	public void setPersonalInfo(com.sakshay.grocermax.bean.PersonalInfo PersonalInfo) {
		this.PersonalInfo = PersonalInfo;
	}

	/**
	 * 
	 * @return The ShippingAddress
	 */
	public com.sakshay.grocermax.bean.Address getShippingAddress() {
		return ShippingAddress;
	}

	/**
	 * 
	 * @param ShippingAddress
	 *            The ShippingAddress
	 */
	public void setShippingAddress(com.sakshay.grocermax.bean.Address ShippingAddress) {
		this.ShippingAddress = ShippingAddress;
	}

	/**
	 * 
	 * @return The BillingAddress
	 */
	public com.sakshay.grocermax.bean.Address getBillingAddress() {
		return BillingAddress;
	}

	/**
	 * 
	 * @param BillingAddress
	 *            The BillingAddress
	 */
	public void setBillingAddress(com.sakshay.grocermax.bean.Address BillingAddress) {
		this.BillingAddress = BillingAddress;
	}

}